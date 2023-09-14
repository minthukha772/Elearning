package com.blissstock.mappingSite.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import com.blissstock.mappingSite.service.MailService;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import com.blissstock.mappingSite.entity.CourseInfo;
import com.blissstock.mappingSite.entity.GuestUser;
import com.blissstock.mappingSite.entity.JoinCourseUser;
import com.blissstock.mappingSite.entity.PaymentHistory;
import com.blissstock.mappingSite.entity.TestResult;
import com.blissstock.mappingSite.entity.Test;
import com.blissstock.mappingSite.entity.TestExaminee;
import com.blissstock.mappingSite.entity.TestExamineeAnswer;
import com.blissstock.mappingSite.entity.TestQuestion;
import com.blissstock.mappingSite.entity.UserInfo;
import com.blissstock.mappingSite.repository.CourseInfoRepository;
import com.blissstock.mappingSite.repository.GuestUserRepository;
import com.blissstock.mappingSite.repository.JoinCourseUserRepository;
import com.blissstock.mappingSite.repository.TestResultRepository;
import com.blissstock.mappingSite.repository.TestRepository;
import com.blissstock.mappingSite.repository.TestExamineeAnswerRepository;
import com.blissstock.mappingSite.repository.TestExamineeRepository;
import com.blissstock.mappingSite.repository.UserInfoRepository;
import com.blissstock.mappingSite.repository.UserRepository;
import com.blissstock.mappingSite.service.UserSessionService;

@Controller
public class TestController {
    private static Logger logger = LoggerFactory.getLogger(
            TestController.class);

    @Autowired
    UserSessionService userSessionService;

    @Autowired
    TestRepository testRepository;

    @Autowired
    CourseInfoRepository courseInfoRepository;

    @Autowired
    UserInfoRepository userInfoRepository;
    @Autowired
    MailService mailService;
    @Autowired
    UserRepository userRepository;

    @Autowired
    private TestResultRepository resultRepo;

    @Autowired
    private TestExamineeRepository TestExamineeRepository;

    @Autowired
    private TestExamineeAnswerRepository TestExamineeAnswerRepository;

    @Autowired
    private JoinCourseUserRepository joinCourseUserRepository;

    @Autowired
    private GuestUserRepository guestUserRepository;

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

