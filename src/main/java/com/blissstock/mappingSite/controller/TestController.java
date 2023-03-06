package com.blissstock.mappingSite.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.ui.Model;

import com.blissstock.mappingSite.entity.CourseInfo;
import com.blissstock.mappingSite.entity.Test;
import com.blissstock.mappingSite.enums.UserRole;
import com.blissstock.mappingSite.repository.CourseInfoRepository;
import com.blissstock.mappingSite.repository.TestRepository;
import com.blissstock.mappingSite.service.UserSessionService;

@Controller
public class TestController {

    @Autowired
    UserSessionService userSessionService;

    @Autowired
    TestRepository testRepository;

    @Autowired
    CourseInfoRepository courseInfoRepository;

    @Valid
    @GetMapping(value = { "/teacher/exam" })
    private String getExamManagementPage(@PathVariable(required = false) Long userId,
            @PathVariable(required = false) String examStatus,
            Model model) {
        if (examStatus == null) {
            examStatus = "";
        }
        Long userID = getUid(null);
        List<Test> testList;
        List<CourseInfo> courseList;

        if (examStatus == "") {
            testList = testRepository.getListByUser(userID);
        } else {
            testList = testRepository.getListByStatusandUser(examStatus, userID);
        }
        courseList = courseInfoRepository.findByUID(userID);

        model.addAttribute("testList", testList);
        model.addAttribute("courseList", courseList);
        
        return "AT0004_ExamList";
    }

    private Long getUid(Long id) {
        Long uid = 0L;
        UserRole role = userSessionService.getRole();
        if (role == UserRole.ADMIN || role == UserRole.SUPER_ADMIN) {
            uid = id;
        } else if (id != null) {
            uid = id;
        } else if (role == UserRole.TEACHER || role == UserRole.STUDENT) {
            uid = userSessionService.getUserAccount().getAccountId();
        } else {
            throw new RuntimeException("user authetication fail");
        }
        return uid;
    }
}
