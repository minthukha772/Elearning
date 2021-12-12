package com.blissstock.mappingSite.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class EnrollStudentController {
    
    @GetMapping("/admin-enroll-student")
    private String enrollStudent(){
        return "AD0002_EnrollStudent";
    }
}
