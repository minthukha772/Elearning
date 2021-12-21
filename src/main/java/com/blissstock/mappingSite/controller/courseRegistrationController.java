package com.blissstock.mappingSite.controller;

import java.util.ArrayList;
import java.util.List;

import com.blissstock.mappingSite.entity.Content;
import com.blissstock.mappingSite.entity.CourseInfo;
import com.blissstock.mappingSite.entity.CourseTime;
import com.blissstock.mappingSite.entity.Syllabus;
import com.blissstock.mappingSite.repository.CourseRepository;
import com.blissstock.mappingSite.repository.CourseTimeRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller

public class courseRegistrationController {

    @Autowired
    private CourseRepository courseRepo;

    @Autowired
    private CourseTimeRepository courseTimeRepo;

    private List<CourseTime> ctList = new ArrayList<>(); 
    

    @RequestMapping("/teacher/course-registration")
    private String courseRegistration(Model model ){
        CourseInfo courseInfo = new CourseInfo();
       
        // List<CourseTime> courseTimeList = new ArrayList<>(7);  
        // courseInfo.setCourseTime(courseTimeList);
        
        model.addAttribute("course", courseInfo);
  
        return "AT00001_CourseRegisteration";
    }
    
    @PostMapping("/courseregister-confirm")
    private String courseRegistrationConfirm(@ModelAttribute("course") CourseInfo course,
                                            @ModelAttribute("day0") String day0,
                                            @ModelAttribute("startTime0") String startTime0,
                                            @ModelAttribute("endTime0") String endTime0,
                                            @ModelAttribute("day1") String day1,
                                            @ModelAttribute("startTime1") String startTime1,
                                            @ModelAttribute("endTime1") String endTime1,
                                            @ModelAttribute("day2") String day2,
                                            @ModelAttribute("startTime2") String startTime2,
                                            @ModelAttribute("endTime2") String endTime2,
                                            @ModelAttribute("day3") String day3,
                                            @ModelAttribute("startTime3") String startTime3,
                                            @ModelAttribute("endTime3") String endTime3,
                                            @ModelAttribute("day4") String day4,
                                            @ModelAttribute("startTime4") String startTime4,
                                            @ModelAttribute("endTime4") String endTime4,
                                            @ModelAttribute("day5") String day5,
                                            @ModelAttribute("startTime5") String startTime5,
                                            @ModelAttribute("endTime5") String endTime5,
                                            @ModelAttribute("day6") String day6,
                                            @ModelAttribute("startTime6") String startTime6,
                                            @ModelAttribute("endTime6") String endTime6,
                                            Model model){
        List<CourseTime> courseTimeList = new ArrayList<>();
        CourseTime courseTime0 = new CourseTime();
        CourseTime courseTime1 = new CourseTime();
        CourseTime courseTime2 = new CourseTime();
        CourseTime courseTime3 = new CourseTime();
        CourseTime courseTime4 = new CourseTime();
        CourseTime courseTime5 = new CourseTime();
        CourseTime courseTime6 = new CourseTime();
        if(course.getClassType().equals("live")){

            model.addAttribute("classActiveLive", true);
            
            if(!day0.equals("")){
                courseTime0.setCourseDays(day0);
                courseTime0.setCourseStartTime(startTime0);
                courseTime0.setCourseEndTime(endTime0);
                courseTimeList.add(courseTime0);
            }
            if(!day1.equals("")){
                courseTime1.setCourseDays(day1);
                courseTime1.setCourseStartTime(startTime1);
                courseTime1.setCourseEndTime(endTime1);
                courseTimeList.add(courseTime1);
            }
            if(!day2.equals("")){
                courseTime2.setCourseDays(day2);
                courseTime2.setCourseStartTime(startTime2);
                courseTime2.setCourseEndTime(endTime2);
                courseTimeList.add(courseTime2);
            }
            if(!day3.equals("")){
                courseTime3.setCourseDays(day3);
                courseTime3.setCourseStartTime(startTime3);
                courseTime3.setCourseEndTime(endTime3);
                courseTimeList.add(courseTime3);
            }
            if(!day4.equals("")){
                courseTime4.setCourseDays(day4);
                courseTime4.setCourseStartTime(startTime4);
                courseTime4.setCourseEndTime(endTime4);
                courseTimeList.add(courseTime4);
            }
            if(!day5.equals("")){
                courseTime5.setCourseDays(day5);
                courseTime5.setCourseStartTime(startTime5);
                courseTime5.setCourseEndTime(endTime5);
                courseTimeList.add(courseTime5);
            }
            if(!day6.equals("")){
                courseTime6.setCourseDays(day6);
                courseTime6.setCourseStartTime(startTime6);
                courseTime6.setCourseEndTime(endTime6);
                courseTimeList.add(courseTime6);
            }

            model.addAttribute("courseTimeList", courseTimeList);
            ctList = courseTimeList;
            System.out.println("Heehee" + ctList);
            
        }else {
            model.addAttribute("classActiveVideo", true);
        }
        
        model.addAttribute("course", course);
        
        // System.out.println("Heehee" + day0 + " " + startTime0 + " " + endTime0 + " " + day1 + " " + startTime1 + " " + endTime1);
        // System.out.println("Haahaa " + courseTimeList.get(0).getCourseDays() + courseTimeList.size());
        
        return "AT00002_CourseRegisterationConfirm";
    }

    @PostMapping("/save-course-register")
    private String saveCourseRegister(@ModelAttribute("course") CourseInfo course){
        
        //course.setIsCourseApproved("true");
        courseRepo.save(course);
        System.out.println("HoeHoe" + ctList);
        for(CourseTime courseTime : ctList){
            courseTime.setCourseInfo(course);
            courseTimeRepo.save(courseTime);
        }
        
        return "takealeave";
    }

    
}