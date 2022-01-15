package com.blissstock.mappingSite.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.blissstock.mappingSite.entity.CourseInfo;
import com.blissstock.mappingSite.entity.CourseTime;
import com.blissstock.mappingSite.entity.JoinCourseUser;
import com.blissstock.mappingSite.entity.Test;
import com.blissstock.mappingSite.entity.UserAccount;
import com.blissstock.mappingSite.entity.UserInfo;
import com.blissstock.mappingSite.enums.UserRole;
import com.blissstock.mappingSite.repository.CourseInfoRepository;
import com.blissstock.mappingSite.repository.JoinCourseUserRepository;
import com.blissstock.mappingSite.repository.UserAccountRepository;
import com.blissstock.mappingSite.repository.UserInfoRepository;
import com.blissstock.mappingSite.service.UserService;
import com.blissstock.mappingSite.service.UserSessionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import ch.qos.logback.core.recovery.ResilientSyslogOutputStream;
import lombok.experimental.Helper;
import net.bytebuddy.agent.builder.AgentBuilder.RedefinitionStrategy.Listener;

@Controller
public class CourseDetailsController {
    
    @Autowired
    UserSessionService userSessionService;

    @Autowired
    UserService userService;

    @Autowired
    CourseInfoRepository courseInfoRepository;

    @Autowired 
    UserInfoRepository userInfoRepository;

    @Autowired
    UserAccountRepository userAccountRepository;

    @Autowired
    JoinCourseUserRepository joinCourseUserRepository;

    @GetMapping(
        value =  {"/student/course-details/{courseId}", "/teacher/course-details/{courseId}", "/admin/course-details/{courseId}"}
    )
    private String getCourseDetails(@PathVariable Long courseId, Model model){
        Long userId;

        CourseInfo courseInfo = courseInfoRepository.findById(courseId).get();
        model.addAttribute("courseInfo", courseInfo);

        List<CourseTime> courseTimeList = courseInfo.getCourseTime();
        model.addAttribute("courseTimeList", courseTimeList);

         

        if(userSessionService.getRole()== UserRole.TEACHER){
            userId = userSessionService.getUserAccount().getAccountId();
            model.addAttribute("teacher", "TEACHER");
            model.addAttribute("classlink", courseInfo.getClassLink());

        }
        else if(userSessionService.getRole()== UserRole.STUDENT){
            model.addAttribute("student", "STUDENT");
        }
        else if(userSessionService.getRole()== UserRole.ADMIN){

            Integer maxStudent = courseInfo.getMaxStu();
            model.addAttribute("admin", "ADMIN");
            model.addAttribute("classlink", courseInfo.getClassLink());

            List<UserInfo> studentList = new ArrayList<>();
            for(JoinCourseUser joinCourseUser: courseInfo.getJoin()){
                studentList.add(joinCourseUser.getUserInfo());
            }
            model.addAttribute("studentList", studentList);
            Integer stuListSize = studentList.size();
            Integer availableStuList = maxStudent - stuListSize;
            model.addAttribute("availableStuList", availableStuList);



        }
        return "CM0003_CourseDetails";
    }

    @PostMapping(
        value = {"/teacher/course-details/insert-class-link", "/admin/course-details/insert-class-link"}
    )
    private String courseDetailsLink(@ModelAttribute("class-link") String classLink,  @ModelAttribute("courseId") Long courseId, @ModelAttribute("roleLink") String roleLink,Model model){
        CourseInfo courseInfo = courseInfoRepository.findById(courseId).get();
        courseInfo.setClassLink(classLink);
        courseInfoRepository.save(courseInfo);
        return "redirect:/" + roleLink  + "/course-details/" + courseId;
    }

    @PostMapping("/admin/course-details/insert-test-link")
    private String courseTestLink(@ModelAttribute("test-link") String testLink, @ModelAttribute("courseId") Long courseId, Model model){
        CourseInfo courseInfo = courseInfoRepository.findById(courseId).get();
        List<Test> testList = courseInfo.getTest();
        Test test = new Test();
        test.setTestLink(testLink);
        test.setCourseInfo(courseInfo);
        testList.add(test);
        courseInfo.setTest(testList);
        courseInfoRepository.save(courseInfo);
        

        return "redirect:/admin/course-details/" + courseId; 
    }

    // @GetMapping("/admin/hello")
    // private String helloWorld(){
    //     Long myId = (long) 1;
    //     CourseInfo courseInfo = courseInfoRepository.findById(myId).get();
    //     System.out.println("BlaBla" + courseInfo.getTest().size());
    //     return "hello";
    }


}
