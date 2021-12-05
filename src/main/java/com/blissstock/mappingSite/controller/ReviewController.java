package com.blissstock.mappingSite.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.validation.Valid;

import com.blissstock.mappingSite.dto.StudentReviewDTO;
import com.blissstock.mappingSite.dto.TeacherReviewDTO;
import com.blissstock.mappingSite.entity.CourseInfo;
import com.blissstock.mappingSite.entity.Review;
import com.blissstock.mappingSite.entity.ReviewTest;
import com.blissstock.mappingSite.entity.UserInfo;
import com.blissstock.mappingSite.repository.CourseInfoRepository;
import com.blissstock.mappingSite.repository.ReviewRepository;
import com.blissstock.mappingSite.repository.ReviewTestRepository;
import com.blissstock.mappingSite.repository.UserRepository;
import com.blissstock.mappingSite.service.StudentReviewService;
import com.blissstock.mappingSite.service.TeacherReviewService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller

public class ReviewController {
    @Autowired
    UserRepository userRepo;
    
    @Autowired
    CourseInfoRepository courseInfoRepo;

    @Autowired
    ReviewRepository reviewRepo;

    @Autowired
    StudentReviewService stuReviewService;

    @Autowired
    TeacherReviewService trReviewService;
    //get student review 
    @Valid
    @GetMapping(value="/student-review/{courseId}/{userId}")
    private String getStudentReviewForm(@PathVariable Long courseId, @PathVariable Long userId, Model model) {  
        StudentReviewDTO stuReview = new StudentReviewDTO();
        model.addAttribute("review", stuReview);
        model.addAttribute("postAction", "/update-student-review/"+courseId+"/"+userId);
	    
        return "CM0007_WriteReviewStudent";

	}

    @PostMapping(value="/update-student-review/{courseId}/{userId}")
    private String postStudentReviewForm( @Valid @ModelAttribute("review") StudentReviewDTO stuReviewDTO, @PathVariable Long courseId, @PathVariable Long userId, BindingResult bindingResult, Model model, @RequestParam(value="action", required=true) String action) { 
        if(bindingResult.hasErrors()) {
			return "CM0007_WriteReviewStudent";			
		}
        CourseInfo course = courseInfoRepo.findById(courseId).orElse(null);
        UserInfo user = userRepo.findById(courseId).orElse(null);
        try{
            if(action.equals("submit")){
              stuReviewService.addReview(stuReviewDTO, course, user);
            }
          }catch(Exception e){
            System.out.println(e);
          }
        model.addAttribute("infoMap", stuReviewDTO.toMapReview());
        return "CM0007_WriteReviewStudent";
	}
    
    @Valid
    @GetMapping(value="/edit-student-review/{reviewId}")
    private String editStudentReviewForm(@PathVariable Long reviewId, Model model, final RedirectAttributes redirectAttributes) {  
        Review review=reviewRepo.findById(reviewId).orElse(null);
        model.addAttribute("review", review);
	    return "CM0007_WriteReviewStudent";
	}
    //get teacher review 
    @GetMapping(value="/teacher-review/{courseId}/{userId}")
    private String getTeacherReviewForm(@PathVariable Long courseId, @PathVariable Long userId, Model model) {
        TeacherReviewDTO trReview = new TeacherReviewDTO();
        model.addAttribute("review", trReview);
        model.addAttribute("postAction", "/update-teacher-review/"+courseId+"/"+userId);
	    return "CM0007_WriteReviewTeacher";
	}
    @PostMapping(value="/update-teacher-review/{courseId}/{userId}")
    private String postTeacherReviewForm( @Valid @ModelAttribute("review") TeacherReviewDTO trReviewDTO, BindingResult bindingResult,@PathVariable Long courseId, @PathVariable Long userId, Model model, @RequestParam(value="action", required=true) String action) { 
        if(bindingResult.hasErrors()) {
			return "CM0007_WriteReviewTeacher";			
		}
        CourseInfo course = courseInfoRepo.findById(courseId).orElse(null); 
        UserInfo user = userRepo.findById(courseId).orElse(null);
        try{
            if(action.equals("submit")){
              trReviewService.addReview(trReviewDTO, course, user);
            }
          }catch(Exception e){
            System.out.println(e);
          }
        model.addAttribute("infoMap", trReviewDTO.toMapTrReview());
        return "CM0007_WriteReviewTeacher";
	}

  //   @PostMapping(value="/save-teacher-review")
  //   private String saveTeacherReviewForm( @Valid @ModelAttribute("review") ReviewTest newTrReview, BindingResult bindingResult, Model model) {
  //       //ReviewTest saveTrReview = new ReviewTest(null, newTrReview.getReviewType(), 0,newTrReview.getFeedback(),newTrReview.getReviewStatus(), null, null);
  //       reviewTestRepo.save(newTrReview);
  //       return "CM0008_WriteReviewConfirmTeacher";
	// }

     //edit teacher review
     @Valid
     @GetMapping(value="/edit-teacher-review/{reviewId}")
     private String editTeacherReviewForm(@PathVariable Long reviewId, Model model, final RedirectAttributes redirectAttributes) {  
         Review review=reviewRepo.findById(reviewId).orElse(null);
         model.addAttribute("review", review);
         return "CM0007_WriteReviewTeacher";
     }
   
   }

    

