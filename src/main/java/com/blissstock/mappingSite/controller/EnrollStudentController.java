package com.blissstock.mappingSite.controller;

import java.util.Optional;

import com.blissstock.mappingSite.entity.CourseInfo;
import com.blissstock.mappingSite.enums.UserRole;
import com.blissstock.mappingSite.repository.CourseRepository;
import com.blissstock.mappingSite.repository.CourseTimeRepository;
import com.blissstock.mappingSite.service.UserSessionService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javassist.bytecode.stackmap.BasicBlock.Catch;

@Controller
public class EnrollStudentController {

    private static final Logger logger = LoggerFactory.getLogger(
            EnrollStudentController.class);

    @Autowired
    UserSessionService userSessionService;

    @Autowired
    private CourseRepository courseRepo;

    @Autowired
    private CourseTimeRepository courseTimeRepo;

    @GetMapping("/admin/enrollStudent/course/{id}")
    private String enrollStudent(@PathVariable(name = "id", required = true) Long id) {
        logger.info("Get Method");
        UserRole userRole = userSessionService.getRole();
        if (userRole.equals(UserRole.SUPER_ADMIN) || userRole.equals(UserRole.ADMIN)) {
            if (id != null) {
                try {
                    Optional<CourseInfo> course = courseRepo.findById(id);
                    if (course.isPresent()) {
                        System.out.println(course.toString());

                    } else {
                        logger.info("Course not found");

                    }
                } catch (Exception e) {
                    logger.info(e.toString());
                }

            }
            return "AD0002_EnrollStudent";
        } else {
            return "redirect:/error/404";
        }

    }

}
