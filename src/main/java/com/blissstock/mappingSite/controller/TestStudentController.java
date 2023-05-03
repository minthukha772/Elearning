package com.blissstock.mappingSite.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.websocket.server.PathParam;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
import com.blissstock.mappingSite.entity.TestStudent;
import com.blissstock.mappingSite.entity.UserAccount;
import com.blissstock.mappingSite.repository.JoinCourseUserRepository;
import com.blissstock.mappingSite.repository.TestRepository;
import com.blissstock.mappingSite.repository.TestStudentRepository;
import com.blissstock.mappingSite.repository.UserAccountRepository;
import com.blissstock.mappingSite.service.JoinCourseService;
import com.blissstock.mappingSite.service.UserSessionService;

import org.springframework.ui.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class TestStudentController {
    private static Logger logger = LoggerFactory.getLogger(
            TestStudentController.class);

    @Autowired
    UserSessionService userSessionService;

    @Autowired
    TestStudentRepository testStudentRepository;

    @Autowired
    TestRepository testRepository;

    @Autowired
    JoinCourseUserRepository joinCourseUserRepository;

    @Autowired
    UserAccountRepository userAccountRepository;

    @Valid
    @GetMapping(value = { "/teacher/exam/{test_id}/examinee", "/admin/exam/{test_id}/examinee" })
    private String getTestStudent(@PathVariable Long test_id, Model model)
            throws ParseException {
        List<TestStudent> testStudents = testStudentRepository.getStudentByTest(test_id);
        model.addAttribute("test_id", test_id);
        model.addAttribute("test_students", testStudents);
        model.addAttribute("total_students", testStudents.size());
        return "AT0005_TestStudentList.html";
    }

    @Valid
    @PostMapping(value = { "/teacher/set-enrolled-examinee", "/admin/set-enrolled-examinee" })
    private String setEnrolledStudents(@RequestBody String testid)
            throws ParseException {
        JSONObject jsonObject = new JSONObject(testid);
        Long test_id = jsonObject.getLong("test_id");
        Test test = testRepository.getTestByID(test_id);
        if (test.getExam_status().equals("Exam Created")) {
            CourseInfo course = test.getCourseInfo();
            List<JoinCourseUser> enrolledList = joinCourseUserRepository.findByCourseID(course.getCourseId());
            for (JoinCourseUser student : enrolledList) {
                TestStudent testStudent = new TestStudent(null, test, student.getUserInfo());
                testStudentRepository.save(testStudent);
            }
        }

        return "AT0005_TestStudentList.html";
    }

    private Long getUid() {
        Long uid = userSessionService.getUserAccount().getAccountId();
        return uid;
    }
}