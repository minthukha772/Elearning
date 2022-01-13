package com.blissstock.mappingSite.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import com.blissstock.mappingSite.dto.CourseInfoDTO;
import com.blissstock.mappingSite.entity.CourseInfo;
import com.blissstock.mappingSite.model.CourseData;
import com.blissstock.mappingSite.service.CourseService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller

public class CourseListController {

    private final static Logger logger = LoggerFactory.getLogger(CourseListController.class);

    @Autowired
    CourseService courseService;

    @GetMapping("/guest/explore")
    private String getCourseListGuest(Model model, String courseName, String teacherName, LocalDate startDate,
            LocalDate endDate) {    
        logger.info("GET request");
        CourseInfoDTO courseInfoDTO = new CourseInfoDTO(courseName, teacherName, startDate, endDate);
       
        logger.debug("couresInfoDto {} ", courseInfoDTO);
        model.addAttribute("courseInfoDTO", courseInfoDTO);

        List<CourseInfo> courseList = courseService.getCourseList(courseInfoDTO);
        //Encasulate Data
        List<CourseData> courseDataList = courseList.stream().map((e) -> CourseData.construct(e)).collect(Collectors.toList());
        logger.info("Getting course, count {}",courseList.size());
        model.addAttribute("courseList", courseDataList);
        // Enable Advance Search Function
        model.addAttribute("searchable", true);
        //model.addAttribute("courseList", new ArrayList<>());
        return "CM0002_CourseList";
    }

}
