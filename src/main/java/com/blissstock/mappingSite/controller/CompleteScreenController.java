package com.blissstock.mappingSite.controller;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CompleteScreenController {

    private static final Logger logger = LoggerFactory.getLogger(CompleteScreenController.class);
    
    @RequestMapping("/card")
    public String CardSample(){
        return "card";
    }
    
    // TODO Change NavBars
    
    @RequestMapping("/register/student/complete")
    public String StudentRegisterComplete(Model model) {
        String header3 = "Student Register Complete";
        String header5 = "Congratulation!";
        String paragraph = "You have reached the final step of registration! Please check the email to start using the service.";
        model.addAttribute("header3", header3);
        model.addAttribute("header5", header5);
        model.addAttribute("paragraph", paragraph);
        List<String> breadcrumbList = new ArrayList<>();
        breadcrumbList.add("Student");
        breadcrumbList.add("Register");
        breadcrumbList.add("Complete");
        model.addAttribute("breadcrumbList",breadcrumbList);
        return "CM0001_CompleteScreen";
    }
    
    @RequestMapping("/Admin/StudentList/Register/Complete")
    public String StudentRegisterCompleteByAdmin(Model model) {
        String header3 = "Student Register Complete";
        String header5 = "Congratulation!";
        String paragraph = "You have reached the final step of registration! Please check the email to start using the service.";
        model.addAttribute("header3", header3);
        model.addAttribute("header5", header5);
        model.addAttribute("paragraph", paragraph);
        List<String> breadcrumbList = new ArrayList<>();
        breadcrumbList.add("Admin");
        breadcrumbList.add("StudentList");
        breadcrumbList.add("Register");
        breadcrumbList.add("Complete");
        model.addAttribute("breadcrumbList",breadcrumbList);
        return "CM0001_CompleteScreen";
    }
    
    @RequestMapping("/register/teacher/complete")
    public String TeacherRegisterComplete(Model model) {
        String header3 = "Teacher Register Complete";
        String header5 = "Congratulation!";
        String paragraph = "Thank you for using our services! We will contact you as soon as possible. Don't forget to check your email.";
        model.addAttribute("header3", header3);
        model.addAttribute("header5", header5);
        model.addAttribute("paragraph", paragraph);
        List<String> breadcrumbList = new ArrayList<>();
        breadcrumbList.add("Teacher");
        breadcrumbList.add("Register");
        breadcrumbList.add("Complete");
        model.addAttribute("breadcrumbList",breadcrumbList);
        return "CM0001_CompleteScreen";
    }
    
    @RequestMapping("/Review/Complete")
    public String ReviewComplete(Model model) {
        String header3 = "Review Complete";
        String header5 = "Congratulation!";
        String paragraph = "You have reached the final step of registration! Please check the email to start using the service.";
        model.addAttribute("header3", header3);
        model.addAttribute("header5", header5);
        model.addAttribute("paragraph", paragraph);
        List<String> breadcrumbList = new ArrayList<>();
        breadcrumbList.add("Review");
        breadcrumbList.add("Complete");
        model.addAttribute("breadcrumbList",breadcrumbList);
        return "CM0001_CompleteScreen";
    }
    
    @RequestMapping("/Admin/Review/Complete")
    public String ReviewCompleteByAdmin(Model model) {
        String header3 = "Review Complete";
        String header5 = "Thank you for your feedback!";
        String paragraph = "Thank you for using our services! We have received your feedback.";
        model.addAttribute("header3", header3);
        model.addAttribute("header5", header5);
        model.addAttribute("paragraph", paragraph);
        List<String> breadcrumbList = new ArrayList<>();
        breadcrumbList.add("Admin");
        breadcrumbList.add("Review");
        breadcrumbList.add("Complete");
        model.addAttribute("breadcrumbList",breadcrumbList);
        return "CM0001_CompleteScreen";
    }
    
    @RequestMapping("/Payment/Complete")
    public String PaymentComplete(Model model) {
        String header3 = "Payment Complete";
        String header5 = "Acknowledgement!";
        String paragraph = "Thank you for using our services! Your payment has been successful.";
        model.addAttribute("header3", header3);
        model.addAttribute("header5", header5);
        model.addAttribute("paragraph", paragraph);
        List<String> breadcrumbList = new ArrayList<>();
        breadcrumbList.add("Payment");
        breadcrumbList.add("Complete");
        model.addAttribute("breadcrumbList",breadcrumbList);
        return "CM0001_CompleteScreen";
    }
    
    @RequestMapping("/TakeALeave/Complete")
    public String TakeALeaveComplete(Model model) {
        String header3 = "Requesting a Leave Complete";
        String header5 = "Acknowledgement!";
        String paragraph = "Thank you for using our services! Your request for taking a leave have been received.";
        model.addAttribute("header3", header3);
        model.addAttribute("header5", header5);
        model.addAttribute("paragraph", paragraph);
        List<String> breadcrumbList = new ArrayList<>();
        breadcrumbList.add("TakeALeave");
        breadcrumbList.add("Complete");
        model.addAttribute("breadcrumbList",breadcrumbList);
        return "CM0001_CompleteScreen";
    }
    
    @RequestMapping("/UploadCourse/Complete")
    public String UploadCourseComplete(Model model) {
        String header3 = "Course Upload Complete";
        String header5 = "Acknowledgement!";
        String paragraph = "Course upload has been successful.";
        model.addAttribute("header3", header3);
        model.addAttribute("header5", header5);
        model.addAttribute("paragraph", paragraph);
        List<String> breadcrumbList = new ArrayList<>();
        breadcrumbList.add("UploadCourse");
        breadcrumbList.add("Complete");
        model.addAttribute("breadcrumbList",breadcrumbList);
        return "CM0001_CompleteScreen";
    }
    
    @RequestMapping("/Admin/UploadCourse/Complete")
    public String UploadCourseCompleteByAdmin(Model model) {
        String header3 = "Course Upload Complete";
        String header5 = "Acknowledgement!";
        String paragraph = "Course upload has been successful.";
        model.addAttribute("header3", header3);
        model.addAttribute("header5", header5);
        model.addAttribute("paragraph", paragraph);
        List<String> breadcrumbList = new ArrayList<>();
        breadcrumbList.add("Admin");
        breadcrumbList.add("UploadCourse");
        breadcrumbList.add("Complete");
        model.addAttribute("breadcrumbList",breadcrumbList);
        return "CM0001_CompleteScreen";
    }
    
    // @GetMapping("/complete")
    // public String completebox(){
        // return "CM0001_Complete";
        // }
        
        // @GetMapping("/complete")
        // public String getCondition(@RequestParam(required = false) String
        // confirmClick, Model model) {
            // model.addAttribute("confirm", confirmClick);
            // return "confirm/CM0001_Complete";
            // }
            
            // @GetMapping("/complete")
            // public String completeDirect(Model model) {
                // model.addAttribute("complete", new Greeting());
                // return "redirect:complete#registercomplete";
                // }
                
            }
            