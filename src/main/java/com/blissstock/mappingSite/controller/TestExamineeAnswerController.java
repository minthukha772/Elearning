package com.blissstock.mappingSite.controller;

import javax.validation.Valid;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.blissstock.mappingSite.entity.TestQuestion;
import com.blissstock.mappingSite.entity.TestQuestionCorrectAnswer;
import com.blissstock.mappingSite.entity.GuestUser;
import com.blissstock.mappingSite.entity.TestExamineeAnswer;
import com.blissstock.mappingSite.entity.UserInfo;
import com.blissstock.mappingSite.exceptions.UnauthorizedFileAccessException;
import com.blissstock.mappingSite.repository.TestQuestionCorrectAnswerRepositoy;
import com.blissstock.mappingSite.repository.TestQuestionRepository;
import com.blissstock.mappingSite.repository.TestRepository;
import com.blissstock.mappingSite.repository.GuestUserRepository;
import com.blissstock.mappingSite.repository.TestExamineeAnswerRepository;
import com.blissstock.mappingSite.repository.UserInfoRepository;
import com.blissstock.mappingSite.service.StorageService;
import com.blissstock.mappingSite.service.StorageServiceImpl;
import com.blissstock.mappingSite.service.UserSessionService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import org.springframework.util.StringUtils;

@Controller
public class TestExamineeAnswerController {

        private static Logger logger = LoggerFactory.getLogger(TestController.class);

        @Autowired
        TestExamineeAnswerRepository TestExamineeAnswerRepository;

        @Autowired
        UserSessionService userSessionService;

        @Autowired
        GuestUserRepository guestUserRepository;

        @Autowired
        TestQuestionCorrectAnswerRepositoy testQuestionCorrectAnswerRepositoy;

        @Autowired
        TestQuestionRepository testQuestionRepository;

        @Autowired
        TestRepository testRepository;

        @Autowired
        UserInfoRepository userInfoRepository;

        @Autowired
        StorageService storageService;

