package com.blissstock.mappingSite.controller;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.websocket.server.PathParam;

import org.hibernate.type.TrueFalseType;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.blissstock.mappingSite.entity.Test;
import com.blissstock.mappingSite.entity.TestQuestion;
import com.blissstock.mappingSite.entity.TestQuestionCorrectAnswer;
import com.blissstock.mappingSite.entity.TestStudentAnswer;
import com.blissstock.mappingSite.entity.UserInfo;
import com.blissstock.mappingSite.exceptions.UnauthorizedFileAccessException;
import com.blissstock.mappingSite.model.AnswerModel;
import com.blissstock.mappingSite.model.ChoiceModel;
import com.blissstock.mappingSite.model.FileInfo;
import com.blissstock.mappingSite.model.QuestionAndCorrectAnswer;
import com.blissstock.mappingSite.model.QuestionAndCorrectAnswerAndStudentAnswer;
import com.blissstock.mappingSite.model.StudentChoiceModel;
import com.blissstock.mappingSite.repository.TestQuestionCorrectAnswerRepositoy;
import com.blissstock.mappingSite.repository.TestQuestionRepository;
import com.blissstock.mappingSite.repository.TestRepository;
import com.blissstock.mappingSite.repository.TestStudentAnswerRepository;
import com.blissstock.mappingSite.repository.UserInfoRepository;
import com.blissstock.mappingSite.service.StorageService;
import com.blissstock.mappingSite.service.StorageServiceImpl;
import com.blissstock.mappingSite.service.UserSessionService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;

@Controller
public class TestQuestionController {
    private static Logger logger = LoggerFactory.getLogger(
            TestQuestionController.class);

    @Autowired
    UserSessionService userSessionService;

    @Autowired
    TestQuestionRepository testQuestionRepository;

    @Autowired
    TestRepository testRepository;

    @Autowired
    TestQuestionCorrectAnswerRepositoy testQuestionCorrectAnswerRepositoy;

    @Autowired
    StorageService storageService;

    @Autowired
    TestStudentAnswerRepository testStudentAnswerRepository;

    @Autowired
    UserInfoRepository userInfoRepository;

    @Valid
    @GetMapping(value = { "/teacher/exam/{test_id}/questions", "/admin/exam/{test_id}/questions" })
    private String getQuestionsByTest(@PathVariable Long test_id, Model model)
            throws ParseException, JsonMappingException, JsonProcessingException {
        List<QuestionAndCorrectAnswer> questionAndCorrectAnswers = new ArrayList<>();
        List<TestQuestion> testQuestions = testQuestionRepository.getQuestionByTest(test_id);
        for (TestQuestion testQuestion : testQuestions) {
            long fileSeparator = 100000L + test_id;
            FileInfo file = storageService.loadQuestionMaterials(fileSeparator, testQuestion.getQuestion_materials());
            testQuestion.setQuestion_materials(file.getUrl());

            List<ChoiceModel> choices = new ArrayList<>();
            if (!testQuestion.getQuestion_type().equals("FREE_ANSWER")) {
                TestQuestionCorrectAnswer testQuestionCorrectAnswer = testQuestionCorrectAnswerRepositoy
                        .getCorrectAnswerByQuestion(testQuestion.getId());
                JSONArray choiceArrary = new JSONArray(testQuestion.getChoices());
                JSONArray answerArray = new JSONArray(testQuestionCorrectAnswer.getCorrectAnswer());
                for (int i = 0; i < choiceArrary.length(); i++) {
                    JSONObject choice = choiceArrary.getJSONObject(i);
                    String choiceData = choice.getString("choice");
                    choices.add(new ChoiceModel(i, choiceData, false));
                }

                for (int j = 0; j < answerArray.length(); j++) {
                    JSONObject answer = answerArray.getJSONObject(j);
                    int correct = answer.getInt("answer");
                    String choice = choices.get(correct).getChoice();
                    choices.set(correct, new ChoiceModel(correct, choice, true));
                }
            }

            QuestionAndCorrectAnswer questionAndCorrectAnswer = new QuestionAndCorrectAnswer(testQuestion.getId(),
                    testQuestion.getQuestion_text(), testQuestion.getQuestion_materials(),
                    testQuestion.getQuestion_materials_type(), choices,
                    testQuestion.getQuestion_type(), testQuestion.getMaximum_mark());
            questionAndCorrectAnswers.add(questionAndCorrectAnswer);
        }
        model.addAttribute("test_id", test_id);
        model.addAttribute("user_role", userSessionService.getRole());
        model.addAttribute("questionList", questionAndCorrectAnswers);
        return "AT0007_TestQuestions.html";
    }

