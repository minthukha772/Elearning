package com.blissstock.mappingSite.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;


import com.blissstock.mappingSite.dto.CourseTestingDTO;
import com.blissstock.mappingSite.entity.CourseInfo;
import com.blissstock.mappingSite.entity.CourseTesting;
import com.blissstock.mappingSite.entity.LeaveInfo;
//import com.blissstock.mappingSite.entity.UserInfo;
import com.blissstock.mappingSite.repository.ContentRepository;
import com.blissstock.mappingSite.repository.CourseRepository;
import com.blissstock.mappingSite.repository.CourseTestingRepository;
import com.blissstock.mappingSite.repository.CourseTimeRepository;
import com.blissstock.mappingSite.repository.LeaveInfoRepository;
import com.blissstock.mappingSite.repository.SyllabusRepository;
import com.blissstock.mappingSite.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
@Controller
public class CourseTestingController {
    
    @Autowired
    UserRepository userRepo;

    @Autowired
    CourseTimeRepository courseIimeRepo;

    @Autowired
    SyllabusRepository syllabusRepo;

    @Autowired
    ContentRepository contentRepo;

    @Autowired
    CourseTestingRepository courseTestRepo;

    @Autowired
    CourseRepository courseRepo;

    @Autowired
    LeaveInfoRepository leaveRepo;
    
/*
    @Valid
    @GetMapping(value="/course-register")
    private String getCourseInfoForm(Model model) {
		CourseTestingDTO courseInfo = new CourseTestingDTO();
        model.addAttribute("course", courseInfo);
        return "AT00001_CourseRegisteration";
    }

    @PostMapping(value="/courseregister-confirm")
	public String postCourseInfoForm( @Valid @ModelAttribute("course") CourseTestingDTO inputCourseInfo, BindingResult bindingResult, Model model) {
	//if(bindingResult.hasErrors()) {
     //return "AT00001_CourseRegisteration";
        //}
        return "AT00002_CourseRegisterationConfirm";
		//save-course-register
	}*/
     
  
  
  /*Leave Save Controller*/ 



    /*@PostMapping("/save-leave-request")
  public String saveCourseInfoForm(
    Model model,
    @Valid @ModelAttribute("leave") LeaveInfo leaveInfo,
    BindingResult bindingResult,
    HttpServletRequest request
  ) {
   LeaveInfo saveCourse = new LeaveInfo(null, leaveInfo.getLeaveStartDate(),leaveInfo.getLeaveEndDate(), leaveInfo.getLeaveStartTime(), leaveInfo.getLeaveEndTime(), leaveInfo.getReason(), leaveInfo.getUserInfo(), leaveInfo.getCourseInfo());{
   
   leaveRepo.save(saveCourse);
    return "LeaveSuccess";
  } 1.12.2021
  */

  /*Leave Save Controller*/ 



//teacher edit course information
 /* @GetMapping(value="/edit-courseinfo/{course_id}")
    private String editGetCourseInfoForm(@PathVariable("course_id") Long courseId,Model model) {
        CourseTesting course= courseTestRepo.findById(courseId).orElse(null);
        model.addAttribute("course", course);
	return "AT00001_CourseRegisteration";
	}

  @PostMapping("/edit-courseinfo-post") 
    public String editCourseInfoPost(@RequestParam("courseId") Long courseId, @Valid @ModelAttribute("course") CourseTesting courseInfo, BindingResult result, Model model,RedirectAttributes redirectAttr) { 
    CourseTesting course= courseTestRepo.findById(courseId).orElse(null);
    course.setCourseName(courseInfo.getCourseName());
    course.setClassType(courseInfo.getClassType());
    course.setCategory(courseInfo.getCategory());
    course.setLevel(courseInfo.getLevel());
    course.setAboutCourse(courseInfo.getAboutCourse());
    course.setClassLink(courseInfo.getClassLink());
    course.setFees(courseInfo.getFees());
    courseTestRepo.save(course);
    return "redirect:/course-register?courseId="+courseId;
   
   }*/

  }
