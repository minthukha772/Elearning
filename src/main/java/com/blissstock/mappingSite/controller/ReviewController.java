package com.blissstock.mappingSite.controller;

import javax.validation.Valid;

import com.blissstock.mappingSite.entity.Review;
import com.blissstock.mappingSite.entity.UserInfo;
import com.blissstock.mappingSite.repository.ReviewRepository;
import com.blissstock.mappingSite.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller

public class ReviewController {
    @Autowired
    UserRepository userRepo;

    @Autowired
    ReviewRepository reviewRepo;

    @GetMapping(value="/StudentReview")
    private String getStudentReviewForm() {
    
    // UserInfo userInfo=userRepo.findById(uid).orElse(null);
    // model.addAttribute("userInfo", userInfo);

	return "CM0007_WriteReviewStudent.html";
	}
    @PostMapping(value="/PostStudentReview")
    private String StudentWriteReview(@Valid @ModelAttribute("review") Review inputReview,BindingResult bindingResult,Model model) {
    // ReviewInfo newReview = new ReviewInfo(inputReview.getFeed, inputHelper.getLastName(),
    // inputHelper.getGender(), inputHelper.getAcc().getBirthDate(), inputHelper.getCountryCode(),
    // inputHelper.getPhoneNo(), inputHelper.getBuildingNo(), inputHelper.getStreetAddress(),
    // inputHelper.getCity(), inputHelper.getState(), inputHelper.getPostalCode(), inputHelper.getCountry(),
    // inputHelper.getHireDate(), inputHelper.getContractStartDate(), inputHelper.getContractEndDate(),
    // inputHelper.getHourlyWage(), inputHelper.getHourlyWageCurrency(), inputHelper.getShiftType(),
    // inputHelper.getRemark(),inputHelper.getEducation());

	return "CM0007_WriteReviewStudent.html";
	}
    @RequestMapping("/TeacherReview")
    private String TeacherWriteReview() {
            return "CM0007_WriteReviewTeacher.html";
    }

}
    

