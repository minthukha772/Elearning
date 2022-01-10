 package com.blissstock.mappingSite.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import com.blissstock.mappingSite.entity.CourseInfo;
import com.blissstock.mappingSite.entity.JoinCourseUser;
import com.blissstock.mappingSite.entity.Review;
import com.blissstock.mappingSite.entity.UserInfo;
import com.blissstock.mappingSite.enums.UserRole;
import com.blissstock.mappingSite.repository.CourseInfoRepository;
import com.blissstock.mappingSite.repository.UserRepository;
import com.blissstock.mappingSite.service.UserService;
import com.blissstock.mappingSite.service.UserSessionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ReviewListController {

    @Autowired
    UserSessionService userSessionService;

    @Autowired
    UserService userService;
    
    @Autowired
    CourseInfoRepository courseInfoRepo;

    @Autowired
    UserRepository userRepo;
    //get student review 
    @Valid
    @GetMapping(value={"/teacher/review-list/{courseId}",
    "/student/review-list/{courseId}",
    "/admin/review-list/{courseId}/{id}" 
    })
    private String getReviewList(@PathVariable Long courseId,@PathVariable(name = "id", required = false) Long id, Model model) {  
        Long userId = id==null ? userSessionService.getUserAccount().getAccountId(): id;

         
        CourseInfo courseInfo=courseInfoRepo.findById(courseId).orElse(null);
        UserInfo user=userRepo.findById(userId).orElse(null);
        //Display course name
        String courseName=courseInfo.getCourseName();
        model.addAttribute("courseName", courseName);


        //Get review list and teahcer name of course
        List<JoinCourseUser> joinList=courseInfo.getJoin();
        List<Review> reviewList=new ArrayList<Review>();
        for(JoinCourseUser join:joinList){
            reviewList.addAll(join.getReview());
            UserInfo joinUser= join.getUserInfo();
            if(joinUser.getUserAccount().getRole().equals("ROLE_TEACHER")){
                model.addAttribute("trName", joinUser.getUserName());
            } 
            else if(joinUser.getUserAccount().getAccountId().equals(userId)){
                if(joinUser.getUserAccount().getRole().equals("ROLE_STUDENT")){
                    model.addAttribute("stuRegistered", true);
                }
            } 
            
        }

        //Display course reviews        
        //List<Review> reviews=courseInfo.getReview();
        List<Review> courseReviewList= new ArrayList<Review>(); 
        for (Review courseReview:reviewList){
            if(courseReview.getReviewType()==0){
                courseReviewList.add(courseReview); 
                model.addAttribute("courseReviewList", courseReviewList);
            }
        }

        
        //Display student reviews
        List<JoinCourseUser> joinUserList=user.getJoin();
        List<Review> stuReviews=new ArrayList<Review>(); 
        for(JoinCourseUser join:joinUserList){
            if(join.getCourseInfo().getCourseId().equals(courseId)){
            stuReviews.addAll(join.getReview());
            }
        }
        List<Review> studentReviewList= new ArrayList<Review>();
        for(Review studentReview:stuReviews) {
            if(studentReview.getStar()==0){
                studentReviewList.add(studentReview);
                model.addAttribute("stuReviews", studentReviewList);
            }
        }

            return "CM0009_ReviewList";
	}
}
 