    @Valid
    @GetMapping(value = { "/teacher/exam/{test_id}/student/{student_id}",
            "/admin/exam/{test_id}/student/{student_id}" })
    private String getStudentAnswer(@PathVariable Long test_id,
            @PathVariable Long student_id, Model model)
            throws ParseException, JsonMappingException, JsonProcessingException {
        List<QuestionAndCorrectAnswerAndStudentAnswer> questionAndCorrectAnswers = new ArrayList<>();
        List<TestQuestion> testQuestions = testQuestionRepository.getQuestionByTest(test_id);
        Integer freeAnswerCount = testQuestionRepository.getFreeAnswerCount();
        Integer markingCount = testStudentAnswerRepository.getMarkingQuestionCount();
        Test test = testRepository.getTestByID(test_id);

        for (TestQuestion testQuestion : testQuestions) {
            String studentAnswer = "";
            String studentAnswerURL = "";
            Integer student_answer_id = 0;
            long fileSeparator = 100000L + test_id;
            FileInfo file = storageService.loadQuestionMaterials(fileSeparator, testQuestion.getQuestion_materials());
            testQuestion.setQuestion_materials(file.getUrl());
            int acquired_mark = 0;
            List<StudentChoiceModel> choices = new ArrayList<>();
            if (!testQuestion.getQuestion_type().equals("FREE_ANSWER")) {
                TestQuestionCorrectAnswer testQuestionCorrectAnswer = testQuestionCorrectAnswerRepositoy
                        .getCorrectAnswerByQuestion(testQuestion.getId());
                TestStudentAnswer testStudentAnswer = testStudentAnswerRepository.getStudentAnswer(student_id,
                        testQuestion.getId());

                JSONArray choiceArrary = new JSONArray(testQuestion.getChoices());
                JSONArray answerArray = new JSONArray(testQuestionCorrectAnswer.getCorrectAnswer());
                JSONArray studentchoiceArray = new JSONArray();
                if (testStudentAnswer != null) {
                    studentchoiceArray = new JSONArray(testStudentAnswer.getStudent_answer());
                }
                for (int i = 0; i < choiceArrary.length(); i++) {
                    JSONObject choice = choiceArrary.getJSONObject(i);
                    String choiceData = choice.getString("choice");
                    choices.add(new StudentChoiceModel(i, choiceData, false, false));
                }

                for (int j = 0; j < answerArray.length(); j++) {
                    JSONObject answer = answerArray.getJSONObject(j);
                    int correct = answer.getInt("answer");
                    String choice = choices.get(correct).getChoice();
                    choices.set(correct, new StudentChoiceModel(correct, choice, true, false));
                }

                if (testStudentAnswer != null) {
                    for (int k = 0; k < studentchoiceArray.length(); k++) {
                        JSONObject answer = studentchoiceArray.getJSONObject(k);
                        int student_choice = answer.getInt("student_choice");
                        String choice = choices.get(student_choice).getChoice();
                        Boolean correctAnswer = choices.get(student_choice).isCorrect();
                        choices.set(student_choice,
                                new StudentChoiceModel(student_choice, choice, correctAnswer, true));
                    }
                }

                for (int l = 0; l < choices.size(); l++) {
                    Boolean correctAnswer = choices.get(l).isCorrect();
                    Boolean studentChoice = choices.get(l).isStudent_choice();
                    if (correctAnswer && studentChoice) {
                        acquired_mark = testQuestion.getMaximum_mark();
                    }
                }
            } else {
                TestStudentAnswer testStudentAnswer = testStudentAnswerRepository.getStudentAnswer(student_id,
                        testQuestion.getId());
                if (testStudentAnswer != null) {
                    acquired_mark = testQuestion.getMaximum_mark();
                    long studentfileSeparator = Long.parseLong(test_id.toString()
                            + testStudentAnswer.getQuestion().getId().toString() + student_id.toString());
                    studentAnswer = testStudentAnswer.getStudent_answer();
                    FileInfo studentAnswerFile = storageService.loadAnswermaterials(studentfileSeparator,
                            testStudentAnswer.getStudent_answer_link());
                    studentAnswerURL = studentAnswerFile.getUrl();
                    student_answer_id = Integer.parseInt(testStudentAnswer.getId().toString());
                }
            }
            QuestionAndCorrectAnswerAndStudentAnswer studentAnswerList = new QuestionAndCorrectAnswerAndStudentAnswer(
                    testQuestion.getId(),
                    student_answer_id,
                    studentAnswer, studentAnswerURL,
                    testQuestion.getQuestion_text(), testQuestion.getQuestion_materials(),
                    testQuestion.getQuestion_materials_type(), choices,
                    testQuestion.getQuestion_type(), testQuestion.getMaximum_mark(),
                    acquired_mark);
            questionAndCorrectAnswers.add(studentAnswerList);
        }
        model.addAttribute("test_id", test_id);
        model.addAttribute("questionList", questionAndCorrectAnswers);
        model.addAttribute("test_date", test.getDate());
        model.addAttribute("name", test.getUserInfo().getUserName());
        model.addAttribute("totalTest", testQuestions.size());
        model.addAttribute("freeTest", freeAnswerCount);
        model.addAttribute("choiceTest", testQuestions.size() - freeAnswerCount);
        if (markingCount == 0) {
            model.addAttribute("status", "Completed");
        } else {
            model.addAttribute("status", "Marking");
        }

        return "AT0006_StudentAnswerList.html";
    }

