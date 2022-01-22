package com.blissstock.mappingSite.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.blissstock.mappingSite.entity.CourseInfo;
import com.blissstock.mappingSite.entity.CourseTime;
import com.blissstock.mappingSite.entity.JoinCourseUser;
import com.blissstock.mappingSite.entity.Syllabus;
import com.blissstock.mappingSite.entity.Test;
import com.blissstock.mappingSite.entity.UserInfo;
import com.blissstock.mappingSite.enums.UserRole;
import com.blissstock.mappingSite.exceptions.CourseNotFoundException;
import com.blissstock.mappingSite.repository.CourseInfoRepository;
import com.blissstock.mappingSite.repository.JoinCourseUserRepository;
import com.blissstock.mappingSite.repository.SyllabusRepository;
import com.blissstock.mappingSite.repository.UserAccountRepository;
import com.blissstock.mappingSite.repository.UserInfoRepository;
import com.blissstock.mappingSite.service.CourseService;
import com.blissstock.mappingSite.service.JoinCourseService;
import com.blissstock.mappingSite.service.UserService;
import com.blissstock.mappingSite.service.UserSessionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class CourseDetailsController {

    private static final Logger logger = LoggerFactory.getLogger(CourseDetailsController.class);

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

    @Autowired
    SyllabusRepository syllabusRepository;

    @Autowired
    JoinCourseService joinCourseService;

    @Autowired
    CourseService courseService;

    @GetMapping(value = { "/student/course-details/{courseId}", "/teacher/course-details/{courseId}",
            "/admin/course-details/{courseId}", "/guest/course-detail/{courseId}" })
    private String getCourseDetails(@PathVariable Long courseId, Model model) {
        Long userId;

        // Get course by ID
        CourseInfo courseInfo = courseInfoRepository.findById(courseId).get();
        model.addAttribute("courseInfo", courseInfo);

        String classType = courseInfo.getClassType();
        boolean isLiveClass = classType.equals("LIVE");
        model.addAttribute("isLiveClass", isLiveClass);

        // Get Time segments for course
        List<CourseTime> courseTimeList = courseInfo.getCourseTime();
        model.addAttribute("courseTimeList", courseTimeList);

        // Get syllabus
        List<Syllabus> syllabusList = courseInfo.getSyllabus();
        model.addAttribute("syllabusList", syllabusList);

        // Get the remaining number of students who can join course
        Integer maxStudent = courseInfo.getMaxStu();
        List<UserInfo> studentList = new ArrayList<>();
        for (JoinCourseUser joinCourseUser : courseInfo.getJoin()) {
            studentList.add(joinCourseUser.getUserInfo());
        }
        Integer stuListSize = studentList.size();
        Integer availableStuList;
        try {
            availableStuList = maxStudent - stuListSize;
        } catch (NullPointerException e) {
            availableStuList = 0;
        }
        Integer currentAttending = studentList.size();
        model.addAttribute("currentAttending", currentAttending);
        model.addAttribute("availableStuList", availableStuList);

        if (userSessionService.getRole() == UserRole.TEACHER) {
            userId = userSessionService.getUserAccount().getAccountId();
            logger.info("The user id is {} ", userId);
            Long registered = courseInfo.getUserInfo().getUid();
            logger.info("The teacher id is {} ", userId);
            boolean teacherRegistered = false;
            if (userId.equals(registered)) {
                teacherRegistered = true;
            }
            model.addAttribute("teacherRegistered", teacherRegistered);
            model.addAttribute("teacher", "TEACHER");
            model.addAttribute("classlink", courseInfo.getClassLink());

        } else if (userSessionService.getRole() == UserRole.STUDENT) {
            userId = userSessionService.getUserAccount().getAccountId();
            boolean studentRegistered = true;

            List<JoinCourseUser> join = joinCourseService.getJoinCourseUser(userId, courseId);
            studentRegistered = join != null && !join.isEmpty();

            logger.info("The boolean value for student registered is {} ", studentRegistered);
            boolean studentNotRegistered = !studentRegistered;
            model.addAttribute("studentNotRegistered", studentNotRegistered);
            model.addAttribute("studentRegistered", studentRegistered);
            model.addAttribute("student", "STUDENT");
        }

        else if (userSessionService.getRole() == UserRole.ADMIN
                || userSessionService.getRole() == UserRole.SUPER_ADMIN) {

            model.addAttribute("admin", "ADMIN");
            model.addAttribute("classlink", courseInfo.getClassLink());
            model.addAttribute("studentList", studentList);

        } else if (userSessionService.getRole() == UserRole.GUEST_USER) {
            model.addAttribute("guest", "GUEST");
        }

        return "CM0003_CourseDetails";
    }

    @PostMapping(value = { "/teacher/course-details/insert-class-link", "/admin/course-details/insert-class-link" })
    private String courseDetailsLink(@ModelAttribute("class-link") String classLink,
            @ModelAttribute("courseId") Long courseId, @ModelAttribute("roleLink") String roleLink, Model model) {
        CourseInfo courseInfo = courseInfoRepository.findById(courseId).get();
        courseInfo.setClassLink(classLink);
        courseInfoRepository.save(courseInfo);
        return "redirect:/" + roleLink + "/course-details/" + courseId;
    }

    @PostMapping("/admin/course-details/insert-test-link")
    private String courseTestLink(@ModelAttribute("test-link") String testLink,
            @ModelAttribute("courseId") Long courseId, Model model) {
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
    // Long myId = (long) 1;
    // CourseInfo courseInfo = courseInfoRepository.findById(myId).get();
    // System.out.println("BlaBla" + courseInfo.getTest().size());
    // return "hello";
    // }

    @DeleteMapping("/admin/course-details/delete/")
    public ResponseEntity<Object> deleteCourse(
            Model model,
            Long courseId,
            HttpServletRequest httpServletRequest) {
        logger.info("DELETE Request for course {}", courseId);
        try {
            // UserInfo userInfo = userService.getUserInfoByID(uid);
            CourseInfo courseInfo = courseInfoRepository.findById(courseId).get();
            if (courseInfo == null) {
                throw new CourseNotFoundException();
            }
            courseService.deleteCourseInfo(courseInfo);
        } catch (CourseNotFoundException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Course Not Found");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something went wrong");
        }

        return ResponseEntity.status(HttpStatus.OK).body("operation success");

    }

}