        try {
            Long userID = getUid();
            List<Test> testList;
            List<CourseInfo> courseList;
            // logger.info("user id {} start processing URL /teacher/exam", userID);
            logger.info("Called getExamManagementPage with parameter(user_id={})", userID);
            if (examStatus != "" || courseid != "" || fromDate != "" || toDate != "") {
                if (examStatus != "") {
                    if (examStatus.equals("Deleted")) {

                        logger.info(
                                "Initiate Operation Retrieve Table test by Query: exam_status = Deleted, user_id={}",
                                userID);

                        testList = testRepository.getDeletedListByUser(userID);
                        // logger.info("userid {} get test list with user id and exam status by test id
                        // {}", userID, testList);
                        model.addAttribute("testList", testList);
                        model.addAttribute("filterType", "Filter By Status");
                        model.addAttribute("filter", "( " + examStatus + " )");
                        logger.info(
                                "Operation Retrieve Table test by Query: exam_status = Deleted, user_id={}. Result List: test_list={}, filter_Type=Filter By Status, exam_status={}  | Success",
                                userID, testList.size(), examStatus);

                    } else if (!examStatus.equals("Deleted")) {
                        logger.info("Initiate Operation Retrieve Table test by Query: exam_status = {}, user_id={}",
                                examStatus, userID);
                        testList = testRepository.getListByStatusAndUser(examStatus, userID);
                        // logger.info("userid {} get test list with user id and exam status by test id
                        // {}", userID, testList);
                        model.addAttribute("testList", testList);
                        model.addAttribute("filterType", "Filter By Status");
                        model.addAttribute("filter", "( " + examStatus + " )");
                        logger.info(
                                "Operation Retrieve Table test by Query: exam_status = {}, user_id={}. Result List: test_list={}, filter_Type=Filter By Status, exam_status={}  | Success",
                                examStatus, userID, testList.size(), examStatus);
                    }
                } else if (courseid != "") {
                    logger.info("Userid {} Initiate Operation Retrieve Table test by Query: course_id ={}", userID,
                            courseid);
                    CourseInfo course = courseInfoRepository.getById(Long.parseLong(courseid));
                    testList = testRepository.getListByCourseAndUser(Long.parseLong(courseid), userID);
                    // logger.info("userid {} get test list with user id and course info by test id
                    // {}", userID, testList);
                    model.addAttribute("testList", testList);
                    model.addAttribute("filterType", "Filter By Course");
                    model.addAttribute("filter", "( " + course.getCourseName() + " )");
                    logger.info(
                            "Operation Retrieve Table test by Query: course_id ={}, user_id={}. Result List: test_list={}, course_name ={}  | Success",
                            courseid, userID, testList.size(), course.getCourseName());
                } else if (fromDate != "" && toDate != "") {
                    Date from = new SimpleDateFormat("yyyy-MM-dd").parse(fromDate);
                    Date to = new SimpleDateFormat("yyyy-MM-dd").parse(toDate);
                    testList = testRepository.getListByDateAndUser(from, to, userID);
                    // logger.info("userid {} get test list with user id and from date to date by
                    // test id {}", userID, testList);
                    logger.info("Initiate Operation Retrieve Table test by Query: user_id ={}, From ={}, To ={}",
                            userID, from, to);
                    model.addAttribute("testList", testList);
                    model.addAttribute("filterType", "Filter By Date");
                    model.addAttribute("filter", "( " + fromDate + " - " + toDate + " )");
                    logger.info(
                            "Operation Retrieve Table test by Query: user_id ={}, From ={}, To ={}. Result List: test_list={}, From ={}, To ={}  | Success",
                            userID, from, to, testList.size(), from, to);
                }
            } else {
                logger.info("Initiate Operation Retrieve Table test by Query: user_id ={}", userID);
                testList = testRepository.getListByUser(userID);
                // logger.info("user id {} get test list by userid", userID);
                model.addAttribute("testList", testList);
                logger.info("Operation Retrieve Table test by Query: user_id ={}. Result List: test_list={} | Success",
                        userID, testList.size());
            }

            model.addAttribute("role", "teacher");
            courseList = courseInfoRepository.findByUID(userID);
            model.addAttribute("courseList", courseList);

            logger.info("User " + userID + " Received response from URL: /teacher/exam with status code: 200");
            logger.info("Called getExamManagementPage with parameter(user_id={}) Success", userID);
            return "AT0004_ExamList";
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage());
            return "500";
        }
    }

    @Valid
    @GetMapping(value = { "/student/exam" })
    private String getExamListPage(Model model,
            @RequestParam(required = false) String examStatus, @RequestParam(required = false) String courseid,
            @RequestParam(required = false) String fromDate, @RequestParam(required = false) String toDate,
            @RequestParam(required = false) String teacherid)
            throws ParseException {
        try {
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

            Long userID = getUid();
            List<Test> testList;
            List<JoinCourseUser> joinList;
            // List<CourseInfo> courseList;
            List<CourseInfo> courseList = new ArrayList<>();
            List<UserInfo> teacherList = new ArrayList<>();
            // logger.info("user id {} start processing URL /teacher/exam", userID);
            logger.info("Called getExamListPage with parameter(user_id={})", userID);
            if (examStatus != "" || courseid != "" || fromDate != "" || toDate != "" || teacherid != "") {
                if (examStatus != "") {
                    logger.info("Initiate Operation Retrieve Table test by Query: exam_status ={}, user_id={}",
                            examStatus, userID);
                    testList = testRepository.getListByStatusAndStudentId(examStatus, userID);
                    // logger.info("userid {} get test list with user id and exam status by test id
                    // {}", userID, testList);
                    model.addAttribute("testList", testList);
                    model.addAttribute("filterType", "Filter By Status");
                    model.addAttribute("filter", "( " + examStatus + " )");
                    logger.info(
                            "Operation Retrieve Table test by Query: exam_status ={}, user_id={}. Result List: test_list={}, exam_status={}  | Success",
                            examStatus, userID, testList.size(), examStatus);
                } else if (courseid != "") {
                    logger.info("Userid {} Initiate Operation Retrieve Table test by Query: course_id ={}", userID,
                            courseid);
                    // Log the test list retrieval by course ID
                    // logger.info("user id {} Retrieving test list by course ID: {}", userID,
                    // courseid);
                    CourseInfo course = courseInfoRepository.getById(Long.parseLong(courseid));
                    testList = testRepository.getListByCourse(Long.parseLong(courseid));
                    model.addAttribute("testList", testList);
                    model.addAttribute("filterType", "Filter By Course");
                    model.addAttribute("filter", "( " + course.getCourseName() + " )");
                    logger.info(
                            "Operation Retrieve Table test by Query: course_id ={}. Result List: test_list={}, course_name ={}  | Success",
                            courseid, testList.size(), course.getCourseName());
                } else if (fromDate != "" && toDate != "") {
                    Date from = new SimpleDateFormat("yyyy-MM-dd").parse(fromDate);
                    Date to = new SimpleDateFormat("yyyy-MM-dd").parse(toDate);
                    logger.info("Initiate Operation Retrieve Table test by Query: user_id ={}, From ={}, To ={}",
                            userID, from, to);
                    testList = testRepository.getListByDateAndStudentId(from, to, userID);
                    // logger.info("userid {} get test list with user id and from date to date by
                    // test id {}", userID,
                    // testList);
                    model.addAttribute("testList", testList);
                    model.addAttribute("filterType", "Filter By Date");
                    model.addAttribute("filter", "( " + fromDate + " - " + toDate + " )");
                    logger.info(
                            "Operation Retrieve Table test by Query: user_id ={}, From ={}, To ={}. Result List: test_list={}, From ={}, To ={}  | Success",
                            userID, from, to, testList.size(), from, to);
                } else if (teacherid != "") {
                    // Log the test list retrieval by teacher ID
                    // logger.info("user id {} Retrieving test list by teacher ID: {}", userID,
                    // teacherid);
                    logger.info("user_id ={} Initiate Operation Retrieve Table test by Query: teacher_id ={}", userID,
                            teacherid);
                    UserInfo teacher = userRepository.findByAccount(Long.parseLong(teacherid));
                    testList = testRepository.getListByUser(Long.parseLong(teacherid));
                    model.addAttribute("testList", testList);
                    model.addAttribute("filterType", "Filter By Teacher");
                    model.addAttribute("filter", "( " + teacher.getUserName() + " )");
                    logger.info(
                            "Operation Retrieve Table test by Query: teacher_id ={}. Result List: test_list={}, teacher_name ={}  | Success",
                            teacherid, testList.size(), teacher.getUserName());
                }
            } else {
                logger.info("Initiate Operation Retrieve Table test by Query: user_id ={}", userID);
                testList = testRepository.getListByStudent(userID);
                // logger.info("user id {} get test list by userid", userID);
                model.addAttribute("testList", testList);
                logger.info("Operation Retrieve Table test by Query:  user_id ={}. Result List: test_list={} | Success",
                        userID, testList.size());
            }

            // model.addAttribute("role", "teacher");
            // courseList = courseInfoRepository.findByUID(userID);
            // model.addAttribute("courseList", courseList);
            joinList = joinCourseUserRepository.findByStuId(userID);
            for (JoinCourseUser checkJoinUser : joinList) {
                Long courseID = checkJoinUser.getCourseInfo().getCourseId();
                CourseInfo course = courseInfoRepository.getById(courseID);
                courseList.add(course);

                Long teacherId = course.getUserInfo().getUid();

                UserInfo teacher = userInfoRepository.findStudentById(teacherId);
                teacherList.add(teacher);

                model.addAttribute("courseList", courseList);
                model.addAttribute("teacherList", teacherList);
            }
            // courseList = courseInfoRepository.findByUID(userID);
            // teacherList = userRepository.findByUserRoleI("ROLE_TEACHER");
            // Log the model attributes being added
            // model.addAttribute("courseList", courseList);
            // logger.info("User " + userID + " Received response from URL: /teacher/exam
            // with status code: 200");
            logger.info("Called getExamListPage with parameter(user_id={}) Success", userID);
            return "ST0005_ExamListStudent";
        } catch (Exception e) {
            // logger.error(e.getLocalizedMessage());
            return "500";
        }
    }

    @Valid
    @GetMapping(value = { "/admin/exam" })
    private String getExamManagementPageByAdmin(Model model,
            @RequestParam(required = false) String examStatus, @RequestParam(required = false) String courseid,
            @RequestParam(required = false) String fromDate, @RequestParam(required = false) String toDate,
            @RequestParam(required = false) String teacherid) {
        try {
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
            String responseString = "";
            if (examStatus != "" || courseid != "" || fromDate != "" || toDate != "" || teacherid != "") {
                if (examStatus != "") {
                    logger.info("Called getExamManagementPageByAdmin with parameters: examStatus={}", examStatus);
                    responseString = "Called AT_0004 with parameters: examStatus=" + examStatus + " Success";
                    if (examStatus.equals("Deleted")) {
                        logger.info("Initiate to Operation Retrieve Table test by Query {}", examStatus);
                        testList = testRepository.getDeletedListByAdmin();
                        logger.info("Operation Retrieve Table test by Query Status {} Result list {} Success",
                                examStatus, testList.size());
                        model.addAttribute("testList", testList);
                        model.addAttribute("filterType", "Filter By Status");
                        model.addAttribute("filter", "( " + examStatus + " )");
                    } else if (!examStatus.equals("Deleted")) {
                        logger.info("Initiate to Operation Retrieve Table test by Query Status {}", examStatus);
                        testList = testRepository.getListByStatus(examStatus);
                        logger.info("Operation Retrieve Table test by Query Status {} Result list {} Success",
                                examStatus, testList.size());
                        model.addAttribute("testList", testList);
                        model.addAttribute("filterType", "Filter By Status");
                        model.addAttribute("filter", "( " + examStatus + " )");
                    }
                } else if (courseid != "") {
                    responseString = "Called AT_0004 with parameters: courseid=" + courseid + " Success";
                    logger.info("Called AT_0004 with parameters: course={}", courseid);
                    logger.info("Initiate to Operation Retrieve Table course_info by Query Course {}", courseid);
                    CourseInfo course = courseInfoRepository.getById(Long.parseLong(courseid));
                    logger.info("Operation Retrieve Table course_info by Query Course {} Success", courseid);
                    logger.info("Initiate to Operation Retrieve Table test by Query Course {}", courseid);
                    testList = testRepository.getListByCourse(Long.parseLong(courseid));
                    logger.info("Operation Retrieve Table test by Query Course {} Result list {} Success", courseid,
                            testList.size());
                    model.addAttribute("testList", testList);
                    model.addAttribute("filterType", "Filter By Course");
                    model.addAttribute("filter", "( " + course.getCourseName() + " )");
                } else if (fromDate != "" && toDate != "") {
                    responseString = "Called AT_0004 with parameters: fromDate=" + fromDate + " toDate=" + toDate
                            + " Success";
                    logger.info("Called AT_0004 with parameters: fromDate={} and toDate={}", fromDate, toDate);
                    Date from = new SimpleDateFormat("yyyy-MM-dd").parse(fromDate);
                    Date to = new SimpleDateFormat("yyyy-MM-dd").parse(toDate);
                    logger.info("Initiate to Operation Retrieve Table test by Query fromDate {} toDate {}", fromDate,
                            toDate);
                    testList = testRepository.getListByDate(from, to);
                    logger.info("Operation Retrieve Table test by Query fromDate {} toDate {} Result list {} Success",
                            from, to, testList.size());
                    model.addAttribute("testList", testList);
                    model.addAttribute("filterType", "Filter By Date");
                    model.addAttribute("filter", "( " + fromDate + " - " + toDate + " )");
                } else if (teacherid != "") {
                    responseString = "Called AT_0004 with parameters: teacherid=" + teacherid + " Success";
                    logger.info("Called AT_0004 with parameters: teacher={}", teacherid);
                    UserInfo teacher = userRepository.findByAccount(Long.parseLong(teacherid));
                    logger.info("Initiate to Operation Retrieve Table test by Query Teacher {}", teacherid);
                    testList = testRepository.getListByUser(Long.parseLong(teacherid));
                    logger.info("Operation Retrieve Table test by Query Teacher {} Result list {} Success", teacherid,
                            testList.size());
                    model.addAttribute("testList", testList);
                    model.addAttribute("filterType", "Filter By Teacher");
                    model.addAttribute("filter", "( " + teacher.getUserName() + " )");
                }
            } else {
                responseString = "Called AT_0004 with parameters: None Success";
                logger.info("Called AT_0004 with parameters: None");
                logger.info("Initiate to Operation Retrieve Table test by Query None");
                testList = testRepository.getListByAdmin();
                logger.info("Operation Retrieve Table test by Query None Result list {} Success", testList.size());
                model.addAttribute("testList", testList);
            }

            logger.info("Initiate to Operation Retrieve Table course_info by Query None");
            courseList = courseInfoRepository.findAll();
            logger.info("Operation Retrieve Table course_info by Query None Result list {} Success", courseList.size());

            logger.info("Initiate to Operation Retrieve Table user_info, user_account by Query Role Teacher");
            teacherList = userRepository.findByUserRoleI("ROLE_TEACHER");
            logger.info("Operation Retrieve Table user_info, user_account by Query Role Teacher Result list {} Success",
                    teacherList.size());
            // Log the model attributes being added
            model.addAttribute("role", "admin");
            model.addAttribute("courseList", courseList);
            model.addAttribute("teacherList", teacherList);

            logger.info(responseString);
            logger.info("Called getExamManagementPageByAdmin with parameters: examStatus={} Success", examStatus);
            return "AT0004_AdminExamList";
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage());
            return "500";
        }
    }

    @Valid
    @GetMapping(value = { "/teacher/delete-exam" })
    private ResponseEntity deleteExam(@RequestParam(required = false) Long test_id)
            throws ParseException {

        try {
            Long userID = getUid();
            logger.info("Called deleteExam with parameter(test_id={})", test_id);
            logger.info("user_id: {}", userID);
            if (test_id == null) {
                logger.warn("Invalid ID format for exam deletion : {}", test_id);
                return ResponseEntity.badRequest().build();
            }
            Optional<Test> test = testRepository.findById(test_id);
            if (test.isEmpty()) {
                logger.warn("Attempt to delete non-existent exam with ID {}", test_id);
                return ResponseEntity.noContent().build();
            }

            Test testData = testRepository.getTestByID(test_id);
            String isDelete = testData.getIsDelete();
            if (!isDelete.equals("true")) {
                logger.info("Initiate to Operation Soft Delete Table test by test_id: {}", test_id);
                logger.info("user_id: {}", userID);
                LocalDateTime currentDateTime = LocalDateTime.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                String deletedAt = currentDateTime.format(formatter);

                testData.setDeletedAt(deletedAt);
                testData.setIsDelete("true");
                logger.info("Initiate to Operation Insert Table Test Data {}", testData.display());
                testRepository.save(testData);
                logger.info("Operation Insert Table Test Data {} | Success", testData.display());
                logger.info("user_id: {}", userID);
            }
            if (isDelete.equals("true")) {
                logger.info("Initiate to Restore Soft Delete Table test by test_id: {}", test_id);
                logger.info("user_id: {}", userID);
                testData.setDeletedAt("null");
                testData.setIsDelete("false");
                logger.info("Initiate to Operation Insert Table Test Data {}", testData.display());
                testRepository.save(testData);
                logger.info("Operation Insert Table Test Data {} | Success", testData.display());
                logger.info("user_id: {}", userID);
            }
            // testRepository.deleteById(test_id);
            // logger.info("User ID {} Exam with ID {} Successfully deleted", userID,
            // test_id);
            logger.info("Called deleteExam with parameter(test_id={}) Success", test_id);
            logger.info("user_id: {}", userID);
            return ResponseEntity.ok(HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Valid
    @GetMapping(value = { "/admin/delete-exam" })
    private ResponseEntity deleteExamByAdmin(@RequestParam(required = false) Long test_id)
            throws ParseException {
        try {
            Long userID = getUid();
            logger.info("Called deleteExamByAdmin with parameter(test_id={})", test_id);
            logger.info("user_id: {}", userID);
            if (test_id == null) {
                logger.warn("Invalid ID format for exam deletion : {}", test_id);
                return ResponseEntity.badRequest().build();
            }
            Optional<Test> test = testRepository.findById(test_id);
            if (test.isEmpty()) {
                logger.warn("Attempt to delete non-existent exam with ID {}", test_id);
                return ResponseEntity.noContent().build();
            }
            logger.info("Received request to delete exam with ID: {}", test_id);
            Test testData = testRepository.getTestByID(test_id);
            String isDelete = testData.getIsDelete();
            if (!isDelete.equals("true")) {
                logger.info("Initiate to Operation Soft Delete Table test by test_id: {}", test_id);
                logger.info("user_id: {}", userID);
                LocalDateTime currentDateTime = LocalDateTime.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                String deletedAt = currentDateTime.format(formatter);
                testData.setDeletedAt(deletedAt);
                testData.setIsDelete("true");
                logger.info("Initiate to Operation Insert Table Test Data {}", testData.display());
                testRepository.save(testData);
                logger.info("Operation Insert Table Test Data {} | Success", testData.display());
                logger.info("user_id: {}", userID);
            }
            if (isDelete.equals("true")) {
                logger.info("Initiate to Restore Soft Delete Table test by test_id: {}", test_id);
                logger.info("user_id: {}", userID);
                testData.setDeletedAt("null");
                testData.setIsDelete("false");
                logger.info("Initiate to Operation Insert Table Test Data {}", testData.display());
                testRepository.save(testData);
                logger.info("Operation Insert Table Test Data {} | Success", testData.display());
                logger.info("user_id: {}", userID);
            }
            // testRepository.deleteById(test_id);
            // logger.info("User ID {} Exam with ID {} Successfully deleted", userID,
            // test_id);
            logger.info("Called deleteExamByAdmin with parameter(test_id={}) Success", test_id);
            logger.info("user_id: {}", userID);
            return ResponseEntity.ok(HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Valid
    @GetMapping(value = { "/admin/get-course-by-teacher" })
    private ResponseEntity getCourseByTeacher(@RequestParam(required = false) String teacher_id) throws ParseException {
        try {
            Long userID = getUid();
            logger.info("Called getCourseByTeacher with parameter(teacher_id={})", teacher_id);
            logger.info("user_id: {}", userID);
            Long teacherId = Long.parseLong(teacher_id);
            logger.info("Initiate Operation Retrieve Table course_info by Query: teacher_id: {}", teacherId);
            List<CourseInfo> courseInfos = courseInfoRepository.findByUID(teacherId);
            logger.info(
                    "User ID {} Operation Retrieve Table course_info by Query: teacher_id: {}. Result: course(s)={} | Success",
                    userID, teacherId, courseInfos.size());
            logger.info("Called getCourseByTeacher with parameter(teacher_id={}) Success", teacher_id);
            logger.info("user_id: {}", userID);
            return ResponseEntity.ok(courseInfos);
        } catch (NumberFormatException e) {
            logger.warn("Invalid teacher ID format: {}", teacher_id);
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            logger.error("Failed to retrieve courses for teacher with ID {}", teacher_id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Valid
    @PostMapping(value = { "/teacher/create-exam" })
    private ResponseEntity saveExam(@RequestBody String payload) {
        try {
            Long userID = getUid();
            logger.info("Called saveExam with parameter(payload={})", payload);
            logger.info("user_id: {}", userID);
            JSONObject jsonObject = new JSONObject(payload);
            String description = jsonObject.getString("description");
            String section_name = jsonObject.getString("section_name");
            String date = jsonObject.getString("date");
            Date examDate = null;
            try {
                examDate = new SimpleDateFormat("yyyy-MM-dd").parse(date);
            } catch (Exception e) {
                logger.warn("Failed to parse date: " + e.getMessage());
            }
            String exam_status = jsonObject.getString("exam_status");
            Long course_id = jsonObject.getLong("course_id");
            String exam_start_time = jsonObject.getString("start_time");
            String exam_end_time = jsonObject.getString("end_time");
            int passing_score = Integer.parseInt(jsonObject.getString("passing_score"));
            int minutes_allowed = jsonObject.getInt("minutes_allowed");
            logger.info("Initiate Operation Retrieve Table course_info by Query: course_id: {}", course_id);
            CourseInfo courseInfo = courseInfoRepository.findByCourseID(course_id);
            logger.info(
                    "User ID {} Operation Retrieve Table course_info by Query: course_id: {}. Result: course_info={} | Success",
                    userID, course_id, courseInfo);
            if (courseInfo == null) {
                logger.warn("Failed to find course with ID: " + course_id);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Failed to find course with ID: " + course_id);
            }
            logger.info("Initiate Operation Retrieve Table user_info by Query: user_id: {}", userID);
            UserInfo userInfo = userInfoRepository.findStudentById(userID);
            if (userInfo == null) {
                logger.warn("Failed to find user with ID: " + userID);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to find user with ID: " + userID);
            }
            logger.info("Operation Retrieve Table user_info by Query: user_id: {}. Result: user_info={} | Success",
                    userID, userInfo);
            Test test = new Test(null, courseInfo, userInfo, description, section_name, minutes_allowed, passing_score,
                    examDate, exam_start_time, exam_end_time, exam_status, "false", "null", "null", 0);
            logger.info("Initiate to Operation Insert Table Test Data {}", test.display());
            testRepository.save(test);
            logger.info("Operation Insert Table Test Data {} | Success", test.display());
            logger.info("Called saveExam with parameter(payload={}) Success", payload);
            logger.info("user_id: {}", userID);
            return ResponseEntity.ok(HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Valid
    @PostMapping(value = { "/admin/create-exam" })
    private ResponseEntity saveExamByAdmin(@RequestBody String payload) {
        try {
            // logger.info("Called saveExamByAdmin");
            Long userID = getUid();
            logger.info("Called saveExamByAdmin with parameter(payload={})", payload);
            logger.info("user_id: {}", userID);
            JSONObject jsonObject = new JSONObject(payload);
            Long teacher_id = jsonObject.getLong("teacher_id");
            String description = jsonObject.getString("description");
            String section_name = jsonObject.getString("section_name");
            String student_guest = jsonObject.getString("student_guest");
            String date = jsonObject.getString("date");
            Date examDate = null;
            try {
                examDate = new SimpleDateFormat("yyyy-MM-dd").parse(date);
            } catch (Exception e) {
                logger.warn("Failed to parse date: " + e.getMessage());
            }
            String exam_status = jsonObject.getString("exam_status");
            Long course_id = jsonObject.getLong("course_id");
            String exam_start_time = jsonObject.getString("start_time");
            String exam_end_time = jsonObject.getString("end_time");
            int passing_score = Integer.parseInt(jsonObject.getString("passing_score"));
            int minutes_allowed = jsonObject.getInt("minutes_allowed");
            CourseInfo courseInfo = courseInfoRepository.findByCourseID(course_id);
            if (courseInfo == null) {
                logger.warn("Operation Retrieve Table course_info by query course_id = {} Result No Data",
                        course_id);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Failed to find course with ID: " + course_id);
            }
            // UserInfo userInfo = userInfoRepository.findStudentById(teacher_id);
            UserInfo userInfo = userInfoRepository.findById(teacher_id).orElse(null);
            if (userInfo == null) {
                logger.warn("Operation Retrieve Table user_info, user_account by query user_id = {} Result No Data",
                        teacher_id);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to find user with ID: " + teacher_id);
            }

            int exam_target;
            // if (student_guest == "student") {
            if (student_guest.equals("student")) {
                exam_target = 0;
            } else {
                exam_target = 1;
            }
            Test test = new Test(null, courseInfo, userInfo, description, section_name, minutes_allowed, passing_score,
                    examDate, exam_start_time, exam_end_time, exam_status, "false", "null", student_guest, exam_target);
            logger.info("Initiate to Operation Insert Table Test Data {}", test.display());

            testRepository.save(test);
            logger.info("Operation Insert Table Test Data {} Success", test.display());

            logger.info("Called saveExamByAdmin with parameter(payload={}) Success", payload);
            logger.info("user_id: {}", userID);
            return ResponseEntity.ok(HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Valid
    @PostMapping(value = { "/admin/edit-exam" })
    private ResponseEntity editExam(@RequestBody String payload) {
        try {
            Long userID = getUid();
            logger.info("Called editExam with parameter(payload={})", payload);
            logger.info("user_id: {}", userID);
            JSONObject jsonObject = new JSONObject(payload);
            Long teacher_id = jsonObject.getLong("teacher_id");
            Long test_id = jsonObject.getLong("test_id");
            String description = jsonObject.getString("description");
            String section_name = jsonObject.getString("section_name");
            String student_guest = jsonObject.getString("student_guest");
            String date = jsonObject.getString("date");
            Date examDate = null;
            try {
                examDate = new SimpleDateFormat("yyyy-MM-dd").parse(date);
            } catch (Exception e) {
                logger.warn("Failed to parse date: " + e.getMessage());
            }
            String exam_status = jsonObject.getString("exam_status");
            Long course_id = jsonObject.getLong("course_id");
            String exam_start_time = jsonObject.getString("start_time");
            String exam_end_time = jsonObject.getString("end_time");
            int passing_score = Integer.parseInt(jsonObject.getString("passing_score"));
            int minutes_allowed = jsonObject.getInt("minutes_allowed");
            CourseInfo courseInfo = courseInfoRepository.findByCourseID(course_id);

            if (courseInfo == null) {
                logger.warn("Operation Retrieve Table course_info by query course_id = {} Result No Data",
                        course_id);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Failed to find course with ID: " + course_id);
            }
            // UserInfo userInfo = userInfoRepository.findStudentById(teacher_id);
            UserInfo userInfo = userInfoRepository.findById(teacher_id).orElse(null);
            if (userInfo == null) {
                logger.warn("Operation Retrieve Table user_info, user_account by query user_id = {} Result No Data",
                        teacher_id);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to find user with ID: " + teacher_id);
            }

            int exam_target;
            if (student_guest.equals("student")) {
                exam_target = 0;
            } else {
                exam_target = 1;
            }
            Test test = new Test(test_id, courseInfo, userInfo, description, section_name, minutes_allowed,
                    passing_score,
                    examDate, exam_start_time, exam_end_time, exam_status, "false", "null", student_guest, exam_target);

            // Test test = new Test(null, courseInfo, userInfo, description, section_name,
            // minutes_allowed, passing_score,
            // examDate, exam_start_time, exam_end_time, exam_status, "false",
            // "null",student_guest,exam_target);

            testRepository.save(test);
            logger.info("Operation Insert Table Test Data {} | Success", test.display());

            if (exam_status.equals("Result Released")) {
                List<TestExaminee> TestExaminees = TestExamineeRepository.getExamineeByTest(test_id);
                for (TestExaminee student : TestExaminees) {
                    int total_acquired_mark = 0;
                    int total_mark = 0;
                    TestResult viewExamResult;
                    if (test.getExam_target() == 1) {
                        viewExamResult = resultRepo.getResultByTestIdAndUser(test_id,
                                student.getGuestUser().getGuest_id());
                    } else {
                        viewExamResult = resultRepo.getResultByTestIdAndUser(test_id,
                                student.getUserInfo().getUid());
                    }

                    if (viewExamResult == null) {
                        List<TestExamineeAnswer> studentAnswerList;
                        if (test.getExam_target() == 1) {
                            studentAnswerList = TestExamineeAnswerRepository
                                    .getGuestAnswerListByTestAndGuest(student.getGuestUser().getGuest_id(),
                                            test_id);
                        } else {
                            studentAnswerList = TestExamineeAnswerRepository
                                    .getStudentAnswerListByTestAndStudent(student.getUserInfo().getUid(),
                                            test_id);
                        }

                        for (TestExamineeAnswer studentAnswer : studentAnswerList) {
                            int acquired_mark = studentAnswer.getAcquired_mark();
                            int max_mark = studentAnswer.getQuestion().getMaximum_mark();
                            total_acquired_mark += acquired_mark;
                            total_mark += max_mark;
                        }
                        Float ftotal_acquired_mark = (float) (total_acquired_mark);
                        Float ftotal_mark = (float) (total_mark);
                        int passing_score_percent = test.getPassing_score_percent();
                        Float fcalculate_percent = (float) (ftotal_acquired_mark / ftotal_mark);
                        fcalculate_percent = fcalculate_percent * 100;
                        if (fcalculate_percent > passing_score_percent) {
                            TestResult result;
                            if (test.getExam_target() == 1) {
                                GuestUser guestUser = guestUserRepository
                                        .findByGuestId(student.getGuestUser().getGuest_id());
                                result = new TestResult(null, test, null, guestUser, total_acquired_mark,
                                        "Passed",
                                        "");
                            } else {
                                UserInfo studentInfo = userInfoRepository
                                        .findStudentById(student.getUserInfo().getUid());
                                result = new TestResult(null, test, studentInfo, null, total_acquired_mark,
                                        "Passed",
                                        "");
                            }

                   
                            resultRepo.save(result);
                        } else {
                            TestResult result;
                            if (test.getExam_target() == 1) {
                                GuestUser guestUser = guestUserRepository
                                        .findByGuestId(student.getGuestUser().getGuest_id());
                                result = new TestResult(null, test, null, guestUser, total_acquired_mark,
                                        "Failed",
                                        "");
                            } else {
                                UserInfo studentInfo = userInfoRepository
                                        .findStudentById(student.getUserInfo().getUid());
                                result = new TestResult(null, test, studentInfo, null, total_acquired_mark,
                                        "Failed",
                                        "");
                            }
                            resultRepo.save(result);
                        }
                    } else {
                        List<TestExamineeAnswer> studentAnswerList = TestExamineeAnswerRepository
                                .getStudentAnswerListByTestAndStudent(student.getUserInfo().getUid(),
                                        test_id);
                        for (TestExamineeAnswer studentAnswer : studentAnswerList) {
                            int acquired_mark = studentAnswer.getAcquired_mark();
                            int max_mark = studentAnswer.getQuestion().getMaximum_mark();
                            total_acquired_mark += acquired_mark;
                            total_mark += max_mark;
                        }
                        Float ftotal_acquired_mark = (float) (total_acquired_mark);
                        Float ftotal_mark = (float) (total_mark);
                        int passing_score_percent = test.getPassing_score_percent();
                        Float fcalculate_percent = (float) (ftotal_acquired_mark / ftotal_mark);
                        fcalculate_percent = fcalculate_percent * 100;
                        if (fcalculate_percent > passing_score_percent) {

                            viewExamResult.setResult("Passed");
                            viewExamResult.setResultMark(total_acquired_mark);
                            logger.info("Initiate to Operation Insert Table Result Data {}", viewExamResult.display());
                            resultRepo.save(viewExamResult);
                            logger.info("Operation Insert Table Result Data {} | Success", viewExamResult.display());
                        } else {

                            viewExamResult.setResult("Failed");
                            viewExamResult.setResultMark(total_acquired_mark);
                            logger.info("Initiate to Operation Insert Table Result Data {}", viewExamResult.display());
                            resultRepo.save(viewExamResult);
                            logger.info("Operation Insert Table Result Data {} | Success", viewExamResult.display());
                        }

                    }

                }
            }
            logger.info("Called editExam with parameter(payload={}) Success", payload);
            logger.info("user_id: {}", userID);

            return ResponseEntity.ok(HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Valid
    @PostMapping(value = { "/teacher/edit-exam" })
    private ResponseEntity editTeacherExam(@RequestBody String payload) {
        try {
            Long userID = getUid();
            logger.info("Called editTeacherExam with parameter(payload={})", payload);
            logger.info("user_id: {}", userID);
            JSONObject jsonObject = new JSONObject(payload);
            Long test_id = jsonObject.getLong("test_id");
            String description = jsonObject.getString("description");
            String section_name = jsonObject.getString("section_name");
            String date = jsonObject.getString("date");
            Date examDate = null;
            try {
                examDate = new SimpleDateFormat("yyyy-MM-dd").parse(date);
            } catch (Exception e) {
                logger.warn("Failed to parse date: " + e.getMessage());
            }
            String exam_status = jsonObject.getString("exam_status");
            Long course_id = jsonObject.getLong("course_id");
            String exam_start_time = jsonObject.getString("start_time");
            String exam_end_time = jsonObject.getString("end_time");
            int passing_score = Integer.parseInt(jsonObject.getString("passing_score"));
            int minutes_allowed = jsonObject.getInt("minutes_allowed");
            CourseInfo courseInfo = courseInfoRepository.findByCourseID(course_id);
            String student_guest = jsonObject.getString("student_guest");
            if (courseInfo == null) {
                logger.warn("Failed to find course with ID: " + course_id);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Failed to find course with ID: " + course_id);
            }
            UserInfo userInfo = userInfoRepository.findStudentById(userID);
            if (userInfo == null) {
                logger.warn("Failed to find teacher with ID: " + userID);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to find user with ID: " + userID);
            }
            // Test test;
            // if(student_guest == "guest"){
            // test = new Test(null,null,null,description, section_name, minutes_allowed,
            // passing_score,
            // examDate, exam_start_time, exam_end_time, exam_status, "false",
            // "null",student_guest);
            // }else{
            Test test = new Test(null, courseInfo, userInfo, description, section_name, minutes_allowed, passing_score,
                    examDate, exam_start_time, exam_end_time, exam_status, "false", "null", student_guest, 0);

            // origin // Test test = new Test(test_id, courseInfo, userInfo, description,
            // section_name, minutes_allowed,
            // passing_score, examDate, exam_start_time, exam_end_time, exam_status,
            // "false", "null");

            logger.info("Initiate to Operation Insert Table Test Data {}", test.display());
            testRepository.save(test);
            logger.info("Operation Insert Table Test Data {} | Success", test.display());

            if (exam_status.equals("Result Released")) {
                List<TestExaminee> TestExaminees = TestExamineeRepository.getExamineeByTest(test_id);
                for (TestExaminee student : TestExaminees) {
                    int total_acquired_mark = 0;
                    int total_mark = 0;
                    TestResult viewExamResult;
                    if (test.getExam_target() == 1) {
                        viewExamResult = resultRepo.getResultByTestIdAndUser(test_id,
                                student.getGuestUser().getGuest_id());
                    } else {
                        viewExamResult = resultRepo.getResultByTestIdAndUser(test_id,
                                student.getUserInfo().getUid());
                    }
                    if (viewExamResult == null) {
                        List<TestExamineeAnswer> studentAnswerList = TestExamineeAnswerRepository
                                .getStudentAnswerListByTestAndStudent(student.getUserInfo().getUid(),
                                        test_id);
                        for (TestExamineeAnswer studentAnswer : studentAnswerList) {
                            int acquired_mark = studentAnswer.getAcquired_mark();
                            int max_mark = studentAnswer.getQuestion().getMaximum_mark();
                            total_acquired_mark += acquired_mark;
                            total_mark += max_mark;
                        }
                        Float ftotal_acquired_mark = (float) (total_acquired_mark);
                        Float ftotal_mark = (float) (total_mark);
                        int passing_score_percent = test.getPassing_score_percent();
                        Float fcalculate_percent = (float) (ftotal_acquired_mark / ftotal_mark);
                        fcalculate_percent = fcalculate_percent * 100;
                        if (fcalculate_percent > passing_score_percent) {
                            resultRepo.save(viewExamResult);
                        } else {
                            resultRepo.save(viewExamResult);
                        }
                    } else {
                        List<TestExamineeAnswer> studentAnswerList = TestExamineeAnswerRepository
                                .getStudentAnswerListByTestAndStudent(student.getUserInfo().getUid(),
                                        test_id);
                        for (TestExamineeAnswer studentAnswer : studentAnswerList) {
                            int acquired_mark = studentAnswer.getAcquired_mark();
                            int max_mark = studentAnswer.getQuestion().getMaximum_mark();
                            total_acquired_mark += acquired_mark;
                            total_mark += max_mark;
                        }
                        Float ftotal_acquired_mark = (float) (total_acquired_mark);
                        Float ftotal_mark = (float) (total_mark);
                        int passing_score_percent = test.getPassing_score_percent();
                        Float fcalculate_percent = (float) (ftotal_acquired_mark / ftotal_mark);
                        fcalculate_percent = fcalculate_percent * 100;
                        if (fcalculate_percent > passing_score_percent) {

                            viewExamResult.setResult("Passed");
                            viewExamResult.setResultMark(total_acquired_mark);
                            logger.info("Initiate to Operation Insert Table Result Data {}", viewExamResult.display());
                            resultRepo.save(viewExamResult);
                            logger.info("Operation Insert Table Result Data {} | Success", viewExamResult.display());
                        } else {

                            viewExamResult.setResult("Failed");
                            viewExamResult.setResultMark(total_acquired_mark);
                            logger.info("Initiate to Operation Insert Table Result Data {}", viewExamResult.display());
                            resultRepo.save(viewExamResult);
                            logger.info("Operation Insert Table Result Data {} | Success", viewExamResult.display());
                        }

                    }
                }
            }
            logger.info("Called editTeacherExam with parameter(payload={}) Success", payload);
            logger.info("user_id: {}", userID);
            return ResponseEntity.ok(HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping(value = { "/admin/testInfo" })
    public ResponseEntity<TestInfo> getTestInfo(@RequestParam(required = false) Long test_id) {

        try {

            Test testData = testRepository.getTestByID(test_id);
            logger.info("Called getTestInfo with parameter(test_id={}) Success", test_id);
            TestInfo testInfo = new TestInfo();
            testInfo.test_id = testData.getTest_id();
            testInfo.questionCount = (long) testData.getTestQuestions().size();
            testInfo.examineeCount = (long) testData.getTestExaminee().size();
            testInfo.examTarget = testData.getExam_target();
            return ResponseEntity.ok(testInfo);
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }

    @PostMapping(value = { "/admin/launchExam" })
    public ResponseEntity launchExam(@RequestParam(required = false) Long test_id) {

        Test testData = testRepository.getTestByID(test_id);
        logger.info("Called launchExam with parameter(test_id={}) Success", test_id);
        logger.info("{}", testData.getTestExaminee().size());
        List<TestExaminee> examineeList = testData.getTestExaminee();

        testData.setIsLaunch(true);
        testRepository.save(testData);

        for (TestExaminee examinee : examineeList) {
            try {
                String mail1 = examinee.getUserInfo().getUserAccount().getMail();
                logger.info(mail1);
                if (mail1 != null) {
                    mailService.guestsendVerificationMail(examinee.getUserInfo().getUserName(),
                            mail1, test_id.toString(), testData);
                }
            } catch (Exception e) {
                logger.info(e.toString());
            }
            try {
                String mail2 = examinee.getGuestUser().getMail();
                logger.info(mail2);
                if (mail2 != null) {
                    mailService.guestsendVerificationMail(examinee.getGuestUser().getName(),
                            mail2, test_id.toString(), testData);
                }
            }

            catch (MessagingException e) {
                logger.info(e.toString());
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            // do something with examinee

        }

        return ResponseEntity.ok(HttpStatus.OK);

    }

    private Long getUid() {
        Long uid = userSessionService.getUserAccount().getAccountId();
        return uid;
    }

    private String getUserRole() {
        String userRole = userSessionService.getUserAccount().getRole();
        return userRole;
    }
}