    @Valid
    @GetMapping(value = { "/student/exam/{test_id}/questions" })
    private String getStudentQuestions(@PathVariable Long test_id, Model model)
            throws ParseException, JsonMappingException, JsonProcessingException {
        List<QuestionAndCorrectAnswer> questionAndCorrectAnswers = new ArrayList<>();
        Test test = testRepository.getTestByID(test_id);
        List<TestQuestion> testQuestions = testQuestionRepository.getQuestionByTest(test_id);
        for (TestQuestion testQuestion : testQuestions) {
            long fileSeparator = 100000L + test_id;
            FileInfo file = storageService.loadQuestionMaterials(fileSeparator, testQuestion.getQuestion_materials());
            testQuestion.setQuestion_materials(file.getUrl());

            List<ChoiceModel> choices = new ArrayList<>();
            if (!testQuestion.getQuestion_type().equals("FREE_ANSWER")) {
                TestQuestionCorrectAnswer testQuestionCorrectAnswer = testQuestionCorrectAnswerRepositoy
                        .getCorrectAnswerByQuestion(testQuestion.getId());
                JSONArray choiceArrary = new JSONArray(testQuestion.getChoices());
                JSONArray answerArray = new JSONArray(testQuestionCorrectAnswer.getCorrectAnswer());
                for (int i = 0; i < choiceArrary.length(); i++) {
                    JSONObject choice = choiceArrary.getJSONObject(i);
                    String choiceData = choice.getString("choice");
                    choices.add(new ChoiceModel(i, choiceData, false));
                }

                for (int j = 0; j < answerArray.length(); j++) {
                    JSONObject answer = answerArray.getJSONObject(j);
                    int correct = answer.getInt("answer");
                    String choice = choices.get(correct).getChoice();
                    choices.set(correct, new ChoiceModel(correct, choice, true));
                }
            }

            QuestionAndCorrectAnswer questionAndCorrectAnswer = new QuestionAndCorrectAnswer(testQuestion.getId(),
                    testQuestion.getQuestion_text(), testQuestion.getQuestion_materials(),
                    testQuestion.getQuestion_materials_type(), choices,
                    testQuestion.getQuestion_type(), testQuestion.getMaximum_mark());
            questionAndCorrectAnswers.add(questionAndCorrectAnswer);
        }
        model.addAttribute("test_date", test.getDate());
        model.addAttribute("test_start_time", test.getStart_time());
        model.addAttribute("test_end_time", test.getEnd_time());
        model.addAttribute("test_id", test_id);
        model.addAttribute("questionList", questionAndCorrectAnswers);
        return "ST0006_ExamQuestionListStudent.html";
    }