        @Valid
        @PostMapping(value = { "/student/submit-answer" })
        private ResponseEntity submitAnswer(@RequestParam(value = "test_id") Long test_id,
                        @RequestParam(value = "question_id") Long question_id,
                        @RequestParam(value = "student_answer") String student_answer,
                        @RequestParam(value = "answer_type") String answer_type,
                        @RequestParam(value = "answer_material", required = false) MultipartFile answer_material)
                        throws JsonMappingException, JsonProcessingException, UnauthorizedFileAccessException {
                Long userID = getUid();
                logger.info(
                                "Called submitAnswer with parameter (test_id={}, question_id={}, student_answer={}, answer_type={}, answer_material={})",
                                test_id, question_id, student_answer, answer_type, answer_material.getSize());
                logger.info("user_id: {}", userID);
                Long student_id = userSessionService.getUserAccount().getAccountId();
                logger.info("Initiate Operation Retrieve Table user_info by Query: student_id={}", student_id);
                UserInfo student = userInfoRepository.findStudentById(student_id);
                logger.info("Operation Retrieve Table user_info by Query: student_id={}. Result List: student={} | Success",
                                student_id, student);
                String answerStatus = "FALSE";
                Integer acquiredmarks = 0;
                logger.info("Initiate Operation Retrieve Table test_question by Query: question_id={}", question_id);
                TestQuestion question = testQuestionRepository.getQuestionByID(question_id);
                logger.info(
                                "Operation Retrieve Table test_question by Query: question_id={}. Result List: Question={} | Success",
                                question_id, question);
                if (!answer_type.equals("FREE_ANSWER")) {
                        logger.info("Initiate Operation Retrieve Table test_question_correct_answer by Query: question_id={}",
                                        question_id);
                        TestQuestionCorrectAnswer questionAndCorrectAnswer = testQuestionCorrectAnswerRepositoy
                                        .getCorrectAnswerByQuestion(question_id);
                        logger.info(
                                        "Operation Retrieve Table test_question_correct_answer by Query: question_id={}. Result List: questionAndCorrectAnswer={} | Success",
                                        question_id, questionAndCorrectAnswer);
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
                        logger.info("Initiate Operation Retrieve Table test_student_answer by Query: question_id={}, student_id={}",
                                        question_id, student_id);
                        TestExamineeAnswer checkTestStudent = TestExamineeAnswerRepository
                                        .getStudentAnswerByQuestionID(question.getId(), student_id);
                        logger.info(
                                        "Operation Retrieve Table test_student_answer by Query: question_id={}, student_id={}. Result List: checkTestStudent={} | Success",
                                        question_id, student_id, checkTestStudent);

                        if (checkTestStudent == null) {
                                TestExamineeAnswer TestExamineeAnswer = new TestExamineeAnswer(null, question.getTest(),
                                                student, null, question,
                                                student_answer, "", answerStatus, acquiredmarks, "MARKED");
                                logger.info("Initiate to Operation Insert Table Test Student Answer Data {}",
                                                TestExamineeAnswer.display());
                                TestExamineeAnswerRepository.save(TestExamineeAnswer);
                                logger.info("Operation Insert Table Test Student Answer Data {} | Success",
                                                TestExamineeAnswer.display());

                        }
                } else {
                        String originalFileName = "";
                        if (answer_material != null) {
                                originalFileName = StringUtils.cleanPath(
                                                answer_material.getOriginalFilename());
                                long studentfileSeparator = Long.parseLong(test_id.toString()
                                                + question_id.toString() + student_id.toString());
                                storageService.storeQuestionMaterials(studentfileSeparator, answer_material,
                                                StorageServiceImpl.ANSWER_MATERIAL_PATH,
                                                true);
                        }

                        TestExamineeAnswer checkTestStudent = TestExamineeAnswerRepository
                                        .getStudentAnswerByQuestionID(question.getId(), student_id);
                        if (checkTestStudent == null) {
                                TestExamineeAnswer TestExamineeAnswer = new TestExamineeAnswer(null, question.getTest(),
                                                student, null, question,
                                                student_answer, originalFileName, "", 0, "MARKING");
                                logger.info("Initiate to Operation Insert Table Test Student Answer Data {}",
                                                TestExamineeAnswer.display());
                                TestExamineeAnswerRepository.save(TestExamineeAnswer);
                                logger.info("Operation Insert Table Test Student Answer Data {} | Success",
                                                TestExamineeAnswer.display());
                        }
                }
                logger.info(
                                "Called submitAnswer with parameter (test_id={}, question_id={}, student_answer={}, answer_type={}, answer_material={}) Success",
                                test_id, question_id, student_answer, answer_type, answer_material.getSize());
                return ResponseEntity.ok(HttpStatus.OK);
        }

