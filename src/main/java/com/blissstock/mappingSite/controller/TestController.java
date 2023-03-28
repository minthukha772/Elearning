package com.blissstock.mappingSite.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import javax.validation.Valid;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;

import java.util.Date;
import com.blissstock.mappingSite.entity.CourseInfo;
import com.blissstock.mappingSite.entity.Test;
import com.blissstock.mappingSite.entity.UserInfo;
import com.blissstock.mappingSite.enums.UserRole;
import com.blissstock.mappingSite.repository.CourseInfoRepository;
import com.blissstock.mappingSite.repository.TestRepository;
import com.blissstock.mappingSite.repository.UserInfoRepository;
import com.blissstock.mappingSite.repository.UserRepository;
import com.blissstock.mappingSite.service.UserSessionService;

@Controller
public class TestController {

    @Autowired
    UserSessionService userSessionService;

    @Autowired
    TestRepository testRepository;

    @Autowired
    CourseInfoRepository courseInfoRepository;

    @Autowired
    UserInfoRepository userInfoRepository;

    @Autowired
    UserRepository userRepository;

    @Valid
    @GetMapping(value = { "/teacher/exam" })
    private String getExamManagementPage(Model model,
            @RequestParam(required = false) String examStatus, @RequestParam(required = false) String courseid,
            @RequestParam(required = false) String fromDate, @RequestParam(required = false) String toDate)
            throws ParseException {
        if (examStatus == null) {
            examStatus = "";
        }
        if (courseid == null) {
            courseid = "";
        }
        if (fromDate == null && toDate == null) {
            fromDate = "";
            toDate = "";
        }

        Long userID = getUid(null);
        List<Test> testList;
        List<CourseInfo> courseList;
        if (examStatus != "" || courseid != "" || fromDate != "" || toDate != "") {
            if (examStatus != "") {
                testList = testRepository.getListByStatusAndUser(examStatus, userID);
                model.addAttribute("testList", testList);
                model.addAttribute("filterType", "Filter By Status");
                model.addAttribute("filter", "( " + examStatus + " )");
            } else if (courseid != "") {
                CourseInfo course = courseInfoRepository.getById(Long.parseLong(courseid));
                testList = testRepository.getListByCourseAndUser(Long.parseLong(courseid), userID);
                model.addAttribute("testList", testList);
                model.addAttribute("filterType", "Filter By Course");
                model.addAttribute("filter", "( " + course.getCourseName() + " )");
            } else if (fromDate != "" && toDate != "") {
                Date from = new SimpleDateFormat("yyyy-MM-dd").parse(fromDate);
                Date to = new SimpleDateFormat("yyyy-MM-dd").parse(toDate);
                testList = testRepository.getListByDateAndUser(from, to, userID);
                model.addAttribute("testList", testList);
                model.addAttribute("filterType", "Filter By Date");
                model.addAttribute("filter", "( " + fromDate + " - " + toDate + " )");
            }
        } else {
            testList = testRepository.getListByUser(userID);
            model.addAttribute("testList", testList);
        }

        model.addAttribute("role", "teacher");
        courseList = courseInfoRepository.findByUID(userID);
        model.addAttribute("courseList", courseList);

        return "AT0004_ExamList";
    }

    @Valid
    @GetMapping(value = { "/admin/exam" })
    private String getExamManagementPageByAdmin(Model model,
            @RequestParam(required = false) String examStatus, @RequestParam(required = false) String courseid,
            @RequestParam(required = false) String fromDate, @RequestParam(required = false) String toDate,
            @RequestParam(required = false) String teacherid)
            throws ParseException {

        Long userID = getUid(null);

        if (examStatus == null) {
            examStatus = "";
        }
        if (courseid == null) {
            courseid = "";
        }
        if (teacherid == null) {
            teacherid = "";
        }
        if (fromDate == null && toDate == null) {
            fromDate = "";
            toDate = "";
        }

        List<Test> testList;
        List<CourseInfo> courseList;
        List<UserInfo> teacherList;
        if (examStatus != "" || courseid != "" || fromDate != "" || toDate != "" || teacherid != "") {
            if (examStatus != "") {
                testList = testRepository.getListByStatus(examStatus);
                model.addAttribute("testList", testList);
                model.addAttribute("filterType", "Filter By Status");
                model.addAttribute("filter", "( " + examStatus + " )");
            } else if (courseid != "") {
                CourseInfo course = courseInfoRepository.getById(Long.parseLong(courseid));
                testList = testRepository.getListByCourse(Long.parseLong(courseid));
                model.addAttribute("testList", testList);
                model.addAttribute("filterType", "Filter By Course");
                model.addAttribute("filter", "( " + course.getCourseName() + " )");
            } else if (fromDate != "" && toDate != "") {
                Date from = new SimpleDateFormat("yyyy-MM-dd").parse(fromDate);
                Date to = new SimpleDateFormat("yyyy-MM-dd").parse(toDate);
                testList = testRepository.getListByDate(from, to);
                model.addAttribute("testList", testList);
                model.addAttribute("filterType", "Filter By Date");
                model.addAttribute("filter", "( " + fromDate + " - " + toDate + " )");
            } else if (teacherid != "") {
                UserInfo teacher = userRepository.findByAccount(Long.parseLong(teacherid));
                testList = testRepository.getListByUser(Long.parseLong(teacherid));
                model.addAttribute("testList", testList);
                model.addAttribute("filterType", "Filter By Teacher");
                model.addAttribute("filter", "( " + teacher.getUserName() + " )");
            }
        } else {
            testList = testRepository.getListByAdmin();
            model.addAttribute("testList", testList);
        }

        courseList = courseInfoRepository.findAll();
        teacherList = userRepository.findByUserRoleI("ROLE_TEACHER");
        model.addAttribute("role", "admin");
        model.addAttribute("courseList", courseList);
        model.addAttribute("teacherList", teacherList);
        return "AT0004_AdminExamList";
    }

