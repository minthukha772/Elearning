package com.blissstock.mappingSite.controller;

import javax.validation.Valid;

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
    @GetMapping(value="/student-review")
    private String getStudentReviewForm(Model model) {
        ReviewTest stuReview = new ReviewTest();
        model.addAttribute("review", stuReview);
	return "CM0007_WriteReviewStudent.html";
	}
    @PostMapping(value="/update-student-review")
    private String postStudentReviewForm( @Valid @ModelAttribute("review") ReviewTest inputStuReview, BindingResult bindingResult, Model model) {
        ReviewTest newStuReview = new ReviewTest(null, 0, inputStuReview.getStar(),inputStuReview.getFeedback(),inputStuReview.getReviewStatus());
        
        //model.addAttribute("review", inputReview); 
        reviewTestRepo.save(newStuReview);
	return "redirect:/student-review";
	}
    //get teacher review 
    @GetMapping(value="/teacher-review")
    private String getTeacherReviewForm(Model model) {
        ReviewTest trReview = new ReviewTest();
        model.addAttribute("review", trReview);
	return "CM0007_WriteReviewTeacher.html";
	}
    @PostMapping(value="/update-teacher-review")
    private String postTeacherReviewForm( @Valid @ModelAttribute("review") Review inputTrReview, BindingResult bindingResult, Model model) {
        ReviewTest newTrReview = new ReviewTest(null, 0, inputTrReview.getReviewType(),inputTrReview.getFeedback(),inputTrReview.getReviewStatus()); 
        reviewTestRepo.save(newTrReview);
	return "redirect:/teacher-review";
	}
    //admin edit review
    @PostMapping("/edit-student-review") 
    public String helperTaskPost(@RequestParam("reviewId") Long reviewId, @ModelAttribute @Valid Review reviewInfo, BindingResult result, Model model,RedirectAttributes redirectAttr) { 
    ReviewTest review= reviewTestRepo.findById(reviewId).orElse(null);
    review.setStar(reviewInfo.getStar());
    review.setFeedback(reviewInfo.getFeedback());
    reviewTestRepo.save(review);
    return "redirect:/student-review?reviewId="+reviewId;
   
   }
}
    

