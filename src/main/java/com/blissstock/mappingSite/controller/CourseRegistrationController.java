package com.blissstock.mappingSite.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.blissstock.mappingSite.entity.Content;
import com.blissstock.mappingSite.entity.CourseInfo;
import com.blissstock.mappingSite.entity.CourseTime;
import com.blissstock.mappingSite.entity.JoinCourseUser;
import com.blissstock.mappingSite.entity.Syllabus;
import com.blissstock.mappingSite.enums.UserRole;
import com.blissstock.mappingSite.exceptions.UnauthorizedFileAccessException;
import com.blissstock.mappingSite.model.FileInfo;
import com.blissstock.mappingSite.repository.CourseInfoRepository;
// import com.blissstock.mappingSite.repository.CourseRepository;
import com.blissstock.mappingSite.repository.CourseTimeRepository;
import com.blissstock.mappingSite.repository.JoinCourseUserRepository;
import com.blissstock.mappingSite.repository.UserInfoRepository;
import com.blissstock.mappingSite.service.UserSessionServiceImpl;
import com.blissstock.mappingSite.utils.CheckUploadFileType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.blissstock.mappingSite.service.MailService;
import com.blissstock.mappingSite.service.StorageService;
import com.blissstock.mappingSite.service.StorageServiceImpl;

@Controller

public class CourseRegistrationController {

    private static final Logger logger = LoggerFactory.getLogger(CourseRegistrationController.class);

    // @Autowired
    // private CourseRepository courseRepo;
    @Autowired
    MailService mailService;
    @Autowired
    private CourseInfoRepository courseInfoRepo;

    @Autowired
    JoinCourseUserRepository joinRepo;

    @Autowired
    private CourseTimeRepository courseTimeRepo;

    @Autowired
    private UserSessionServiceImpl userSessionService;

    @Autowired
    private UserInfoRepository userInfoRepository;

    private List<CourseTime> ctList = new ArrayList<>();

    @Autowired
    StorageService storageService;

    @Autowired
    public CourseRegistrationController(StorageService storageService) {
      this.storageService = storageService;
    }
    @RequestMapping(value = { "/teacher/course-registration", "/admin/course-registration/{id}" })
    private String courseRegistration(Model model, @PathVariable(required = false) Long id) {
        logger.info("GET Requested");

        CourseInfo courseInfo = new CourseInfo();

        List<CourseTime> courseTimeList = new ArrayList<>(7);
        courseInfo.setCourseTime(courseTimeList);

        UserRole role = userSessionService.getRole();
        if (role == UserRole.ADMIN || role == UserRole.SUPER_ADMIN) {
            long uid = id;
            courseInfo.setUid(uid);
            logger.info("Admin ID for course registration {}", uid);

        } else {
            long uid = userSessionService.getId();
            courseInfo.setUid(uid);
            logger.info("Teacher ID for course registration {}", uid);

        }

        // TODO WHY

        //courseInfo.setClassType("VIDEO");

        model.addAttribute("course", courseInfo);

        FileInfo cphoto = storageService.loadCoursePhoto(courseInfo);
        model.addAttribute("cphoto", cphoto);

        if (role == UserRole.TEACHER) {
            model.addAttribute("postAction", "/teacher/save-course-register");
        } else {
            model.addAttribute("postAction", "/admin/save-course-register");
        }

        return "AT0001_CourseRegistration";
    }

