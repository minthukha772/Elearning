package com.blissstock.mappingSite.controller;

import java.util.List;
import java.util.Optional;

import com.blissstock.mappingSite.entity.CourseInfo;
import com.blissstock.mappingSite.enums.UserRole;
import com.blissstock.mappingSite.repository.CourseRepository;
import com.blissstock.mappingSite.repository.CourseTimeRepository;
import com.blissstock.mappingSite.repository.UserAccountRepository;
import com.blissstock.mappingSite.repository.UserInfoRepository;
import com.blissstock.mappingSite.service.UserSessionService;
import com.blissstock.mappingSite.entity.UserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
    private UserInfoRepository userInfoRepository;

    @Autowired
    private CourseRepository courseRepo;

    @Autowired
    private CourseTimeRepository courseTimeRepo;

    @GetMapping("/admin/enrollStudent/course/{id}")
    private String enrollStudent(@PathVariable(name = "id", required = true) Long id, Model model) {
        logger.info("Get Method");
        UserRole userRole = userSessionService.getRole();
        if (userRole.equals(UserRole.SUPER_ADMIN) || userRole.equals(UserRole.ADMIN)) {
            if (id != null) {
                try {
                    // http://localhost:8080/admin/enrollStudent/course/50001
                    Optional<CourseInfo> courseInfo = courseRepo.findById(id);
                    if (courseInfo.isPresent()) {
                        CourseInfo course = courseInfo.get();
                        // System.out.println(course.toString());
                        model.addAttribute("courseName", course.getCourseName());
                        // todo find teacher name
                        course.getUserInfo();
                        model.addAttribute("teachername", "Teacher");
                        model.addAttribute("level", course.getLevel());

                        List<UserInfo> userInfo = userInfoRepository.findStudentsToEnroll(id.toString());
                        System.out.println(userInfo.get(0).getUserName());
                        System.out.println(userInfo.toString());
                        model.addAttribute("students", userInfo);

                    } else {
                        logger.info("Course not found");

                    }
                } catch (Exception e) {
                    System.out.println(" get students to enroll");
                    logger.info(e.toString());
                }

            }
            return "AD0002_EnrollStudent";
        } else {
            return "redirect:/error/404";
        }

    }

}
