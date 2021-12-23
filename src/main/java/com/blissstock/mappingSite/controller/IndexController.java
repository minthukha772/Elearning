package com.blissstock.mappingSite.controller;

<<<<<<< HEAD
import org.springframework.ui.Model;
import java.util.*;
import com.blissstock.mappingSite.entity.CourseInfo;
=======

import java.util.List;

import com.blissstock.mappingSite.entity.CourseInfo;
import com.blissstock.mappingSite.repository.CourseRepository;

>>>>>>> e3ddc02530232fdac29a31f539222426c8a3c104
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
<<<<<<< HEAD
=======
import org.springframework.ui.Model;
>>>>>>> e3ddc02530232fdac29a31f539222426c8a3c104
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
        try {
            logger.info("GET request");

            List<CourseInfo> liveList = courseRepo.findByClassType("Live");

            List<CourseInfo> videoList = courseRepo.findByClassType("Video");
            model.addAttribute("liveCourse", liveList);
            // TODO change to video list
            model.addAttribute("recordedCourse", liveList);

            System.out.print(liveList.toString());
        } catch (Exception e) {
            logger.info("Exception at index controller :: {}", e.toString());
            System.out.print(e.toString());
        }

        return "index.html";
    }

}