    @PostMapping(value = { "/teacher/courseregister-confirm", "/admin/courseregister-confirm" })
    private String courseRegistrationConfirm(@ModelAttribute("course") CourseInfo course,
            @RequestParam("course_pic") MultipartFile cphoto,
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

            Model model) {
        logger.info("POST requested");

        List<CourseTime> courseTimeList = new ArrayList<>();
        CourseTime courseTime0 = new CourseTime();
        CourseTime courseTime1 = new CourseTime();
        CourseTime courseTime2 = new CourseTime();
        CourseTime courseTime3 = new CourseTime();
        CourseTime courseTime4 = new CourseTime();
        CourseTime courseTime5 = new CourseTime();
        CourseTime courseTime6 = new CourseTime();

        if (course.getClassType().toLowerCase().equals("live")) {

            model.addAttribute("classActiveLive", true);

            if (!day0.equals("")) {
                courseTime0.setCourseDays(day0);
                courseTime0.setCourseStartTime(startTime0);
                courseTime0.setCourseEndTime(endTime0);
                courseTimeList.add(courseTime0);
            }
            if (!day1.equals("")) {
                courseTime1.setCourseDays(day1);
                courseTime1.setCourseStartTime(startTime1);
                courseTime1.setCourseEndTime(endTime1);
                courseTimeList.add(courseTime1);
            }
            if (!day2.equals("")) {
                courseTime2.setCourseDays(day2);
                courseTime2.setCourseStartTime(startTime2);
                courseTime2.setCourseEndTime(endTime2);
                courseTimeList.add(courseTime2);
            }
            if (!day3.equals("")) {
                courseTime3.setCourseDays(day3);
                courseTime3.setCourseStartTime(startTime3);
                courseTime3.setCourseEndTime(endTime3);
                courseTimeList.add(courseTime3);
            }
            if (!day4.equals("")) {
                courseTime4.setCourseDays(day4);
                courseTime4.setCourseStartTime(startTime4);
                courseTime4.setCourseEndTime(endTime4);
                courseTimeList.add(courseTime4);
            }
            if (!day5.equals("")) {
                courseTime5.setCourseDays(day5);
                courseTime5.setCourseStartTime(startTime5);
                courseTime5.setCourseEndTime(endTime5);
                courseTimeList.add(courseTime5);
            }
            if (!day6.equals("")) {
                courseTime6.setCourseDays(day6);
                courseTime6.setCourseStartTime(startTime6);
                courseTime6.setCourseEndTime(endTime6);
                courseTimeList.add(courseTime6);
            }

            model.addAttribute("courseTimeList", courseTimeList);
            ctList = courseTimeList;

        } else {
            model.addAttribute("classActiveVideo", true);
        }

        model.addAttribute("course", course);

        if (!cphoto.isEmpty() && CheckUploadFileType.checkType(cphoto)) {
            // get original photo name and generate a new file name
            String originalFileName = StringUtils.cleanPath(
                cphoto.getOriginalFilename());
            try {
              storageService.store(course.getCourseId(), cphoto, StorageServiceImpl.COURSE_PATH, true);
            } catch (UnauthorizedFileAccessException e) {
              e.printStackTrace();
            }
            // insert photo
            course.setCoursePhoto(originalFileName);
    
            logger.info("profile photo {} stored", originalFileName);
            //return  "redirect:/teacher/course-registration";
          }

        UserRole role = userSessionService.getRole();

        if (role == UserRole.TEACHER) {
            model.addAttribute("postAction", "/teacher/save-course-register");

        } else {
            model.addAttribute("postAction", "/admin/save-course-register");

        }

        // System.out.println("Heehee" + day0 + " " + startTime0 + " " + endTime0 + " "
        // + day1 + " " + startTime1 + " " + endTime1);
        // System.out.println("Haahaa " + courseTimeList.get(0).getCourseDays() +
        // courseTimeList.size());

        return "AT0002_CourseRegistrationConfirm";
    }

    @PostMapping(value = { "/teacher/save-course-register", "/admin/save-course-register" })
    private String saveCourseRegister(HttpServletRequest request, @ModelAttribute("course") CourseInfo course,@RequestParam("course_pic") MultipartFile cphoto,HttpServletRequest httpServletRequest) {
        // course.setUserInfo(userInfoRepository.findById(userSessionService.getId()).get());
        course.setUserInfo(userInfoRepository.findById(course.getUid()).get());
        logger.info("Post Requested");
        UserRole role = userSessionService.getRole();
        if (role == UserRole.ADMIN || role == UserRole.SUPER_ADMIN) {
            course.setIsCourseApproved(true);
        } else {
            course.setIsCourseApproved(false);
        }
            
        CourseInfo savedCourse=courseInfoRepo.save(course);

        if (!cphoto.isEmpty() && CheckUploadFileType.checkType(cphoto)) {
            // get original photo name and generate a new file name
            String originalFileName = StringUtils.cleanPath(
                cphoto.getOriginalFilename());
            try {
              storageService.store(savedCourse.getCourseId(), cphoto, StorageServiceImpl.COURSE_PATH, true);
            } catch (UnauthorizedFileAccessException e) {
              e.printStackTrace();
            }
            // insert photo
            course.setCoursePhoto(originalFileName);
            courseInfoRepo.save(course);
    
            logger.info("profile photo {} stored", originalFileName);
            //return  "redirect:/teacher/course-registration";
          }

        JoinCourseUser joins = new JoinCourseUser();
        joins.setCourseInfo(course);
        joins.setUserInfo(userInfoRepository.findById(course.getUid()).get());
        joinRepo.save(joins);

        // course.setJoin(join);

        System.out.println("HoeHoe" + ctList);
        for (CourseTime courseTime : ctList) {
            // set to upper case
            course.setClassType(course.getClassType().toUpperCase());
            courseTime.setCourseInfo(course);
            courseTimeRepo.save(courseTime);
        }

        // UserRole role = userSessionService.getRole();

        if (role == UserRole.TEACHER) {
            try {

                new Thread(new Runnable() {
                    public void run() {
                        try {

                            String appUrl = request.getServerName() + // "localhost"
                                    ":" +
                                    request.getServerPort(); // "8080"

                            mailService.SendAdminNewCourseByTeacher(course, appUrl);
                            mailService.SendTeacherNewCourseByTeacher(course, appUrl);

                        } catch (Exception e) {
                            logger.info(e.toString());
                        }
                    }
                }).start();
            } catch (Exception e) {
                logger.info(e.toString());
            }
            return "redirect:/teacher/course-upload/complete";
        } else {
            return "redirect:/admin/course-upload/complete";
        }
        // return "takealeave";
    }

    // @RequestMapping("/admin/course")
    // public String courseTest()
    // {
    // return "card";
    // }

}
