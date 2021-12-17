package com.blissstock.mappingSite.controller;

import org.springframework.ui.Model;
import java.util.*;
import com.blissstock.mappingSite.entity.CourseInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.blissstock.mappingSite.repository.CourseRepository;

@Controller
@RequestMapping("/")
public class IndexController {
    private static final Logger logger = LoggerFactory.getLogger(CheckEmailController.class);

    @Autowired
    private CourseRepository courseRepo;

    @GetMapping("/")
    private String getCourses(Model model) {
        logger.info("GET request");
        List<CourseInfo> liveList = courseRepo.findByClassType("live");

        List<CourseInfo> recordedList = courseRepo.findByClassType("recorded");
        model.addAttribute("liveCourse", liveList);
        model.addAttribute("recordedCourse", recordedList);
        System.out.print(liveList.toString());
        return "index.html";
    }

}
