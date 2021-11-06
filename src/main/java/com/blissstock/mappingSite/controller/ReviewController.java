package com.blissstock.mappingSite.controller;

import javax.validation.Valid;

import com.blissstock.mappingSite.dto.ReviewDTO;
import com.blissstock.mappingSite.entity.Review;
import com.blissstock.mappingSite.entity.ReviewTest;
import com.blissstock.mappingSite.entity.UserInfo;
import com.blissstock.mappingSite.repository.ReviewRepository;
import com.blissstock.mappingSite.repository.ReviewTestRepository;
import com.blissstock.mappingSite.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller

public class ReviewController {
    @Autowired
    UserRepository userRepo;

    @Autowired
    ReviewTestRepository reviewTestRepo;
    //get student review 
    @Valid
    @GetMapping(value="/student-review/{courseName}/{userName}")
    private String getStudentReviewForm(@PathVariable String courseName, @PathVariable String userName, Model model) {  
        ReviewTest stuReview = new ReviewTest();
        model.addAttribute("review", stuReview);
	    return "CM0007_WriteReviewStudent";
	}
    @PostMapping(value="/update-student-review")
    private String postStudentReviewForm( @Valid @ModelAttribute("review") ReviewDTO inputStuReview, BindingResult bindingResult, Model model) {
	return "CM0008_WriteReviewConfirmStudent";
	}
    //get teacher review 
    @GetMapping(value="/teacher-review/{courseName}/{userName}")
    private String getTeacherReviewForm(@PathVariable String courseName, @PathVariable String userName,Model model) {
        ReviewTest trReview = new ReviewTest();
        model.addAttribute("review", trReview);
	return "CM0007_WriteReviewTeacher";
	}
    @PostMapping(value="/update-teacher-review")
    private String postTeacherReviewForm( @Valid @ModelAttribute("review") ReviewDTO inputTrReview, BindingResult bindingResult, Model model) { 
    return "CM0008_WriteReviewConfirmTeacher";
	}
    //admin edit review
    @GetMapping(value="/admin-student-review/{id}")
    private String adminGetStudentReviewForm(@PathVariable("id") Long reviewId,Model model) {
        ReviewTest review= reviewTestRepo.findById(reviewId).orElse(null);
        model.addAttribute("review", review);
	return "CM0007_AdminEditStudentReview.html";
	}
    @PostMapping("/edit-student-review") 
    public String helperTaskPost(@RequestParam("reviewId") Long reviewId, @Valid @ModelAttribute("review") ReviewTest reviewInfo, BindingResult result, Model model,RedirectAttributes redirectAttr) { 
    ReviewTest review= reviewTestRepo.findById(reviewId).orElse(null);
    review.setStar(reviewInfo.getStar());
    review.setFeedback(reviewInfo.getFeedback());
    reviewTestRepo.save(review);
    return "redirect:/student-review?reviewId="+reviewId;
   
   }
}
    

