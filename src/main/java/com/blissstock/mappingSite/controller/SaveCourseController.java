package com.blissstock.mappingSite.controller;

import javax.servlet.http.HttpServletRequest;

import com.blissstock.mappingSite.service.CourseSessionService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import com.blissstock.mappingSite.service.UserSessionService;

@Controller
public class SaveCourseController {

    @Autowired
    CourseSessionService courseSessionService;

    @Autowired
    UserSessionService userSessionService;

    private static final Logger logger = LoggerFactory.getLogger(SaveCourseController.class);
    
    @PostMapping("/guest/save/course-id/{courseId}")
    public String saveCourse(@PathVariable long courseId, HttpServletRequest request){

        Long userID = getUid();
        String role = getUserRole();

        try {
        //logger.info("Post Request");
        logger.info("Called saveCourse with parameter(course_id={})", courseId);
        logger.info("user_id: {}, role: {}", userID, role);
        courseSessionService.saveCourse(request, courseId);
        logger.info("Called saveCourse with parameter(course_id={}) Success", courseId);
        logger.info("user_id: {}, role: {}", userID, role);
        return "redirect:/login";
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage());
            return "500";
        }
    }

    private Long getUid() {
        Long uid = userSessionService.getUserAccount().getAccountId();
        return uid;
    }

    private String getUserRole() {
        String userRole = userSessionService.getUserAccount().getRole();
        return userRole;
    }

    // @GetMapping("/guest/get/saved")
    // public String getCourse( HttpServletRequest request){
    //     logger.info("Get Request");
    //     long courseId = -1;
    //     try{
    //         courseId = (long) request.getSession().getAttribute("courseId");
    //         request.getSession().removeAttribute("courseId");
    //     }catch(NullPointerException e){
    //         e.printStackTrace();
            
    //     }
    //     logger.info("courseId: {}",courseId);
        
    //     return "redirect:/login";
    // }
    
}
