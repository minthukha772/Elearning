package com.blissstock.mappingSite.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller

public class ReviewController {
    @RequestMapping("/StudentReview")
    private String StudentWriteReview() {
			return "CM0007_WriteReviewStudent.html";
		}
    @RequestMapping("/TeacherReview")
    private String TeacherWriteReview() {
            return "CM0007_WriteReviewTeacher.html";
    }
    
}
