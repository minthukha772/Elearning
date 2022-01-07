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
import com.blissstock.mappingSite.repository.CourseInfoRepository;

@Controller
@RequestMapping("/")
public class IndexController {
    private static final Logger logger = LoggerFactory.getLogger(IndexController.class);

    @Autowired
    private CourseInfoRepository courseRepo;

    @GetMapping("/")
    private String getCourses(Model model) {
        try {
            logger.info("GET request");

            List<CourseInfo> liveList = courseRepo.findByClassType("Live");

            List<CourseInfo> videoList = courseRepo.findByClassType("Video");
            model.addAttribute("liveCourse", liveList);
           
            model.addAttribute("recordedCourse", videoList);

            System.out.print(liveList.toString());
        } catch (Exception e) {
            logger.info("Exception at index controller :: {}", e.toString());
            System.out.print(e.toString());
        }

        return "index.html";
    }

}
