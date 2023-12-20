package com.blissstock.mappingSite.controller;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.blissstock.mappingSite.entity.TestResult;
import com.blissstock.mappingSite.entity.UserAccount;
import com.blissstock.mappingSite.entity.GuestUser;
import com.blissstock.mappingSite.entity.Test;
import com.blissstock.mappingSite.entity.TestQuestion;
import com.blissstock.mappingSite.entity.TestQuestionCorrectAnswer;
import com.blissstock.mappingSite.entity.TestExaminee;
import com.blissstock.mappingSite.entity.TestExamineeAnswer;
import com.blissstock.mappingSite.entity.UserInfo;
import com.blissstock.mappingSite.exceptions.UnauthorizedFileAccessException;
import com.blissstock.mappingSite.model.AnswerModel;
import com.blissstock.mappingSite.model.ChoiceModel;
import com.blissstock.mappingSite.model.FileInfo;
import com.blissstock.mappingSite.model.QuestionAndCorrectAnswer;
import com.blissstock.mappingSite.model.QuestionAndCorrectAnswerAndExamineeAnswer;
import com.blissstock.mappingSite.model.ExamineeChoiceModel;
import com.blissstock.mappingSite.repository.TestResultRepository;
import com.blissstock.mappingSite.repository.TestQuestionCorrectAnswerRepositoy;
import com.blissstock.mappingSite.repository.TestQuestionRepository;
import com.blissstock.mappingSite.repository.TestRepository;
import com.blissstock.mappingSite.repository.GuestUserRepository;
import com.blissstock.mappingSite.repository.TestExamineeAnswerRepository;
import com.blissstock.mappingSite.repository.TestExamineeRepository;
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
import org.springframework.dao.DataAccessException;

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
    TestExamineeAnswerRepository TestExamineeAnswerRepository;

    @Autowired
    UserInfoRepository userInfoRepository;

    @Autowired
    GuestUserRepository guestUserRepository;

    @Autowired
    TestExamineeRepository TestExamineeRepository;

    @Autowired
    TestResultRepository resultRepository;

    @Valid
    @GetMapping(value = { "/teacher/exam/{test_id}/questions", "/admin/exam/{test_id}/questions" })
    private String getQuestionsByTest(@PathVariable Long test_id, Model model)
            throws ParseException, JsonMappingException, JsonProcessingException {
        Long userID = getUid();
        logger.info("Called getQuestionsByTest with parameter(user_id={})", userID);
        List<QuestionAndCorrectAnswer> questionAndCorrectAnswers = new ArrayList<>();
        logger.info("Initiate Operation Retrieve Table test_question by Query: test_id={}", test_id);
        List<TestQuestion> testQuestions = testQuestionRepository.getQuestionByTest(test_id);
        logger.info(
                "Operation Retrieve Table test_question by Query: test_id={}. Result List: testQuestions={} | Success",
                test_id, testQuestions.size());
        for (TestQuestion testQuestion : testQuestions) {
            long fileSeparator = 100000L + test_id;
            FileInfo file = storageService.loadQuestionMaterials(fileSeparator, testQuestion.getQuestion_materials());
            testQuestion.setQuestion_materials(file.getUrl());

            List<ChoiceModel> choices = new ArrayList<>();
            if (!testQuestion.getQuestion_type().equals("FREE_ANSWER")) {
                logger.info("Initiate Operation Retrieve Table test_question_correct_answer by Query: question_id={}",
                        testQuestion.getId());
                TestQuestionCorrectAnswer testQuestionCorrectAnswer = testQuestionCorrectAnswerRepositoy
                        .getCorrectAnswerByQuestion(testQuestion.getId());
                logger.info(
                        "Operation Retrieve Table test_question_correct_answer by Query: question_id={}. Result List: test_question_correct_answer={} | Success",
                        testQuestion.getId(), testQuestionCorrectAnswer);
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
        logger.info("Called getQuestionsByTest with parameter(user_id={}) Success", userID);
        return "AT0007_TestQuestions.html";
    }

    @Valid
    @GetMapping(value = { "/teacher/exam/{test_id}/student/{student_id}",
            "/admin/exam/{test_id}/student/{student_id}" })
    private String getStudentAnswer(@PathVariable Long test_id,
            @PathVariable Long student_id, Model model)
            throws ParseException, JsonMappingException, JsonProcessingException {
        Long userID = getUid();
        logger.info("Called getStudentAnswer with parameter(user_id={})", userID);
        List<QuestionAndCorrectAnswerAndExamineeAnswer> questionAndCorrectAnswers = new ArrayList<>();
        logger.info("Initiate Operation Retrieve Table test_question by Query: test_id={}", test_id);
        List<TestQuestion> testQuestions = testQuestionRepository.getQuestionByTest(test_id);
        logger.info(
                "Operation Retrieve Table test_question by Query: test_id={}. Result List: testQuestions={} | Success",
                test_id, testQuestions.size());
        logger.info("Initiate Operation Retrieve Table test_question by Query: test_id={}", test_id);
        Integer freeAnswerCount = testQuestionRepository.getFreeAnswerCount(test_id);
        logger.info(
                "Operation Retrieve Table test_question by Query: test_id={}. Result List: freeAnswerCount={} | Success",
                test_id, freeAnswerCount);
        logger.info("Initiate Operation Retrieve Table test_student_answer by Query: test_id={}", test_id);
        Integer markingCount = TestExamineeAnswerRepository.getMarkingQuestionCount(test_id);
        logger.info(
                "Operation Retrieve Table test_student_answer by Query: test_id={}. Result List: markingCount={} | Success",
                test_id, markingCount);
        logger.info("Initiate Operation Retrieve Table test by Query: test_id={}", test_id);
        Test test = testRepository.getTestByID(test_id);
        logger.info("Operation Retrieve Table test by Query: test_id={}. Result List: test={} | Success", test_id,
                test);
        logger.info("Initiate Operation Retrieve Table user_info by Query: student_id={}", student_id);
        String userName;
        Long userId;
        if (test.getExam_target() == 1) {
            GuestUser guestUser = guestUserRepository.findByGuestId(student_id);
            userName = guestUser.getName();
            userId = guestUser.getGuest_id();
            logger.info(
                    "Operation Retrieve Table user_info by Query: student_id={}. Result List: userInfo={} | Success",
                    student_id, guestUser);
        } else {
            UserInfo userInfo = userInfoRepository.findStudentById(student_id);
            userName = userInfo.getUserName();
            userId = userInfo.getUid();
            logger.info(
                    "Operation Retrieve Table user_info by Query: student_id={}. Result List: userInfo={} | Success",
                    student_id, userInfo);
        }
        String markedStatus = "";
        for (TestQuestion testQuestion : testQuestions) {
            String studentAnswer = "";
            String studentAnswerURL = "";
            Integer student_answer_id = 0;
            long fileSeparator = 100000L + test_id;
            FileInfo file = storageService.loadQuestionMaterials(fileSeparator, testQuestion.getQuestion_materials());
            testQuestion.setQuestion_materials(file.getUrl());
            int acquired_mark = 0;
            List<ExamineeChoiceModel> choices = new ArrayList<>();
            if (!testQuestion.getQuestion_type().equals("FREE_ANSWER")) {
                TestExamineeAnswer TestExamineeAnswer;
                TestQuestionCorrectAnswer testQuestionCorrectAnswer = testQuestionCorrectAnswerRepositoy
                        .getCorrectAnswerByQuestion(testQuestion.getId());
                if (test.getExam_target() == 1) {
                    TestExamineeAnswer = TestExamineeAnswerRepository.getGuestAnswer(student_id,
                            testQuestion.getId());
                } else {
                    TestExamineeAnswer = TestExamineeAnswerRepository.getStudentAnswer(student_id,
                            testQuestion.getId());
                }

                JSONArray choiceArrary = new JSONArray(testQuestion.getChoices());
                JSONArray answerArray = new JSONArray(testQuestionCorrectAnswer.getCorrectAnswer());
                JSONArray studentchoiceArray = new JSONArray();
                if (TestExamineeAnswer != null) {
                    studentchoiceArray = new JSONArray(TestExamineeAnswer.getExaminee_answer());
                }
                for (int i = 0; i < choiceArrary.length(); i++) {
                    JSONObject choice = choiceArrary.getJSONObject(i);
                    String choiceData = choice.getString("choice");
                    choices.add(new ExamineeChoiceModel(i, choiceData, false, false));
                }

                for (int j = 0; j < answerArray.length(); j++) {
                    JSONObject answer = answerArray.getJSONObject(j);
                    int correct = answer.getInt("answer");
                    String choice = choices.get(correct).getChoice();
                    choices.set(correct, new ExamineeChoiceModel(correct, choice, true, false));
                }

                if (TestExamineeAnswer != null) {
                    for (int k = 0; k < studentchoiceArray.length(); k++) {
                        JSONObject answer = studentchoiceArray.getJSONObject(k);
                        int student_choice = answer.getInt("student_choice");
                        String choice = choices.get(student_choice).getChoice();
                        Boolean correctAnswer = choices.get(student_choice).isCorrect();
                        choices.set(student_choice,
                                new ExamineeChoiceModel(student_choice, choice, correctAnswer, true));
                    }
                }

                boolean allCorrectAnswersMatched = true;

                for (int l = 0; l < choices.size(); l++) {
                    Boolean correctAnswer = choices.get(l).isCorrect();
                    Boolean studentChoice = choices.get(l).isExaminee_choice();

                    if (correctAnswer && !studentChoice) {
                        allCorrectAnswersMatched = false;
                        break;
                    }
                }

                if (allCorrectAnswersMatched) {
                    acquired_mark = testQuestion.getMaximum_mark();
                }
            } else {
                TestExamineeAnswer TestExamineeAnswer;
                if (test.getExam_target() == 1) {
                    TestExamineeAnswer = TestExamineeAnswerRepository.getGuestAnswer(student_id,
                            testQuestion.getId());
                } else {
                    TestExamineeAnswer = TestExamineeAnswerRepository.getStudentAnswer(student_id,
                            testQuestion.getId());
                }
                if (TestExamineeAnswer != null) {
                    acquired_mark = TestExamineeAnswer.getAcquired_mark();
                    long studentfileSeparator = Long.parseLong(test_id.toString()
                            + TestExamineeAnswer.getQuestion().getId().toString() + student_id.toString());
                    studentAnswer = TestExamineeAnswer.getExaminee_answer();
                    FileInfo studentAnswerFile = storageService.loadAnswermaterials(studentfileSeparator,
                            TestExamineeAnswer.getExaminee_answer_link());
                    studentAnswerURL = studentAnswerFile.getUrl();
                    student_answer_id = Integer.parseInt(TestExamineeAnswer.getId().toString());
                    markedStatus = TestExamineeAnswer.getMarked_status();
                }
            }
            QuestionAndCorrectAnswerAndExamineeAnswer studentAnswerList = new QuestionAndCorrectAnswerAndExamineeAnswer(
                    testQuestion.getId(),
                    student_answer_id,
                    studentAnswer, studentAnswerURL,
                    testQuestion.getQuestion_text(), testQuestion.getQuestion_materials(),
                    testQuestion.getQuestion_materials_type(), choices,
                    testQuestion.getQuestion_type(), testQuestion.getMaximum_mark(),
                    acquired_mark, markedStatus);
            questionAndCorrectAnswers.add(studentAnswerList);

        }
        try {
            TestResult viewExamResult = resultRepository.getResultByTestIdAndUser(test_id, student_id);
            if (viewExamResult != null) {
                model.addAttribute("comment", viewExamResult.getTeacherComment());

            }
        } catch (DataAccessException ex) {

        }
        model.addAttribute("test_id", test_id);
        model.addAttribute("exam_target", test.getExam_target());
        model.addAttribute("questionList", questionAndCorrectAnswers);
        model.addAttribute("test_date", test.getDate());
        model.addAttribute("name", userName);
        model.addAttribute("userId", userId);
        model.addAttribute("totalTest", testQuestions.size());
        model.addAttribute("freeTest", freeAnswerCount);
        model.addAttribute("choiceTest", testQuestions.size() - freeAnswerCount);
        model.addAttribute("user_role", userSessionService.getRole());
        if (markingCount == 0) {
            model.addAttribute("status", "Completed");
        } else {
            model.addAttribute("status", "Marking");
        }
        logger.info("Called getStudentAnswer with parameter(user_id={})", userID);
        return "AT0006_ExamineeAnswerList.html";
    }

    @Valid
    @GetMapping(value = { "/admin/exam/{test_id}/guest/{guest_id}" })
    private String getGuestAnswer(@PathVariable Long test_id, @PathVariable Long guest_id, Model model)
            throws ParseException, JsonMappingException, JsonProcessingException {
        Long userID = getUid();
        logger.info("Called getGuestAnswer with parameter(user_id={})", userID);
        List<QuestionAndCorrectAnswerAndExamineeAnswer> questionAndCorrectAnswers = new ArrayList<>();
        logger.info("Initiate Operation Retrieve Table test_question by Query: test_id={}", test_id);
        List<TestQuestion> testQuestions = testQuestionRepository.getQuestionByTest(test_id);
        logger.info(
                "Operation Retrieve Table test_question by Query: test_id={}. Result List: testQuestions={} | Success",
                test_id, testQuestions.size());
        logger.info("Initiate Operation Retrieve Table test_question by Query: test_id={}", test_id);
        Integer freeAnswerCount = testQuestionRepository.getFreeAnswerCount(test_id);
        logger.info(
                "Operation Retrieve Table test_question by Query: test_id={}. Result List: freeAnswerCount={} | Success",
                test_id, freeAnswerCount);
        logger.info("Initiate Operation Retrieve Table test_examinee_answer by Query: test_id={}", test_id);
        Integer markingCount = TestExamineeAnswerRepository.getMarkingQuestionCount(test_id);
        logger.info(
                "Operation Retrieve Table test_examinee_answer by Query: test_id={}. Result List: markingCount={} | Success",
                test_id, markingCount);
        logger.info("Initiate Operation Retrieve Table test by Query: test_id={}", test_id);
        Test test = testRepository.getTestByID(test_id);
        logger.info("Operation Retrieve Table test by Query: test_id={}. Result List: test={} | Success", test_id,
                test);
        logger.info("Initiate Operation Retrieve Table guest by Query: guest_id={}", guest_id);
        GuestUser guestUser = guestUserRepository.findByGuestId(guest_id);
        logger.info("Operation Retrieve Table guest by Query: guest_id={}. Result List: guestUser={} | Success",
                guest_id, guestUser);
        String markedStatus = "";

        for (TestQuestion testQuestion : testQuestions) {
            String guestAnswer = "";
            String guestAnswerURL = "";
            Integer guest_answer_id = 0;
            long fileSeparator = 100000L + test_id;
            FileInfo file = storageService.loadQuestionMaterials(fileSeparator, testQuestion.getQuestion_materials());
            testQuestion.setQuestion_materials(file.getUrl());
            int acquired_mark = 0;
            List<ExamineeChoiceModel> choices = new ArrayList<>();
            if (!testQuestion.getQuestion_type().equals("FREE_ANSWER")) {
                TestQuestionCorrectAnswer testQuestionCorrectAnswer = testQuestionCorrectAnswerRepositoy
                        .getCorrectAnswerByQuestion(testQuestion.getId());
                TestExamineeAnswer TestExamineeAnswer = TestExamineeAnswerRepository.getGuestAnswer(guest_id,
                        testQuestion.getId());

                JSONArray choiceArrary = new JSONArray(testQuestion.getChoices());
                JSONArray answerArray = new JSONArray(testQuestionCorrectAnswer.getCorrectAnswer());
                JSONArray guestchoiceArray = new JSONArray();
                if (TestExamineeAnswer != null) {
                    guestchoiceArray = new JSONArray(TestExamineeAnswer.getExaminee_answer());
                }
                for (int i = 0; i < choiceArrary.length(); i++) {
                    JSONObject choice = choiceArrary.getJSONObject(i);
                    String choiceData = choice.getString("choice");
                    choices.add(new ExamineeChoiceModel(i, choiceData, false, false));
                }

                for (int j = 0; j < answerArray.length(); j++) {
                    JSONObject answer = answerArray.getJSONObject(j);
                    int correct = answer.getInt("answer");
                    String choice = choices.get(correct).getChoice();
                    choices.set(correct, new ExamineeChoiceModel(correct, choice, true, false));
                }

                if (TestExamineeAnswer != null) {
                    for (int k = 0; k < guestchoiceArray.length(); k++) {
                        JSONObject answer = guestchoiceArray.getJSONObject(k);
                        int guest_choice = answer.getInt("student_choice");
                        String choice = choices.get(guest_choice).getChoice();
                        Boolean correctAnswer = choices.get(guest_choice).isCorrect();
                        choices.set(guest_choice,
                                new ExamineeChoiceModel(guest_choice, choice, correctAnswer, true));
                    }
                }

                boolean allCorrectAnswersMatched = true;

                for (int l = 0; l < choices.size(); l++) {
                    Boolean correctAnswer = choices.get(l).isCorrect();
                    Boolean guestChoice = choices.get(l).isExaminee_choice();

                    if (correctAnswer && !guestChoice) {
                        allCorrectAnswersMatched = false;
                        break;
                    }
                }

                if (allCorrectAnswersMatched) {
                    acquired_mark = testQuestion.getMaximum_mark();
                }
            } else {
                TestExamineeAnswer TestExamineeAnswer = TestExamineeAnswerRepository.getGuestAnswer(guest_id,
                        testQuestion.getId());
                if (TestExamineeAnswer != null) {
                    acquired_mark = TestExamineeAnswer.getAcquired_mark();
                    long guestfileSeparator = Long.parseLong(test_id.toString()
                            + TestExamineeAnswer.getQuestion().getId().toString() + guest_id.toString());
                    guestAnswer = TestExamineeAnswer.getExaminee_answer();
                    FileInfo guestAnswerFile = storageService.loadAnswermaterials(guestfileSeparator,
                            TestExamineeAnswer.getExaminee_answer_link());
                    guestAnswerURL = guestAnswerFile.getUrl();
                    guest_answer_id = Integer.parseInt(TestExamineeAnswer.getId().toString());
                    markedStatus = TestExamineeAnswer.getMarked_status();
                }
            }
            QuestionAndCorrectAnswerAndExamineeAnswer guestAnswerList = new QuestionAndCorrectAnswerAndExamineeAnswer(
                    testQuestion.getId(),
                    guest_answer_id,
                    guestAnswer, guestAnswerURL,
                    testQuestion.getQuestion_text(), testQuestion.getQuestion_materials(),
                    testQuestion.getQuestion_materials_type(), choices,
                    testQuestion.getQuestion_type(), testQuestion.getMaximum_mark(),
                    acquired_mark, markedStatus);
            questionAndCorrectAnswers.add(guestAnswerList);

        }
        try {
            TestResult viewExamResult = resultRepository.getResultByTestIdAndGuestUser(test_id, guest_id);
            if (viewExamResult != null) {
                model.addAttribute("comment", viewExamResult.getTeacherComment());

            }
        } catch (DataAccessException ex) {

        }
        model.addAttribute("test_id", test_id);
        model.addAttribute("exam_target", test.getExam_target());
        model.addAttribute("questionList", questionAndCorrectAnswers);
        model.addAttribute("test_date", test.getDate());
        model.addAttribute("name", guestUser.getName());
        model.addAttribute("guestId", guestUser.getGuest_id());
        model.addAttribute("totalTest", testQuestions.size());
        model.addAttribute("freeTest", freeAnswerCount);
        model.addAttribute("choiceTest", testQuestions.size() - freeAnswerCount);
        model.addAttribute("user_role", userSessionService.getRole());
        if (markingCount == 0) {
            model.addAttribute("status", "Completed");
        } else {
            model.addAttribute("status", "Marking");
        }
        logger.info("Called getGuestAnswer with parameter(user_id={})", userID);
        return "AT0006_ExamineeAnswerList.html";
    }

    @Valid
    @GetMapping(value = { "/student/exam/{test_id}/questions" })
    private String getStudentQuestions(@PathVariable Long test_id, Model model)
            throws ParseException, JsonMappingException, JsonProcessingException {
        Long userID = getUid();
        logger.info("Called getStudentQuestions with parameter(user_id={})", userID);
        logger.info("Initiate Operation Retrieve Table test by Query: test_id={}", test_id);
        Test testinfo = testRepository.getTestByID(test_id);
        logger.info("Operation Retrieve Table test by Query: test_id={}. Result List: testinfo={} | Success", test_id,
                testinfo);
        Date examDate = testinfo.getDate();
        LocalDate convertedExamDate = examDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate currentDate = LocalDate.now();
        Long studentId = userSessionService.getId();

        logger.info("Initiate Operation Retrieve Table test_student by Query: test_id={}, studentId={}", test_id,
                studentId);
        TestExaminee studentInfo = TestExamineeRepository.findByTestIdAndUid(test_id, studentId);
        logger.info(
                "Operation Retrieve Table test_student by Query: test_id={}, studentId={}. Result List: studentInfo={} | Success",
                test_id, studentId, studentInfo);

        logger.info("Initiate Operation Retrieve Table test_student_answer by Query: studentId={}, test_id={}",
                studentId, test_id);
        TestExamineeAnswer studentAnswerInfo = TestExamineeAnswerRepository.getStudentAnswerByTestAndStudent(studentId,
                test_id);
        logger.info(
                "Operation Retrieve Table test_student_answer by Query: studentId={}, test_id={}. Result List: studentAnswerInfo={} | Success",
                studentId, test_id, studentAnswerInfo);

        String studentExamStartTime = studentInfo.getStudentExamStartTime();
        String examTitle = testinfo.getSection_name();

        DateTimeFormatter examTimeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        String examStartTimeString = testinfo.getStart_time();
        LocalTime examStartTime = LocalTime.parse(examStartTimeString, examTimeFormatter);
        LocalTime currentTime = LocalTime.now().withSecond(0).withNano(0);
        String examEndTimeString = testinfo.getEnd_time();
        LocalTime examEndTime = LocalTime.parse(examEndTimeString, examTimeFormatter);

        LocalTime examStartTimeFinal = examStartTime.plusMinutes(30);

        String examAnnouncement = null;

        if (studentAnswerInfo != null) {
            examAnnouncement = "Apologies! Exam answer is already submitted. Examinees are not allowed to submit the answer more than once! ";
            model.addAttribute("exam_announce", examAnnouncement);
            return "ST0006_ExamQuestionListStudent.html";
        }

        if (currentDate.isBefore(convertedExamDate)) {
            examAnnouncement = "Apologies! Exam is not currently available yet. Please note that the exam will be accessible on "
                    + convertedExamDate + " " + examStartTime + " (Japan Standard Time, JST).";
        } else if (currentDate.isEqual(convertedExamDate)) {

            if (currentTime.isBefore(examStartTime)) {
                examAnnouncement = "Apologies! Exam is not currently available yet. Please note that the exam will be accessible on "
                        + convertedExamDate + " " + examStartTime + " (Japan Standard Time, JST).";
            } else if (currentTime.equals(examStartTime) || currentTime.isBefore(examStartTimeFinal)
                    || currentTime.equals(examStartTimeFinal)) {

                if (studentExamStartTime == null || studentExamStartTime.isEmpty()) {

                    studentExamStartTime = currentTime.toString();
                    studentInfo.setStudentExamStartTime(studentExamStartTime);
                    logger.info("Initiate to Operation Insert Table Test Student Data {}", studentInfo.display());
                    TestExamineeRepository.save(studentInfo);
                    logger.info("Operation Insert Table Test Student Data {} | Success", studentInfo.display());

                    List<QuestionAndCorrectAnswer> questionAndCorrectAnswers = new ArrayList<>();
                    Test test = testRepository.getTestByID(test_id);
                    List<TestQuestion> testQuestions = testQuestionRepository.getQuestionByTest(test_id);
                    for (TestQuestion testQuestion : testQuestions) {
                        long fileSeparator = 100000L + test_id;
                        FileInfo file = storageService.loadQuestionMaterials(fileSeparator,
                                testQuestion.getQuestion_materials());
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

                        QuestionAndCorrectAnswer questionAndCorrectAnswer = new QuestionAndCorrectAnswer(
                                testQuestion.getId(),
                                testQuestion.getQuestion_text(), testQuestion.getQuestion_materials(),
                                testQuestion.getQuestion_materials_type(), choices,
                                testQuestion.getQuestion_type(), testQuestion.getMaximum_mark());
                        questionAndCorrectAnswers.add(questionAndCorrectAnswer);
                    }
                    model.addAttribute("test_date", test.getDate());
                    model.addAttribute("test_start_time", test.getStart_time());
                    model.addAttribute("test_end_time", test.getEnd_time());
                    model.addAttribute("test_id", test_id);
                    model.addAttribute("exam_announce", examAnnouncement);
                    model.addAttribute("exam_start_time", currentTime);
                    model.addAttribute("exam_end_time", examEndTime);
                    model.addAttribute("exam_title", examTitle);
                    model.addAttribute("questionList", questionAndCorrectAnswers);
                    logger.info("Called getStudentQuestions with parameter(user_id={}) Success", userID);
                    return "ST0006_ExamQuestionListStudent.html";
                } else if (studentExamStartTime != null) {

                    List<QuestionAndCorrectAnswer> questionAndCorrectAnswers = new ArrayList<>();

                    logger.info("Initiate Operation Retrieve Table test by Query: test_id={}", test_id);
                    Test test = testRepository.getTestByID(test_id);
                    logger.info("Operation Retrieve Table test by Query: test_id={}. Result List: test={} | Success",
                            test_id, test);

                    logger.info("Initiate Operation Retrieve Table test_question by Query: test_id={}", test_id);
                    List<TestQuestion> testQuestions = testQuestionRepository.getQuestionByTest(test_id);
                    logger.info(
                            "Operation Retrieve Table test_question by Query: test_id={}. Result List: testQuestions={} | Success",
                            test_id, testQuestions.size());

                    for (TestQuestion testQuestion : testQuestions) {
                        long fileSeparator = 100000L + test_id;
                        FileInfo file = storageService.loadQuestionMaterials(fileSeparator,
                                testQuestion.getQuestion_materials());
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

                        QuestionAndCorrectAnswer questionAndCorrectAnswer = new QuestionAndCorrectAnswer(
                                testQuestion.getId(),
                                testQuestion.getQuestion_text(), testQuestion.getQuestion_materials(),
                                testQuestion.getQuestion_materials_type(), choices,
                                testQuestion.getQuestion_type(), testQuestion.getMaximum_mark());
                        questionAndCorrectAnswers.add(questionAndCorrectAnswer);
                    }
                    model.addAttribute("test_date", test.getDate());
                    model.addAttribute("test_start_time", test.getStart_time());
                    model.addAttribute("test_end_time", test.getEnd_time());
                    model.addAttribute("test_id", test_id);
                    model.addAttribute("exam_announce", examAnnouncement);
                    model.addAttribute("exam_start_time", currentTime);
                    model.addAttribute("exam_end_time", examEndTime);
                    model.addAttribute("exam_title", examTitle);
                    model.addAttribute("questionList", questionAndCorrectAnswers);
                    logger.info("Called getStudentQuestions with parameter(user_id={}) Success", userID);
                    return "ST0006_ExamQuestionListStudent.html";
                }

            } else if (currentTime.isAfter(examStartTimeFinal) && currentTime.isBefore(examEndTime)) {
                if (studentExamStartTime == null || studentExamStartTime.isEmpty()) {
                    examAnnouncement = "Apologies! The exam is currently in progress. Late examinees are not allowed to take the exam.";
                } else if (studentExamStartTime != null) {
                    List<QuestionAndCorrectAnswer> questionAndCorrectAnswers = new ArrayList<>();
                    Test test = testRepository.getTestByID(test_id);
                    List<TestQuestion> testQuestions = testQuestionRepository.getQuestionByTest(test_id);
                    for (TestQuestion testQuestion : testQuestions) {
                        long fileSeparator = 100000L + test_id;
                        FileInfo file = storageService.loadQuestionMaterials(fileSeparator,
                                testQuestion.getQuestion_materials());
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

                        QuestionAndCorrectAnswer questionAndCorrectAnswer = new QuestionAndCorrectAnswer(
                                testQuestion.getId(),
                                testQuestion.getQuestion_text(), testQuestion.getQuestion_materials(),
                                testQuestion.getQuestion_materials_type(), choices,
                                testQuestion.getQuestion_type(), testQuestion.getMaximum_mark());
                        questionAndCorrectAnswers.add(questionAndCorrectAnswer);
                    }
                    model.addAttribute("test_date", test.getDate());
                    model.addAttribute("test_start_time", test.getStart_time());
                    model.addAttribute("test_end_time", test.getEnd_time());
                    model.addAttribute("test_id", test_id);
                    model.addAttribute("exam_announce", examAnnouncement);
                    model.addAttribute("exam_start_time", currentTime);
                    model.addAttribute("exam_end_time", examEndTime);
                    model.addAttribute("exam_title", examTitle);
                    model.addAttribute("questionList", questionAndCorrectAnswers);
                    logger.info("Called getStudentQuestions with parameter(user_id={}) Success", userID);
                    return "ST0006_ExamQuestionListStudent.html";
                }
            }

            else if (currentTime.isAfter(examEndTime) || currentTime.equals(examEndTime)) {
                examAnnouncement = "Apologies! This exam has already been conducted. It was held on "
                        + convertedExamDate
                        + " " + examStartTime + " (Japan Standard Time, JST).";

            }

        } else if (currentDate.isAfter(convertedExamDate)) {
            examAnnouncement = "Apologies! This exam has already been conducted. It was held on " + convertedExamDate
                    + " " + examStartTime + " (Japan Standard Time, JST).";
        }

        // List<QuestionAndCorrectAnswer> questionAndCorrectAnswers = new ArrayList<>();
        // Test test = testRepository.getTestByID(test_id);
        // List<TestQuestion> testQuestions =
        // testQuestionRepository.getQuestionByTest(test_id);
        // for (TestQuestion testQuestion : testQuestions) {
        // long fileSeparator = 100000L + test_id;
        // FileInfo file = storageService.loadQuestionMaterials(fileSeparator,
        // testQuestion.getQuestion_materials());
        // testQuestion.setQuestion_materials(file.getUrl());

        // List<ChoiceModel> choices = new ArrayList<>();
        // if (!testQuestion.getQuestion_type().equals("FREE_ANSWER")) {
        // TestQuestionCorrectAnswer testQuestionCorrectAnswer =
        // testQuestionCorrectAnswerRepositoy
        // .getCorrectAnswerByQuestion(testQuestion.getId());
        // JSONArray choiceArrary = new JSONArray(testQuestion.getChoices());
        // JSONArray answerArray = new
        // JSONArray(testQuestionCorrectAnswer.getCorrectAnswer());
        // for (int i = 0; i < choiceArrary.length(); i++) {
        // JSONObject choice = choiceArrary.getJSONObject(i);
        // String choiceData = choice.getString("choice");
        // choices.add(new ChoiceModel(i, choiceData, false));
        // }

        // for (int j = 0; j < answerArray.length(); j++) {
        // JSONObject answer = answerArray.getJSONObject(j);
        // int correct = answer.getInt("answer");
        // String choice = choices.get(correct).getChoice();
        // choices.set(correct, new ChoiceModel(correct, choice, true));
        // }
        // }

        // QuestionAndCorrectAnswer questionAndCorrectAnswer = new
        // QuestionAndCorrectAnswer(testQuestion.getId(),
        // testQuestion.getQuestion_text(), testQuestion.getQuestion_materials(),
        // testQuestion.getQuestion_materials_type(), choices,
        // testQuestion.getQuestion_type(), testQuestion.getMaximum_mark());
        // questionAndCorrectAnswers.add(questionAndCorrectAnswer);
        // }
        // model.addAttribute("test_date", test.getDate());
        // model.addAttribute("test_start_time", test.getStart_time());
        // model.addAttribute("test_end_time", test.getEnd_time());
        // model.addAttribute("test_id", test_id);
        // model.addAttribute("questionList", questionAndCorrectAnswers);

        model.addAttribute("exam_announce", examAnnouncement);
        logger.info("Called getStudentQuestions with parameter(user_id={}) Success", userID);
        return "ST0006_ExamQuestionListStudent.html";
    }

    @Valid
    @GetMapping(value = { "/guest-exam/{test_id}/questions" })
    private String getGuestUserQuestions(@PathVariable Long test_id, Model model)
            throws ParseException, JsonMappingException, JsonProcessingException {
        Long guestUserID = getGuestUserID();
        logger.info("Called getGuestQuestions with parameter(user_id={})", guestUserID);
        logger.info("Initiate Operation Retrieve Table test by Query: test_id={}", test_id);
        Test target = testRepository.getTargetByID(test_id);

        if (target.getExam_target() == 1) {
            Test testinfo = testRepository.getTestByID(test_id);
            logger.info("Operation Retrieve Table test by Query: test_id={}. Result List: testinfo={} | Success",
                    test_id,
                    testinfo);
            Date examDate = testinfo.getDate();
            LocalDate convertedExamDate = examDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate currentDate = LocalDate.now();

            logger.info("Initiate Operation Retrieve Table test_student by Query: test_id={}, guestuserId={}", test_id,
                    guestUserID);
            TestExaminee studentInfo = TestExamineeRepository.findByTestIdAndUidGuest(test_id, guestUserID);
            logger.info(
                    "Operation Retrieve Table TestExaminee by Query: test_id={}, guestuserId={}. Result List: guestuserInfo={} | Success",
                    test_id, guestUserID, studentInfo);

            logger.info("Initiate Operation Retrieve Table test_student_answer by Query: guestuserId={}, test_id={}",
                    guestUserID, test_id);
            TestExamineeAnswer guestAnswerInfo = TestExamineeAnswerRepository.getGuestAnswerByTestAndStudent(
                    guestUserID,
                    test_id);
            logger.info(
                    "Operation Retrieve Table test_student_answer by Query: guestUserId={}, test_id={}. Result List:guestUserAnswerInfo={} | Success",
                    guestUserID, test_id, guestAnswerInfo);

            String studentExamStartTime = studentInfo.getStudentExamStartTime();
            String examTitle = testinfo.getSection_name();

            DateTimeFormatter examTimeFormatter = DateTimeFormatter.ofPattern("HH:mm");

            String examStartTimeString = testinfo.getStart_time();
            LocalTime examStartTime = LocalTime.parse(examStartTimeString, examTimeFormatter);
            LocalTime currentTime = LocalTime.now().withSecond(0).withNano(0);
            String examEndTimeString = testinfo.getEnd_time();
            LocalTime examEndTime = LocalTime.parse(examEndTimeString, examTimeFormatter);

            LocalTime examStartTimeFinal = examStartTime.plusMinutes(30);

            String examAnnouncement = null;

            if (guestAnswerInfo != null) {
                examAnnouncement = "Apologies! Exam answer is already submitted. Examinees are not allowed to submit the answer more than once! ";
                model.addAttribute("exam_announce", examAnnouncement);
                return "GU0002_GuestUser.html";
            }

            if (currentDate.isBefore(convertedExamDate)) {
                examAnnouncement = "Apologies! Exam is not currently available yet. Please note that the exam will be accessible on "
                        + convertedExamDate + " " + examStartTime + " (Japan Standard Time, JST).";
            } else if (currentDate.isEqual(convertedExamDate)) {

                if (currentTime.isBefore(examStartTime)) {
                    examAnnouncement = "Apologies! Exam is not currently available yet. Please note that the exam will be accessible on "
                            + convertedExamDate + " " + examStartTime + " (Japan Standard Time, JST).";
                } else if (currentTime.equals(examStartTime) || currentTime.isBefore(examStartTimeFinal)
                        || currentTime.equals(examStartTimeFinal)) {

                    if (studentExamStartTime == null || studentExamStartTime.isEmpty()) {

                        studentExamStartTime = currentTime.toString();
                        studentInfo.setStudentExamStartTime(studentExamStartTime);
                        logger.info("Initiate to Operation Insert Table Test Guest User Data {}");
                        TestExamineeRepository.save(studentInfo);
                        logger.info("Operation Insert Table Test GuestUser Data {} | Success");

                        List<QuestionAndCorrectAnswer> questionAndCorrectAnswers = new ArrayList<>();
                        Test test = testRepository.getTestByID(test_id);
                        List<TestQuestion> testQuestions = testQuestionRepository.getQuestionByTest(test_id);
                        for (TestQuestion testQuestion : testQuestions) {
                            long fileSeparator = 100000L + test_id;
                            FileInfo file = storageService.loadQuestionMaterials(fileSeparator,
                                    testQuestion.getQuestion_materials());
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

                            QuestionAndCorrectAnswer questionAndCorrectAnswer = new QuestionAndCorrectAnswer(
                                    testQuestion.getId(),
                                    testQuestion.getQuestion_text(), testQuestion.getQuestion_materials(),
                                    testQuestion.getQuestion_materials_type(), choices,
                                    testQuestion.getQuestion_type(), testQuestion.getMaximum_mark());
                            questionAndCorrectAnswers.add(questionAndCorrectAnswer);
                        }
                        model.addAttribute("test_date", test.getDate());
                        model.addAttribute("test_start_time", test.getStart_time());
                        model.addAttribute("test_end_time", test.getEnd_time());
                        model.addAttribute("test_id", test_id);
                        model.addAttribute("exam_announce", examAnnouncement);
                        model.addAttribute("exam_start_time", currentTime);
                        model.addAttribute("exam_end_time", examEndTime);
                        model.addAttribute("exam_title", examTitle);
                        model.addAttribute("questionList", questionAndCorrectAnswers);
                        logger.info("Called getGuestUserQuestions with parameter(user_id={}) Success", guestUserID);
                        return "GU0002_GuestUser.html";
                    } else if (studentExamStartTime != null) {

                        List<QuestionAndCorrectAnswer> questionAndCorrectAnswers = new ArrayList<>();

                        logger.info("Initiate Operation Retrieve Table test by Query: test_id={}", test_id);
                        Test test = testRepository.getTestByID(test_id);
                        logger.info(
                                "Operation Retrieve Table test by Query: test_id={}. Result List: test={} | Success",
                                test_id, test);

                        logger.info("Initiate Operation Retrieve Table test_question by Query: test_id={}", test_id);
                        List<TestQuestion> testQuestions = testQuestionRepository.getQuestionByTest(test_id);
                        logger.info(
                                "Operation Retrieve Table test_question by Query: test_id={}. Result List: testQuestions={} | Success",
                                test_id, testQuestions.size());

                        for (TestQuestion testQuestion : testQuestions) {
                            long fileSeparator = 100000L + test_id;
                            FileInfo file = storageService.loadQuestionMaterials(fileSeparator,
                                    testQuestion.getQuestion_materials());
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

                            QuestionAndCorrectAnswer questionAndCorrectAnswer = new QuestionAndCorrectAnswer(
                                    testQuestion.getId(),
                                    testQuestion.getQuestion_text(), testQuestion.getQuestion_materials(),
                                    testQuestion.getQuestion_materials_type(), choices,
                                    testQuestion.getQuestion_type(), testQuestion.getMaximum_mark());
                            questionAndCorrectAnswers.add(questionAndCorrectAnswer);
                        }
                        model.addAttribute("test_date", test.getDate());
                        model.addAttribute("test_start_time", test.getStart_time());
                        model.addAttribute("test_end_time", test.getEnd_time());
                        model.addAttribute("test_id", test_id);
                        model.addAttribute("exam_announce", examAnnouncement);
                        model.addAttribute("exam_start_time", currentTime);
                        model.addAttribute("exam_end_time", examEndTime);
                        model.addAttribute("exam_title", examTitle);
                        model.addAttribute("questionList", questionAndCorrectAnswers);
                        logger.info("Called getGuestUserQuestions with parameter(user_id={}) Success", guestUserID);
                        return "GU0002_GuestUser.html";
                    }

                } else if (currentTime.isAfter(examStartTimeFinal) && currentTime.isBefore(examEndTime)) {
                    if (studentExamStartTime == null || studentExamStartTime.isEmpty()) {
                        examAnnouncement = "Apologies! The exam is currently in progress. Late examinees are not allowed to take the exam.";
                    } else if (studentExamStartTime != null) {
                        List<QuestionAndCorrectAnswer> questionAndCorrectAnswers = new ArrayList<>();
                        Test test = testRepository.getTestByID(test_id);
                        List<TestQuestion> testQuestions = testQuestionRepository.getQuestionByTest(test_id);
                        for (TestQuestion testQuestion : testQuestions) {
                            long fileSeparator = 100000L + test_id;
                            FileInfo file = storageService.loadQuestionMaterials(fileSeparator,
                                    testQuestion.getQuestion_materials());
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

                            QuestionAndCorrectAnswer questionAndCorrectAnswer = new QuestionAndCorrectAnswer(
                                    testQuestion.getId(),
                                    testQuestion.getQuestion_text(), testQuestion.getQuestion_materials(),
                                    testQuestion.getQuestion_materials_type(), choices,
                                    testQuestion.getQuestion_type(), testQuestion.getMaximum_mark());
                            questionAndCorrectAnswers.add(questionAndCorrectAnswer);
                        }
                        model.addAttribute("test_date", test.getDate());
                        model.addAttribute("test_start_time", test.getStart_time());
                        model.addAttribute("test_end_time", test.getEnd_time());
                        model.addAttribute("test_id", test_id);
                        model.addAttribute("exam_announce", examAnnouncement);
                        model.addAttribute("exam_start_time", currentTime);
                        model.addAttribute("exam_end_time", examEndTime);
                        model.addAttribute("exam_title", examTitle);
                        model.addAttribute("questionList", questionAndCorrectAnswers);
                        logger.info("Called getGuestUserQuestions with parameter(user_id={}) Success", guestUserID);
                        return "GU0002_GuestUser.html";
                    }
                }

                else if (currentTime.isAfter(examEndTime) || currentTime.equals(examEndTime)) {
                    examAnnouncement = "Apologies! This exam has already been conducted. It was held on "
                            + convertedExamDate
                            + " " + examStartTime + " (Japan Standard Time, JST).";

                }

            } else if (currentDate.isAfter(convertedExamDate)) {
                examAnnouncement = "Apologies! This exam has already been conducted. It was held on "
                        + convertedExamDate
                        + " " + examStartTime + " (Japan Standard Time, JST).";
            }

            model.addAttribute("exam_announce", examAnnouncement);
            logger.info("Called getGuestUserQuestions with parameter(user_id={}) Success", guestUserID);
            return "GU0002_GuestUser.html";
        } else {
            return "Your taregt_exam ID is wrong";
        }

    }

    @Valid
    @PostMapping(value = { "/teacher/comment", "/admin/comment" })
    private ResponseEntity saveComment(@RequestBody String payLoad) {
        try {
            Long userID = getUid();
            logger.info("Called saveComment with parameter(payLoad={}, userID={})", payLoad, userID);
            JSONObject jsonObject = new JSONObject(payLoad);
            Long testId = jsonObject.getLong("test_Id");
            Long studentId = null;
            Long guestId = null;
            if(jsonObject.has("student_Id")){
                studentId = jsonObject.getLong("student_Id");
            }
            if(jsonObject.has("guest_Id")){
                guestId = jsonObject.getLong("guest_Id");
            }
            String comment = jsonObject.getString("comment");
            TestResult result = new TestResult();

            logger.info("Initiate Operation Retrieve Table test by Query: testId={}", testId);
            Test test = testRepository.getTestByID(testId);
            logger.info("Operation Retrieve Table test by Query: testId={}. Result List: test={} | Success", testId,
                    test);

            if (studentId != null) {
                logger.info("Initiate Operation Retrieve Table user_info by Query: studentId={}", studentId);
                UserInfo userInfo = userInfoRepository.findStudentById(studentId);
                logger.info(
                        "Operation Retrieve Table user_info by Query: studentId={}. Result List: userInfo={} | Success",
                        studentId, userInfo);

                logger.info("Initiate Operation Retrieve Table result by Query: testId={}, studentId={}", testId,
                        studentId);
                TestResult viewCommentResult = resultRepository.getResultByTestIdAndUser(testId, studentId);
                logger.info(
                        "Operation Retrieve Table result by Query: testId={}, studentId={}. Result List: viewCommentResult={} | Success",
                        testId, studentId, viewCommentResult);

                if (viewCommentResult == null) {

                    result.setTest(test);
                    result.setUser(userInfo);
                    result.setTeacherComment(comment);

                    logger.info("Initiate to Operation Insert Table Result Data {}", result.display());
                    resultRepository.save(result);
                    logger.info("Operation Insert Table Result Data {} | Success", result.display());

                } else {
                    viewCommentResult.setTeacherComment(comment);

                    logger.info("Initiate to Operation Insert Table Result Data {}", viewCommentResult.display());
                    resultRepository.save(viewCommentResult);
                    logger.info("Operation Insert Table Result Data {} | Success", viewCommentResult.display());
                }
            }
            logger.info("Initiate Operation Retrieve Table guest by Query: guestId={}", guestId);
            GuestUser guestUser = guestUserRepository.findByGuestId(guestId);
            logger.info("Operation Retrieve Table guest by Query: guestId={}. Result List: guestUser={} | Success",
                    guestId, guestUser);

            logger.info("Initiate Operation Retrieve Table result by Query: testId={}, guestId={}", testId,
                    guestId);
            TestResult viewCommentResult = resultRepository.getResultByTestIdAndGuestUser(testId, guestId);
            logger.info(
                    "Operation Retrieve Table result by Query: testId={}, guestId={}. Result List: viewCommentResult={} | Success",
                    testId, guestId, viewCommentResult);

            if (viewCommentResult == null) {

                result.setTest(test);
                result.setGuestUser(guestUser);
                result.setTeacherComment(comment);

                logger.info("Initiate to Operation Insert Table Result Data {}", result.display());
                resultRepository.save(result);
                logger.info("Operation Insert Table Result Data {} | Success", result.display());

            } else {
                viewCommentResult.setTeacherComment(comment);

                logger.info("Initiate to Operation Insert Table Result Data {}", viewCommentResult.display());
                resultRepository.save(viewCommentResult);
                logger.info("Operation Insert Table Result Data {} | Success", viewCommentResult.display());
            }
            logger.info("Called saveComment with parameter(payLoad={}, userID={}) Success", payLoad, userID);
            return ResponseEntity.ok(HttpStatus.OK);

        } catch (Exception e) {
            logger.error(e.getLocalizedMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
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
        logger.info(
                "Called createQuestionByTestID with parameter(testID={}, question_text={}, question_materials={}, choices={}, answers={}, questions_type={}, maximum_mark={}, userID={},)",
                testID, question_text, question_materials, choices, answers, questions_type, maximum_mark, userID);

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
            } else if (fileType.equals("mp4")) {
                fileType = "VIDEO";
            } else {
                fileType = "BLANK";
            }
        }

        logger.info("Initiate Operation Retrieve Table test by Query: testID={}", testID);
        Test test = testRepository.getTestByID(testID);
        logger.info("Operation Retrieve Table test by Query: testID={}. Result List: test={} | Success", testID, test);

        TestQuestion testQuestion = new TestQuestion(null, test, question_text, originalFileName, fileType, choices,
                questions_type, maximum_mark);

        logger.info("Initiate to Operation Insert Table Test Question Data {}", testQuestion.display());
        TestQuestion result = testQuestionRepository.save(testQuestion);
        logger.info("Operation Insert Table Test Question Data {} | Success", testQuestion.display());

        TestQuestionCorrectAnswer correctAnswer = new TestQuestionCorrectAnswer(null, result, answers);

        logger.info("Initiate to Operation Insert Table Test Question Correct Answer Data {}", correctAnswer.display());
        testQuestionCorrectAnswerRepositoy.save(correctAnswer);
        logger.info("Operation Insert Table Test Question Correct Answer Data {} | Success", correctAnswer.display());
        logger.info(
                "Called createQuestionByTestID with parameter(testID={}, question_text={}, question_materials={}, choices={}, answers={}, questions_type={}, maximum_mark={}, userID={},) Success",
                testID, question_text, question_materials, choices, answers, questions_type, maximum_mark, userID);
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
        logger.info(
                "Called editQuestionByTestID with parameter(testID={}, questionID={}, question_text={}, question_materials={}, choices={}, answers={}, questions_type={}, maximum_mark={}, userID={},)",
                testID, questionID, question_text, question_materials, choices, answers, questions_type, maximum_mark,
                userID);
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
            } else if (fileType.equals("mp4")) {
                fileType = "VIDEO";
            } else {
                fileType = "BLANK";
            }
        } else {
            fileType = initQuestion.getQuestion_materials_type();
            originalFileName = initQuestion.getQuestion_materials();
        }

        logger.info("Initiate Operation Retrieve Table test by Query: testID={}", testID);
        Test test = testRepository.getTestByID(testID);
        logger.info("Operation Retrieve Table test by Query: testID={}. Result List: test={} | Success", testID, test);

        TestQuestion testQuestion = new TestQuestion(questionID, test, question_text, originalFileName, fileType,
                choices, questions_type, maximum_mark);

        logger.info("Initiate to Operation Insert Table Test Question Data {}", testQuestion.display());
        TestQuestion result = testQuestionRepository.save(testQuestion);
        logger.info("Operation Insert Table Test Question Data {} | Success", testQuestion.display());

        TestQuestionCorrectAnswer testQuestionCorrectAnswer = testQuestionCorrectAnswerRepositoy
                .getCorrectAnswerByQuestion(result.getId());
        TestQuestionCorrectAnswer correctAnswer = new TestQuestionCorrectAnswer(testQuestionCorrectAnswer.getId(),
                result, answers);

        logger.info("Initiate to Operation Insert Table Test Question Correct Answer Data {}", correctAnswer.display());
        testQuestionCorrectAnswerRepositoy.save(correctAnswer);
        logger.info("Operation Insert Table Test Question Correct Answer Data {} | Success", correctAnswer.display());
        logger.info(
                "Called editQuestionByTestID with parameter(testID={}, questionID={}, question_text={}, question_materials={}, choices={}, answers={}, questions_type={}, maximum_mark={}, userID={},) Success",
                testID, questionID, question_text, question_materials, choices, answers, questions_type, maximum_mark,
                userID);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @Valid
    @GetMapping(value = { "/teacher/delete-question", "/admin/delete-question" })
    private ResponseEntity deleteQuestionByQuestionID(@RequestParam(required = false) Long question_id) {

        Long userID = getUid();
        logger.info("Called deleteQuestionByQuestionID with parameter(question_id={}, userID={})", question_id, userID);
        Integer deleteCorrectAnswer = testQuestionCorrectAnswerRepositoy.deleteByQuestionID(question_id);
        Integer deleteQuestion = testQuestionRepository.deleteQuestionByID(question_id);
        logger.info("Called deleteQuestionByQuestionID with parameter(question_id={}, userID={}) Success", question_id,
                userID);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    private Long getUid() {
        UserAccount userAccount = userSessionService.getUserAccount();
        Long uid = userAccount.getAccountId();
        return uid;
    }

    private Long getGuestUserID() {
        Long guid = userSessionService.getGuestUserAccount().getGuest_id();
        return guid;
    }

}