    @Valid
    @PostMapping(value = { "/teacher/create-question", "/admin/create-question" })
    private ResponseEntity createQuestionByTestID(
            @RequestParam(value = "test_id") Long testID,
            @RequestParam(value = "question_text") String question_text,
            @RequestParam(required = false, value = "question_materials") MultipartFile question_materials,
            @RequestParam(required = false, value = "choices") String choices,
            @RequestParam(required = false, value = "answers") String answers,
            @RequestParam(value = "questions_type") String questions_type,
            @RequestParam(value = "maximum_mark") Integer maximum_mark)
            throws ParseException, UnauthorizedFileAccessException {
        Long userID = getUid();
        String fileType = "BLANK";
        String originalFileName = "";
        if (question_materials != null) {
            originalFileName = StringUtils.cleanPath(
                    question_materials.getOriginalFilename());
            int index = originalFileName.lastIndexOf(".");
            fileType = originalFileName.substring(index + 1);

            long fileSeparator = 100000L + testID;
            try {
                storageService.storeQuestionMaterials(fileSeparator, question_materials,
                        StorageServiceImpl.QUESTION_MATERIAL_PATH,
                        false);
            } catch (Exception e) {
                logger.error(e.getLocalizedMessage(), e);
            }
            if (fileType.equals("jpg") || fileType.equals("png") || fileType.equals("jpeg")) {
                fileType = "IMAGE";
            } else if (fileType.equals("mp3")) {
                fileType = "AUDIO";
            } else {
                fileType = "VIDEO";
            }
        }
        Test test = testRepository.getTestByID(testID);
        TestQuestion testQuestion = new TestQuestion(null, test, question_text, originalFileName, fileType, choices,
                questions_type, maximum_mark);
        TestQuestion result = testQuestionRepository.save(testQuestion);
        TestQuestionCorrectAnswer correctAnswer = new TestQuestionCorrectAnswer(null, result, answers);
        testQuestionCorrectAnswerRepositoy.save(correctAnswer);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @Valid
    @PostMapping(value = { "/teacher/edit-question", "/admin/edit-question" })
    private ResponseEntity editQuestionByTestID(
            @RequestParam(value = "test_id") Long testID,
            @RequestParam(value = "question_id") Long questionID,
            @RequestParam(value = "question_text") String question_text,
            @RequestParam(required = false, value = "question_materials") MultipartFile question_materials,
            @RequestParam(required = false, value = "choices") String choices,
            @RequestParam(required = false, value = "answers") String answers,
            @RequestParam(value = "questions_type") String questions_type,
            @RequestParam(value = "maximum_mark") Integer maximum_mark)
            throws ParseException, UnauthorizedFileAccessException {

        Long userID = getUid();
        String fileType = "BLANK";
        String originalFileName = "";
        TestQuestion initQuestion = testQuestionRepository.getById(questionID);
        if (question_materials != null) {
            originalFileName = StringUtils.cleanPath(
                    question_materials.getOriginalFilename());
            int index = originalFileName.lastIndexOf(".");
            fileType = originalFileName.substring(index + 1);

            long fileSeparator = 100000L + testID;
            try {
                storageService.storeQuestionMaterials(fileSeparator, question_materials,
                        StorageServiceImpl.QUESTION_MATERIAL_PATH,
                        false);
            } catch (Exception e) {
                logger.error(e.getLocalizedMessage(), e);
            }
            if (fileType.equals("jpg") || fileType.equals("png") || fileType.equals("jpeg")) {
                fileType = "IMAGE";
            } else if (fileType.equals("mp3")) {
                fileType = "AUDIO";
            } else {
                fileType = "VIDEO";
            }
        } else {
            fileType = initQuestion.getQuestion_materials_type();
            originalFileName = initQuestion.getQuestion_materials();
        }
        Test test = testRepository.getTestByID(testID);
        TestQuestion testQuestion = new TestQuestion(questionID, test, question_text, originalFileName, fileType,
                choices, questions_type, maximum_mark);
        TestQuestion result = testQuestionRepository.save(testQuestion);
        TestQuestionCorrectAnswer testQuestionCorrectAnswer = testQuestionCorrectAnswerRepositoy
                .getCorrectAnswerByQuestion(result.getId());
        TestQuestionCorrectAnswer correctAnswer = new TestQuestionCorrectAnswer(testQuestionCorrectAnswer.getId(),
                result, answers);
        testQuestionCorrectAnswerRepositoy.save(correctAnswer);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @Valid
    @GetMapping(value = { "/teacher/delete-question", "/admin/delete-question" })
    private ResponseEntity deleteQuestionByQuestionID(@RequestParam(required = false) Long question_id) {
        Integer deleteCorrectAnswer = testQuestionCorrectAnswerRepositoy.deleteByQuestionID(question_id);
        Integer deleteQuestion = testQuestionRepository.deleteQuestionByID(question_id);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    private Long getUid() {
        Long uid = userSessionService.getUserAccount().getAccountId();
        return uid;
    }
}