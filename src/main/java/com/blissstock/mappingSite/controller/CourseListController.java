package com.blissstock.mappingSite.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.blissstock.mappingSite.dto.CourseInfoDTO;
import com.blissstock.mappingSite.entity.CourseInfo;
import com.blissstock.mappingSite.entity.JoinCourseUser;
import com.blissstock.mappingSite.entity.UserInfo;
import com.blissstock.mappingSite.enums.UserRole;
import com.blissstock.mappingSite.model.CourseData;
import com.blissstock.mappingSite.model.FileInfo;
import com.blissstock.mappingSite.service.CourseService;
import com.blissstock.mappingSite.service.JoinCourseUserService;
import com.blissstock.mappingSite.service.StorageService;
import com.blissstock.mappingSite.service.UserService;
import com.blissstock.mappingSite.service.UserSessionService;
import com.blissstock.mappingSite.utils.StringToDateConvert;

import org.apache.commons.lang3.ObjectUtils.Null;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import net.bytebuddy.asm.Advice.Local;

@Controller

public class CourseListController {

    private final static Logger logger = LoggerFactory.getLogger(CourseListController.class);

    @Autowired
    CourseService courseService;

    @Autowired
    StorageService storageService;

    @Autowired
    UserSessionService userSessionService;

    @Autowired
    UserService userService;

    @Autowired
    JoinCourseUserService joinCourseUserService;

    @GetMapping("/guest/explore")
    private String getCourseListGuest(Model model, String courseName, String teacherName, String startDate,
            String endDate) {
        logger.info("GET request");

        CourseInfoDTO courseInfoDTO = new CourseInfoDTO(courseName, teacherName,
                StringToDateConvert.stringToDate(startDate), StringToDateConvert.stringToDate(endDate));

        logger.debug("couresInfoDto {} ", courseInfoDTO);
        model.addAttribute("courseInfoDTO", courseInfoDTO);

        List<CourseInfo> courseList = courseService.getCourseList(courseInfoDTO);
        // Encasulate Data
        List<CourseData> courseDataList = courseList.stream().map((e) -> CourseData.construct(e))
                .collect(Collectors.toList());
        logger.info("Getting course, count {}", courseList.size());
        model.addAttribute("courseList", courseDataList);
        // Enable Advance Search Function
        model.addAttribute("searchable", true);
        // model.addAttribute("courseList", new ArrayList<>());
        return "CM0002_CourseList";
    }

    @GetMapping("/guest/explore/teacher/{id}")
    private String getCourseListByTeacherID(Model model, @PathVariable Long id) {
        logger.info("GET request");

        UserInfo userInfo = userService.getUserInfoByID(id);
        if (userInfo == null || userInfo.getCourseInfo() == null
                || !userInfo.getUserAccount().getRole().equals(UserRole.TEACHER.getValue())) {
            return "redirect:/guest/explore/";
        }
        model.addAttribute("userInfo", userInfo);

        // ##################################################//
        // Load Profile Photo
        try {
            FileInfo profilePic = storageService.loadProfileAsFileInfo(userInfo);
            model.addAttribute("profilePic", profilePic);
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("unable to get profile {}", id);
        }

        // ##################################################//

        List<CourseInfo> courseList = userInfo.getCourseInfo();
        // Encasulate Data
        List<CourseData> courseDataList = courseList.stream().map((e) -> CourseData.construct(e))
                .collect(Collectors.toList());
        logger.info("Getting course, count {}", courseList.size());
        model.addAttribute("courseList", courseDataList);
        // Enable Disable Search Function
        model.addAttribute("searchable", false);
        // model.addAttribute("courseList", new ArrayList<>());
        return "CM0002_CourseList";
    }

    @GetMapping(value = "/teacher/my-course")
    public String teacherCourse(Model model) {
        Long uid = userSessionService.getId();
        return getCourseListByTeacherID(model, uid);

    }

    @GetMapping(value = "/student/my-course")
    public String studentCourse(Model model) {
        logger.info("GET Request");
        UserInfo userInfo = userSessionService.getUserInfo();
        List<JoinCourseUser> joinCourseUsers = joinCourseUserService.findByUserInfo(userInfo);
        if (joinCourseUsers == null) {
            joinCourseUsers = new ArrayList<>();
        }
        List<CourseData> courseDataList = joinCourseUsers.stream().map((e) -> {
            return CourseData.construct(e.getCourseInfo());
        }).collect(Collectors.toList());
        logger.info("Getting course, count {}", courseDataList.size());

        // ##################################################//
        // Load Profile Photo
        try {
            FileInfo profilePic = storageService.loadProfileAsFileInfo(userInfo);
            model.addAttribute("profilePic", profilePic);
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("unable to get profile {}", userInfo.getUid());
        }

        // ##################################################//

        model.addAttribute("userInfo", userInfo);
        model.addAttribute("courseList", courseDataList);
        // Disable Advance Search Function
        model.addAttribute("searchable", false);

        return "CM0002_CourseList";
    }

}
