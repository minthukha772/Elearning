 package com.blissstock.mappingSite.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import com.blissstock.mappingSite.entity.CourseInfo;
import com.blissstock.mappingSite.entity.JoinCourseUser;
import com.blissstock.mappingSite.entity.Review;
import com.blissstock.mappingSite.entity.UserAccount;
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
    "/admin/review-list/{courseId}" 
    })
    private String getReviewList(@PathVariable Long courseId, Model model) {  
        model.addAttribute("courseId",courseId);
        Long userId = userSessionService.getUserAccount().getId();
        
        CourseInfo courseInfo=courseInfoRepo.findById(courseId).orElse(null);
        UserInfo user=userRepo.findById(userId).orElse(null);
        //Display course name
        String courseName=courseInfo.getCourseName();
        model.addAttribute("courseName", courseName);

        //Add user name and review to list
        List<JoinCourseUser> joinList=courseInfo.getJoin();
        List<UserInfo> userInfoList= new ArrayList<UserInfo>(); 
        List<Review> reviewList=new ArrayList<Review>(); 
        for(JoinCourseUser join:joinList){
            userInfoList.add(join.getUserInfo());
            reviewList.addAll(join.getReview());
        }
        //Display user names
        for(UserInfo userInfo:userInfoList){
            String userRole =  userInfo.getUserAccount().getRole();
            if(userRole.equals(UserRole.TEACHER)){
                String trName=userInfo.getUserName();
                model.addAttribute("trName", trName);
            } 
            else if(userRole.equals(UserRole.STUDENT)){
                String stuName=userInfo.getUserName();
                model.addAttribute("stuName", stuName);
            }
        }

        //Display reviews        
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
            stuReviews.addAll(join.getReview());
        }
        //List<Review> stuReviews=user.getReview();
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

            return "CM0009_ReviewList";
	}
}
 