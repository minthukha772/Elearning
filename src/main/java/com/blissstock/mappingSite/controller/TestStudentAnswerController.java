package com.blissstock.mappingSite.controller;

import javax.mail.Multipart;
import javax.validation.Valid;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.blissstock.mappingSite.entity.Test;
import com.blissstock.mappingSite.entity.TestQuestion;
import com.blissstock.mappingSite.entity.TestQuestionCorrectAnswer;
import com.blissstock.mappingSite.entity.TestStudentAnswer;
import com.blissstock.mappingSite.entity.UserAccount;
import com.blissstock.mappingSite.exceptions.UnauthorizedFileAccessException;
import com.blissstock.mappingSite.model.FileInfo;
import com.blissstock.mappingSite.model.QuestionAndCorrectAnswer;
import com.blissstock.mappingSite.repository.TestQuestionCorrectAnswerRepositoy;
import com.blissstock.mappingSite.repository.TestQuestionRepository;
import com.blissstock.mappingSite.repository.TestRepository;
import com.blissstock.mappingSite.repository.TestStudentAnswerRepository;
import com.blissstock.mappingSite.repository.UserAccountRepository;
import com.blissstock.mappingSite.service.StorageService;
import com.blissstock.mappingSite.service.StorageServiceImpl;
import com.blissstock.mappingSite.service.UserAccountControlService;
import com.blissstock.mappingSite.service.UserSessionService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import org.springframework.util.StringUtils;

@Controller
public class TestStudentAnswerController {
    @Autowired
    TestStudentAnswerRepository testStudentAnswerRepository;

    @Autowired
    UserSessionService userSessionService;

    @Autowired
    TestQuestionCorrectAnswerRepositoy testQuestionCorrectAnswerRepositoy;

    @Autowired
    TestQuestionRepository testQuestionRepository;

    @Autowired
    TestRepository testRepository;

    @Autowired
    StorageService storageService;

    @Valid
    @PostMapping(value = { "/student/submit-answer" })
    private ResponseEntity getQuestionsByTest(@RequestParam(value = "test_id") Long test_id,
            @RequestParam(value = "question_id") Long question_id,
            @RequestParam(value = "student_answer") String student_answer,
            @RequestParam(value = "answer_type") String answer_type,
            @RequestParam(value = "answer_material", required = false) MultipartFile answer_material)
            throws JsonMappingException, JsonProcessingException, UnauthorizedFileAccessException {
        Long student_id = userSessionService.getUserAccount().getAccountId();
        String answerStatus = "FALSE";
        Integer acquiredmarks = 0;

        TestQuestion question = testQuestionRepository.getQuestionByID(question_id);

        if (!answer_type.equals("FREE_ANSWER")) {
            TestQuestionCorrectAnswer questionAndCorrectAnswer = testQuestionCorrectAnswerRepositoy
                    .getCorrectAnswerByQuestion(question_id);

            JSONArray studentAnswersArray = new JSONArray(student_answer);
            JSONArray correctAnswersArray = new JSONArray(questionAndCorrectAnswer.getCorrectAnswer());
            for (var i = 0; i < correctAnswersArray.length(); i++) {
                JSONObject correctAnswer = correctAnswersArray.getJSONObject(i);
                for (var j = 0; j < studentAnswersArray.length(); j++) {
                    JSONObject studentAnswer = studentAnswersArray.getJSONObject(j);
                    int correct_answer = correctAnswer.getInt("answer");
                    int student_choice = studentAnswer.getInt("student_choice");
                    if (correct_answer == student_choice) {
                        answerStatus = "TRUE";
                    }
                }
            }
            if (answerStatus == "TRUE") {
                acquiredmarks = question.getMaximum_mark();
            }
            TestStudentAnswer testStudentAnswer = new TestStudentAnswer(null, question.getTest(), question.getTest().getUserInfo(), question,
                    student_answer, "", answerStatus, acquiredmarks, "MARKED");
            testStudentAnswerRepository.save(testStudentAnswer);
        } else {
            String originalFileName = StringUtils.cleanPath(
                    answer_material.getOriginalFilename());

            long fileSeparator = 100000L + test_id + 00L + question_id + 00L + student_id;
            storageService.storeQuestionMaterials(fileSeparator, answer_material,
                    StorageServiceImpl.ANSWER_MATERIAL_PATH,
                    true);

            TestStudentAnswer testStudentAnswer = new TestStudentAnswer(null, question.getTest(), question.getTest().getUserInfo(), question,
                    student_answer, originalFileName, "", 0, "MARKING");
            testStudentAnswerRepository.save(testStudentAnswer);
        }

        return ResponseEntity.ok(HttpStatus.OK);
    }
}
