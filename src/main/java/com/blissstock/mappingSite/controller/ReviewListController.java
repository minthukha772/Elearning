package com.blissstock.mappingSite.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import com.blissstock.mappingSite.entity.CourseInfo;
import com.blissstock.mappingSite.entity.JoinCourseUser;
import com.blissstock.mappingSite.entity.Review;
import com.blissstock.mappingSite.entity.UserInfo;
import com.blissstock.mappingSite.repository.CourseInfoRepository;
import com.blissstock.mappingSite.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ReviewListController {

    @Autowired
    CourseInfoRepository courseInfoRepo;

    @Autowired
    UserRepository userRepo;
    //get student review 
    @Valid
    @GetMapping(value="/{role}/review-list/{courseId}/{userId}")
    private String getReviewList(@PathVariable Long courseId, @PathVariable Long userId, @PathVariable String role, Model model) {  
        CourseInfo courseInfo=courseInfoRepo.findById(courseId).orElse(null);
        UserInfo user=userRepo.findById(userId).orElse(null);
        //Display course name
        String courseName=courseInfo.getCourseName();
        model.addAttribute("courseName", courseName);

        //Display teacher name
        List<JoinCourseUser> joinList=courseInfo.getJoin();
        List<UserInfo> userInfoList= new ArrayList<UserInfo>(); 
        for(JoinCourseUser jUser:joinList){
            userInfoList.add(jUser.getUserInfo());
        }
        
        for(UserInfo userInfo:userInfoList){
            String userRole =  userInfo.getUserAccount().getRole();
            if(userRole.equals("teacher")){
                String trName=userInfo.getUserName();
                model.addAttribute("trName", trName);
            } 
            else if(userRole.equals("student")){
                String stuName=userInfo.getUserName();
                model.addAttribute("stuName", stuName);
            }
        }

        //Display review
        List<Review> reviews=courseInfo.getReview();
        List<Review> courseReviewList= new ArrayList<Review>(); 
        for (Review courseReview:reviews){
            if(courseReview.getReviewType()==0){
                courseReviewList.add(courseReview); 
                model.addAttribute("courseReviewList", courseReviewList);
            }
        }

        List<Review> stuReviews=user.getReview();
        List<Review> studentReviewList= new ArrayList<Review>();
        for(Review studentReview:stuReviews) {
            if(studentReview.getStar()==0){
                studentReviewList.add(studentReview);
                model.addAttribute("stuReviews", studentReviewList);
            }
        }
        // for(Review review : reviews){
        //     model.addAttribute("review", review);  
        // }
        // UserInfo userInfo=userRepo.findByUserName(userName);
        // //Long userId=userInfo.getUid();
        // UserAccount account=userInfo.getUserAccount();
        // String role = account.getRole();

        if(role.equals("teacher")){
            return "CM0009_ReviewList";
        } else if (role.equals("admin")){
            return "CM0009_ReviewListAdmin";
        }

            return "CM0009_ReviewListStudent";
	}
}