    @Valid
    @GetMapping(value = { "/teacher/delete-exam" })
    private ResponseEntity deleteExam(@RequestParam(required = false) Long test_id)
            throws ParseException {
        testRepository.deleteById(test_id);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @Valid
    @GetMapping(value = { "/admin/delete-exam" })
    private ResponseEntity deleteExamByAdmin(@RequestParam(required = false) Long test_id)
            throws ParseException {
        testRepository.deleteById(test_id);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @Valid
    @GetMapping(value = { "/admin/get-course-by-teacher" })
    private ResponseEntity getCourseByTeacher(@RequestParam(required = false) String teacher_id)
            throws ParseException {
        List<CourseInfo> courseInfos = courseInfoRepository.findByUID(Long.parseLong(teacher_id));
        return ResponseEntity.status(HttpStatus.OK).body(courseInfos);
    }

    @Valid
    @PostMapping(value = { "/teacher/create-exam" })
    private ResponseEntity saveExam(@RequestBody String payload) {
        Long userID = getUid(null);
        JSONObject jsonObject = new JSONObject(payload);
        String description = jsonObject.getString("description");
        String section_name = jsonObject.getString("section_name");
        String date = jsonObject.getString("date");
        Date examDate = null;
        try {
            examDate = new SimpleDateFormat("yyyy-MM-dd").parse(date);
        } catch (Exception e) {

        }
        String exam_status = jsonObject.getString("exam_status");
        Long course_id = jsonObject.getLong("course_id");
        String exam_start_time = jsonObject.getString("start_time");
        String exam_end_time = jsonObject.getString("end_time");
        int passing_score = Integer.parseInt(jsonObject.getString("passing_score"));
        int minutes_allowed = jsonObject.getInt("minutes_allowed");
        CourseInfo courseInfo = courseInfoRepository.findByCourseID(course_id);
        UserInfo userInfo = userInfoRepository.findStudentById(userID);
        Test test = new Test(null, courseInfo, userInfo, description, section_name, minutes_allowed, passing_score,
                examDate, exam_start_time, exam_end_time, exam_status);
        testRepository.save(test);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @Valid
    @PostMapping(value = { "/admin/create-exam" })
    private ResponseEntity saveExamByAdmin(@RequestBody String payload) {
        JSONObject jsonObject = new JSONObject(payload);
        Long teacher_id = jsonObject.getLong("teacher_id");        
        String description = jsonObject.getString("description");
        String section_name = jsonObject.getString("section_name");
        String date = jsonObject.getString("date");
        Date examDate = null;
        try {
            examDate = new SimpleDateFormat("yyyy-MM-dd").parse(date);
        } catch (Exception e) {

        }
        String exam_status = jsonObject.getString("exam_status");
        Long course_id = jsonObject.getLong("course_id");
        String exam_start_time = jsonObject.getString("start_time");
        String exam_end_time = jsonObject.getString("end_time");
        int passing_score = Integer.parseInt(jsonObject.getString("passing_score"));
        int minutes_allowed = jsonObject.getInt("minutes_allowed");
        CourseInfo courseInfo = courseInfoRepository.findByCourseID(course_id);
        UserInfo userInfo = userInfoRepository.findStudentById(teacher_id);
        Test test = new Test(null, courseInfo, userInfo, description, section_name, minutes_allowed, passing_score,
                examDate, exam_start_time, exam_end_time, exam_status);
        testRepository.save(test);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @Valid
    @PostMapping(value = { "/admin/edit-exam" })
    private ResponseEntity editExam(@RequestBody String payload) {
        JSONObject jsonObject = new JSONObject(payload);
        Long teacher_id = jsonObject.getLong("teacher_id");
        Long test_id = jsonObject.getLong("test_id");
        String description = jsonObject.getString("description");
        String section_name = jsonObject.getString("section_name");
        String date = jsonObject.getString("date");
        Date examDate = null;
        try {
            examDate = new SimpleDateFormat("yyyy-MM-dd").parse(date);
        } catch (Exception e) {

        }
        String exam_status = jsonObject.getString("exam_status");
        Long course_id = jsonObject.getLong("course_id");
        String exam_start_time = jsonObject.getString("start_time");
        String exam_end_time = jsonObject.getString("end_time");
        int passing_score = Integer.parseInt(jsonObject.getString("passing_score"));
        int minutes_allowed = jsonObject.getInt("minutes_allowed");
        CourseInfo courseInfo = courseInfoRepository.findByCourseID(course_id);
        UserInfo userInfo = userInfoRepository.findStudentById(teacher_id);
        Test test = new Test(test_id, courseInfo, userInfo, description, section_name, minutes_allowed, passing_score,
                examDate, exam_start_time, exam_end_time, exam_status);
        testRepository.save(test);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    private Long getUid(Long id) {
        Long uid = 0L;
        UserRole role = userSessionService.getRole();
        if (role == UserRole.ADMIN || role == UserRole.SUPER_ADMIN) {
            uid = id;
        } else if (id != null) {
            uid = id;
        } else if (role == UserRole.TEACHER || role == UserRole.STUDENT) {
            uid = userSessionService.getUserAccount().getAccountId();
        } else {
            throw new RuntimeException("user authetication fail");
        }
        return uid;
    }
}
