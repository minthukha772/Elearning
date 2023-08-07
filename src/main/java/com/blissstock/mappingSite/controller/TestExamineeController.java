package com.blissstock.mappingSite.controller;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import javax.validation.Valid;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.blissstock.mappingSite.entity.CourseInfo;
import com.blissstock.mappingSite.entity.JoinCourseUser;
import com.blissstock.mappingSite.entity.Test;
import com.blissstock.mappingSite.entity.TestQuestion;
import com.blissstock.mappingSite.entity.TestExaminee;
import com.blissstock.mappingSite.entity.UserInfo;
import com.blissstock.mappingSite.model.TestExamineeWithMarkedCountModel;
import com.blissstock.mappingSite.repository.JoinCourseUserRepository;
import com.blissstock.mappingSite.repository.TestQuestionRepository;
import com.blissstock.mappingSite.repository.TestRepository;
import com.blissstock.mappingSite.repository.TestExamineeAnswerRepository;
import com.blissstock.mappingSite.repository.TestExamineeRepository;
import com.blissstock.mappingSite.repository.UserAccountRepository;
import com.blissstock.mappingSite.repository.UserInfoRepository;
import com.blissstock.mappingSite.service.UserSessionService;

import org.springframework.ui.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class TestExamineeController {
    private static Logger logger = LoggerFactory.getLogger(
            TestExamineeController.class);

    @Autowired
    UserSessionService userSessionService;

    @Autowired
    TestExamineeRepository testStudentRepository;

    @Autowired
    TestExamineeAnswerRepository testStudentAnswerRepository;

    @Autowired
    TestRepository testRepository;

    @Autowired
    TestQuestionRepository testQuestionRepository;

    @Autowired
    JoinCourseUserRepository joinCourseUserRepository;

    @Autowired
    UserAccountRepository userAccountRepository;

    @Autowired
    UserInfoRepository userInfoRepository;

    @Valid
    @GetMapping(value = { "/teacher/exam/{test_id}/examinee", "/admin/exam/{test_id}/examinee" })
    private String getTestExaminee(@PathVariable Long test_id, Model model,
    
            @RequestParam(required = false) String name) 
            throws ParseException {
                logger.info("API name : {}.Parameter : {}", "getTestExaminee",test_id);
                logger.info("Operation Retrieve Table {} by query : findByNameandTestId {} {}", "TestExaminee", name, test_id);
                logger.info("Initiate to Operation Retrieve Table {} by query : findByNameandTestId {}", "TestExaminee", name, test_id);
        List<TestExaminee> testStudents = new ArrayList<>();
        List<TestExamineeWithMarkedCountModel> testStudentList = new ArrayList<>();
        int checked_students = 0;
        int total_free_questions = 0;
        if (name == null) {
            
            testStudents = testStudentRepository.getStudentByTest(test_id);
        } else {
            logger.info("Initiate to Operation Retrieve Table {} by query :findByNameandTestId{}{}","TestExaminee", name, test_id);
            testStudents = testStudentRepository.findByNameandTestId(name, test_id);
            logger.info("Operation Retrieve Table {} by query :findByNameandTestId{}{}Result List : {} Success","TestExaminee", name, test_id,testStudents.toString());
        }
        total_free_questions = testQuestionRepository.getFreeAnswerCount(test_id);
        for (TestExaminee TestExaminee : testStudents) {
            Integer answerCount = testStudentAnswerRepository.getCountStudentAnswerListByTestAndStudent(test_id,
                    TestExaminee.getUserInfo().getUid());
            if (answerCount == 0) {
                TestExamineeWithMarkedCountModel testStudentWithMarkedCountModel = new TestExamineeWithMarkedCountModel(
                        TestExaminee.getId(), TestExaminee.getTest(), TestExaminee.getUserInfo(), total_free_questions,
                        0);
                testStudentList.add(testStudentWithMarkedCountModel);
            } else {
                int uncheck_free_questions = testStudentAnswerRepository.getUnCheckAnswerCountByTestAndStudent(
                        test_id,
                        TestExaminee.getUserInfo().getUid());
                TestExamineeWithMarkedCountModel testStudentWithMarkedCountModel = new TestExamineeWithMarkedCountModel(
                        TestExaminee.getId(), TestExaminee.getTest(), TestExaminee.getUserInfo(), total_free_questions,
                        total_free_questions - uncheck_free_questions);
                if (uncheck_free_questions == 0) {
                    checked_students++;
                }
                testStudentList.add(testStudentWithMarkedCountModel);
            }
        }
        logger.info("API name : {}. Parameter : {}", "getTestExaminee", test_id);
        logger.info("Initiate to Operation Save File {}", test_id);
        model.addAttribute("user_role", userSessionService.getRole());
        model.addAttribute("test_id", test_id);
        model.addAttribute("test_students", testStudentList);
        model.addAttribute("total_students", testStudents.size());
        model.addAttribute("check_students", checked_students);
        return "AT0005_TestExamineeList.html";
    }

    @Valid
    @GetMapping(value = { "/teacher/get-student", "/admin/get-student" })
    private ResponseEntity getCustomStudent(@RequestParam(value = "name") String name)
            throws ParseException { 
                logger.info("API name : {}.Parameter : {}", "getCustomStudent",name);
                logger.info("Initiate to Operation Insert Table {} Data {}", "TestExaminee", name);
        List<UserInfo> testStudents = userInfoRepository.findByName(name, name.toLowerCase());
        return ResponseEntity.ok(testStudents);
    }

    @Valid
    @GetMapping(value = { "/teacher/get-student-exam", "/admin/get-student-exam" })
    private ResponseEntity getCustomStudentExam(@RequestParam(value = "name") String name,
            @RequestParam(value = "test_id") Long test_id)
            throws ParseException {
                 logger.info("API name : {}.Parameter : {}", "getCustomStudentExam",name);
                 logger.info("Operation Retrieve Table {} by query : findByNameandTestId {} {}", "TestExaminee",name, test_id);

        List<UserInfo> testStudents = userInfoRepository.findByNameandTestId(name, test_id);
        return ResponseEntity.ok(testStudents);
    }

    @Valid
    @PostMapping(value = { "/teacher/set-enrolled-examinee", "/admin/set-enrolled-examinee" })
    private String setEnrolledStudents(@RequestBody String testid)
            throws ParseException {
                 logger.info("API name : {}.Parameter : {}", "setEnrolledStudents",testid);
                 logger.info("Operation Retrieve Table {} by query : findByNameandTestId {} {}", "TestExaminee", testid);
                 logger.info("Initiate to Operation Retrieve Table {} by query : findByNameandTestId {}", "TestExaminee",testid );
        JSONObject jsonObject = new JSONObject(testid);
        Long test_id = jsonObject.getLong("test_id");
        Test test = testRepository.getTestByID(test_id);
        if (test.getExam_status().equals("Exam Created")) {
            CourseInfo course = test.getCourseInfo();
            List<JoinCourseUser> enrolledList = joinCourseUserRepository.findByStudentByCourseID(course.getCourseId());
            for (JoinCourseUser student : enrolledList) {
                TestExaminee checkStudent = testStudentRepository.getStudentByID(student.getUserInfo().getUid(),
                        test_id);
                if (checkStudent == null) {
                    TestExaminee TestExaminee = new TestExaminee(null, test, student.getUserInfo(), null);
                    testStudentRepository.save(TestExaminee);
                }
            }
        }
        logger.info("API name : {}. Parameter : {}", "setCustomStudents", testid);
        logger.info("Operation Insert Table {} Data {} Success", "TestExaminee", testid);
        logger.info("Operation Save File {} Success", testid);
        return "AT0005_TestExamineeList.html";
    }

    @Valid
    @PostMapping(value = { "/teacher/set-examinee", "/admin/set-examinee" })
    private String setCustomStudents(@RequestBody String testid)
            throws ParseException {
                logger.info("API name : {}.Parameter : {}", "setCustomStudents",testid);
                logger.info("Operation Retrieve Table {} by query : findByNameandTestId {} {}", "TestExaminee", testid);
                 logger.info("Initiate to Operation Retrieve Table {} by query : findByNameandTestId {}", "TestExaminee",testid );
                 logger.info("Initiate to Operation Insert Table {} Data {}", "TestExaminee", testid);
                 logger.info("Initiate to Operation Save File {}", testid);
        JSONObject jsonObject = new JSONObject(testid);
        Long test_id = jsonObject.getLong("test_id");
        Long student_id = jsonObject.getLong("student_id");
        Test test = testRepository.getTestByID(test_id);
        UserInfo user = userInfoRepository.findStudentById(student_id);
        if (test.getExam_status().equals("Exam Created")) {
            TestExaminee existingStudent = testStudentRepository.getStudentByID(student_id, test_id);
            if (existingStudent == null) {
         logger.info("Initiate to Operation Retrieve Table {} by query :findByNameandTestId{}{}","TestExaminee", student_id, test_id);
         logger.info("Initiate to Operation Update Table {} Data {} By {} = {}", "TestExaminee", testid, "student_id", student_id);
                TestExaminee TestExaminee = new TestExaminee(null, test, user, null);
                testStudentRepository.save(TestExaminee);
            logger.info("Operation Retrieve Table {} by query :findByNameandTestId{}{}Result List : {} Success","TestExaminee", student_id, test_id,TestExaminee.toString()); 
            logger.info("Operation Update Table {} Data {} By {} = {} Success", "TestExaminee", testid, "student_id", student_id);   
            }
        }
        logger.info("API name : {}. Parameter : {}", "setCustomStudents", testid);
        logger.info("Operation Insert Table {} Data {} Success", "TestExaminee", testid);
        logger.info("Operation Save File {} Success", testid);
        return "AT0005_TestExamineeList.html";
    }

    private Long getUid() {
        Long uid = userSessionService.getUserAccount().getAccountId();
        return uid;
    }
}