        @Valid
        @PostMapping(value = { "/guestUser/submit-answer" })
        private ResponseEntity submitAnswerGuestUser(@RequestParam(value = "test_id") Long test_id,
                        @RequestParam(value = "question_id") Long question_id,
                        @RequestParam(value = "student_answer") String student_answer,
                        @RequestParam(value = "answer_type") String answer_type,
                        @RequestParam(value = "answer_material", required = false) MultipartFile answer_material)
                        throws JsonMappingException, JsonProcessingException, UnauthorizedFileAccessException {
                Long guestUserId = getGuestid();
                GuestUser guestUser = guestUserRepository.findByGuestId(guestUserId);
                logger.info("Initiate Operation Retrieve Table user_info by Query: guestUserId={}", guestUserId);
                String answerStatus = "FALSE";
                Integer acquiredmarks = 0;
                logger.info("Initiate Operation Retrieve Table test_question by Query: question_id={}", question_id);
                TestQuestion question = testQuestionRepository.getQuestionByID(question_id);
                logger.info(
                                "Operation Retrieve Table test_question by Query: question_id={}. Result List: Question={} | Success",
                                question_id, question);
                if (!answer_type.equals("FREE_ANSWER")) {
                        logger.info("Initiate Operation Retrieve Table test_question_correct_answer by Query: question_id={}",
                                        question_id);
                        TestQuestionCorrectAnswer questionAndCorrectAnswer = testQuestionCorrectAnswerRepositoy
                                        .getCorrectAnswerByQuestion(question_id);
                        logger.info("Operation Retrieve Table test_question_correct_answer by Query: question_id={}. Result List: questionAndCorrectAnswer={} | Success",
                                        question_id, questionAndCorrectAnswer);
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
                        logger.info("Initiate Operation Retrieve Table test_student_answer by Query: question_id={}, student_id={}",
                                        question_id, guestUserId);
                        TestExamineeAnswer checkTestStudent = TestExamineeAnswerRepository
                                        .getGuestUserAnswerByQuestionID(question.getId(), guestUserId);
                        logger.info(
                                        "Operation Retrieve Table test_student_answer by Query: question_id={}, guestUserId={}. Result List: checkTestStudent={} | Success",
                                        question_id, guestUserId, checkTestStudent);

                        if (checkTestStudent == null) {
                                TestExamineeAnswer TestExamineeAnswer = new TestExamineeAnswer(null, question.getTest(),
                                                null, guestUser, question,
                                                student_answer, "", answerStatus, acquiredmarks, "MARKED");
                                logger.info("Initiate to Operation Insert Table Test Student Answer Data {}",
                                                TestExamineeAnswer.display());
                                TestExamineeAnswerRepository.save(TestExamineeAnswer);
                                logger.info("Operation Insert Table Test Guest User Answer Data {} | Success",
                                                TestExamineeAnswer.display());

                        }
                } else {
                        String originalFileName = "";
                        if (answer_material != null) {
                                originalFileName = StringUtils.cleanPath(
                                                answer_material.getOriginalFilename());
                                long studentfileSeparator = Long.parseLong(test_id.toString()
                                                + question_id.toString() + guestUserId.toString());
                                storageService.storeQuestionMaterials(studentfileSeparator, answer_material,
                                                StorageServiceImpl.ANSWER_MATERIAL_PATH,
                                                true);
                        }

                        TestExamineeAnswer checkTestStudent = TestExamineeAnswerRepository
                                        .getGuestUserAnswerByQuestionID(question.getId(), guestUserId);
                        if (checkTestStudent == null) {
                                TestExamineeAnswer TestExamineeAnswer = new TestExamineeAnswer(null, question.getTest(),
                                                null, guestUser, question,
                                                student_answer, originalFileName, "", 0, "MARKING");
                                logger.info("Initiate to Operation Insert Table Test Student Answer Data {}",
                                                TestExamineeAnswer.display());
                                TestExamineeAnswerRepository.save(TestExamineeAnswer);
                                logger.info("Operation Insert Table Test Student Answer Data {} | Success",
                                                TestExamineeAnswer.display());
                        }
                }
                logger.info(
                                "Called submitAnswer with parameter (test_id={}, question_id={}, student_answer={}, answer_type={}, answer_material={}) Success",
                                test_id, question_id, student_answer, answer_type, answer_material.getSize());
                return ResponseEntity.ok(HttpStatus.OK);
        }

        @Valid
        @PostMapping(value = { "/teacher/mark-examinee-question", "/admin/mark-examinee-question" })
        private ResponseEntity updateExamineeFreeQuestion(
                        @RequestParam(value = "id") Long id,
                        @RequestParam(value = "acquired_mark") Integer acquired_mark)
                        throws UnauthorizedFileAccessException {
                logger.info("Called updateExamineeFreeQuestion with parameter(id={}, acquired_mark={})", id,
                                acquired_mark);
                logger.info("Initiate Operation Retrieve Table test_examinee_answer by Query: id={}",
                                id);
                TestExamineeAnswer TestExamineeAnswer = TestExamineeAnswerRepository.getExamineeAnswerByID(id);
                logger.info(
                                "Operation Retrieve Table test_examinee_answer by Query: id={}. Result List: TestExamineeAnswer={} | Success",
                                TestExamineeAnswer);
                TestExamineeAnswer.setAcquired_mark(acquired_mark);
                TestExamineeAnswer.setMarked_status("MARKED");
                logger.info("Initiate to Operation Insert Table test_examinee_answer Data {}",
                                TestExamineeAnswer.display());
                TestExamineeAnswerRepository.save(TestExamineeAnswer);
                logger.info("Operation Insert Table test_examinee_answer Data {} | Success",
                                TestExamineeAnswer.display());
                logger.info("Called updateExamineeFreeQuestion with parameter(id={}, acquired_mark={}) Success", id,
                                acquired_mark);
                return ResponseEntity.ok(HttpStatus.OK);
        }

        private Long getUid() {
                Long uid = userSessionService.getUserAccount().getAccountId();
                return uid;
        }

        private Long getGuestid() {
                Long uid = userSessionService.getGuestUserAccount().getGuest_id();
                return uid;
        }
}