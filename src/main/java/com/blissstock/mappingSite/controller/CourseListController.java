package com.blissstock.mappingSite.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import javax.websocket.server.PathParam;

import com.blissstock.mappingSite.dto.CourseInfoDTO;
import com.blissstock.mappingSite.entity.CourseInfo;
import com.blissstock.mappingSite.entity.UserInfo;
import com.blissstock.mappingSite.enums.UserRole;
import com.blissstock.mappingSite.model.CourseData;
import com.blissstock.mappingSite.service.CourseService;
import com.blissstock.mappingSite.service.UserService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller

public class CourseListController {

    private final static Logger logger = LoggerFactory.getLogger(CourseListController.class);

    @Autowired
    CourseService courseService;

    @Autowired
    UserService userService;

    @GetMapping("/guest/explore")
    private String getCourseListGuest(Model model, String courseName, String teacherName, LocalDate startDate,
            LocalDate endDate) {
        logger.info("GET request");
        CourseInfoDTO courseInfoDTO = new CourseInfoDTO(courseName, teacherName, startDate, endDate);

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
        logger.debug("User Info: {}", userInfo);
        logger.debug("userInfo == null: {}", userInfo == null);
        logger.debug("userInfo.getCourseInfo() == null: {}", userInfo.getCourseInfo() == null);
        logger.debug("!{}.equals(UserRole.TEACHER.getValue()): {}",
                userInfo.getUserAccount().getRole(),
                !userInfo.getUserAccount().getRole().equals(UserRole.TEACHER.getValue()));

        if (userInfo == null || userInfo.getCourseInfo() == null
                || !userInfo.getUserAccount().getRole().equals(UserRole.TEACHER.getValue())) {
            return "redirect:/guest/explore/";
        }

        List<CourseInfo> courseList = userInfo.getCourseInfo();
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

}
