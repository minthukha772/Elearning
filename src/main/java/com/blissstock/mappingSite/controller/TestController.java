package com.blissstock.mappingSite.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

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
import com.blissstock.mappingSite.entity.JoinCourseUser;
import com.blissstock.mappingSite.entity.PaymentHistory;
import com.blissstock.mappingSite.entity.Result;
import com.blissstock.mappingSite.entity.Test;
import com.blissstock.mappingSite.entity.TestStudent;
import com.blissstock.mappingSite.entity.TestStudentAnswer;
import com.blissstock.mappingSite.entity.UserInfo;
import com.blissstock.mappingSite.repository.CourseInfoRepository;
import com.blissstock.mappingSite.repository.JoinCourseUserRepository;
import com.blissstock.mappingSite.repository.ResultRepository;
import com.blissstock.mappingSite.repository.TestRepository;
import com.blissstock.mappingSite.repository.TestStudentAnswerRepository;
import com.blissstock.mappingSite.repository.TestStudentRepository;
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
    UserRepository userRepository;

    @Autowired
    private ResultRepository resultRepo;

    @Autowired
    private TestStudentRepository testStudentRepository;

    @Autowired
    private TestStudentAnswerRepository testStudentAnswerRepository;

    @Autowired
    private JoinCourseUserRepository joinCourseUserRepository;

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
            logger.info("user id {} start processing URL /teacher/exam", userID);
            if (examStatus != "" || courseid != "" || fromDate != "" || toDate != "") {
                if (examStatus != "") {
                    if (examStatus.equals("Deleted")) {
                        testList = testRepository.getDeletedListByUser(userID);
                        logger.info("userid {} get test list with user id and exam status by test id {}", userID,
                                testList);
                        model.addAttribute("testList", testList);
                        model.addAttribute("filterType", "Filter By Status");
                        model.addAttribute("filter", "( " + examStatus + " )");
                    } else if (!examStatus.equals("Deleted")) {
                        testList = testRepository.getListByStatusAndUser(examStatus, userID);
                        logger.info("userid {} get test list with user id and exam status by test id {}", userID,
                                testList);
                        model.addAttribute("testList", testList);
                        model.addAttribute("filterType", "Filter By Status");
                        model.addAttribute("filter", "( " + examStatus + " )");
                    }
                } else if (courseid != "") {
                    CourseInfo course = courseInfoRepository.getById(Long.parseLong(courseid));
                    testList = testRepository.getListByCourseAndUser(Long.parseLong(courseid), userID);
                    logger.info("userid {} get test list with user id and course info by test id {}", userID, testList);
                    model.addAttribute("testList", testList);
                    model.addAttribute("filterType", "Filter By Course");
                    model.addAttribute("filter", "( " + course.getCourseName() + " )");
                } else if (fromDate != "" && toDate != "") {
                    Date from = new SimpleDateFormat("yyyy-MM-dd").parse(fromDate);
                    Date to = new SimpleDateFormat("yyyy-MM-dd").parse(toDate);
                    testList = testRepository.getListByDateAndUser(from, to, userID);
                    logger.info("userid {} get test list with user id and from date to date by test id {}", userID,
                            testList);
                    model.addAttribute("testList", testList);
                    model.addAttribute("filterType", "Filter By Date");
                    model.addAttribute("filter", "( " + fromDate + " - " + toDate + " )");
                }
            } else {
                testList = testRepository.getListByUser(userID);
                logger.info("user id {} get test list by userid", userID);
                model.addAttribute("testList", testList);
            }

            model.addAttribute("role", "teacher");
            courseList = courseInfoRepository.findByUID(userID);
            model.addAttribute("courseList", courseList);

            logger.info("User " + userID + " Received response from URL: /teacher/exam with status code: 200");
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
            logger.info("user id {} start processing URL /teacher/exam", userID);
            if (examStatus != "" || courseid != "" || fromDate != "" || toDate != "" || teacherid != "") {
                if (examStatus != "") {
                    testList = testRepository.getListByStatusAndStudentId(examStatus, userID);
                    // logger.info("userid {} get test list with user id and exam status by test id
                    // {}", userID, testList);
                    model.addAttribute("testList", testList);
                    model.addAttribute("filterType", "Filter By Status");
                    model.addAttribute("filter", "( " + examStatus + " )");
                } else if (courseid != "") {

                    // Log the test list retrieval by course ID
                    logger.info("user id {} Retrieving test list by course ID: {}", userID, courseid);
                    CourseInfo course = courseInfoRepository.getById(Long.parseLong(courseid));
                    testList = testRepository.getListByCourse(Long.parseLong(courseid));
                    model.addAttribute("testList", testList);
                    model.addAttribute("filterType", "Filter By Course");
                    model.addAttribute("filter", "( " + course.getCourseName() + " )");

                } else if (fromDate != "" && toDate != "") {
                    Date from = new SimpleDateFormat("yyyy-MM-dd").parse(fromDate);
                    Date to = new SimpleDateFormat("yyyy-MM-dd").parse(toDate);
                    testList = testRepository.getListByDateAndStudentId(from, to, userID);
                    // logger.info("userid {} get test list with user id and from date to date by
                    // test id {}", userID,
                    // testList);
                    model.addAttribute("testList", testList);
                    model.addAttribute("filterType", "Filter By Date");
                    model.addAttribute("filter", "( " + fromDate + " - " + toDate + " )");
                } else if (teacherid != "") {
                    // Log the test list retrieval by teacher ID
                    logger.info("user id {} Retrieving test list by teacher ID: {}", userID, teacherid);
                    UserInfo teacher = userRepository.findByAccount(Long.parseLong(teacherid));
                    testList = testRepository.getListByUser(Long.parseLong(teacherid));
                    model.addAttribute("testList", testList);
                    model.addAttribute("filterType", "Filter By Teacher");
                    model.addAttribute("filter", "( " + teacher.getUserName() + " )");
                }
            } else {
                testList = testRepository.getListByStudent(userID);
                // logger.info("user id {} get test list by userid", userID);
                model.addAttribute("testList", testList);
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
                    logger.info("Called AT_000004 with parameters: examStatus={}", examStatus);
                    responseString = "Called AT_000004 with parameters: examStatus=" + examStatus + " Success";
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
                    responseString = "Called AT_000004 with parameters: courseid=" + courseid + " Success";
                    logger.info("Called AT_000004 with parameters: course={}", courseid);
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
                    responseString = "Called AT_000004 with parameters: fromDate=" + fromDate + " toDate=" + toDate
                            + " Success";
                    logger.info("Called AT_000004 with parameters: fromDate={} and toDate={}", fromDate, toDate);
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
                    responseString = "Called AT_000004 with parameters: teacherid=" + teacherid + " Success";
                    logger.info("Called AT_000004 with parameters: teacher={}", teacherid);
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
                responseString = "Called AT_000004 with parameters: None Success";
                logger.info("Called AT_000004 with parameters: None");
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
                LocalDateTime currentDateTime = LocalDateTime.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                String deletedAt = currentDateTime.format(formatter);

                testData.setDeletedAt(deletedAt);
                testData.setIsDelete("true");
                testRepository.save(testData);
            }
            if (isDelete.equals("true")) {
                testData.setDeletedAt("null");
                testData.setIsDelete("false");
                testRepository.save(testData);
            }
            // testRepository.deleteById(test_id);
            // logger.info("User ID {} Exam with ID {} Successfully deleted", userID,
            // test_id);
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
                LocalDateTime currentDateTime = LocalDateTime.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                String deletedAt = currentDateTime.format(formatter);
                testData.setDeletedAt(deletedAt);
                testData.setIsDelete("true");
                testRepository.save(testData);
            }
            if (isDelete.equals("true")) {
                testData.setDeletedAt("null");
                testData.setIsDelete("false");
                testRepository.save(testData);
            }
            // testRepository.deleteById(test_id);
            // logger.info("User ID {} Exam with ID {} Successfully deleted", userID,
            // test_id);
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
            Long teacherId = Long.parseLong(teacher_id);
            List<CourseInfo> courseInfos = courseInfoRepository.findByUID(teacherId);
            logger.info("User ID {} Retrieved {} course(s) for teacher with ID {}", userID, courseInfos.size(),
                    teacherId);
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
            CourseInfo courseInfo = courseInfoRepository.findByCourseID(course_id);
            if (courseInfo == null) {
                logger.warn("Failed to find course with ID: " + course_id);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Failed to find course with ID: " + course_id);
            }
            UserInfo userInfo = userInfoRepository.findStudentById(userID);
            if (userInfo == null) {
                logger.warn("Failed to find user with ID: " + userID);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to find user with ID: " + userID);
            }
            Test test = new Test(null, courseInfo, userInfo, description, section_name, minutes_allowed, passing_score,
                    examDate, exam_start_time, exam_end_time, exam_status, "false", "null");
            logger.info("User ID {} Exam start to insert test {}", userID, test);
            testRepository.save(test);
            logger.info("User ID {} Exam saved Successfully test {}", userID, test);
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
            logger.info("Called saveExamByAdmin");
            Long userID = getUid();
            JSONObject jsonObject = new JSONObject(payload);
            Long teacher_id = jsonObject.getLong("teacher_id");
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
            if (courseInfo == null) {
                logger.warn("Operation Retrieve Table course_info by query course_id = {} Result No Data",
                        course_id);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Failed to find course with ID: " + course_id);
            }
            UserInfo userInfo = userInfoRepository.findStudentById(teacher_id);
            if (userInfo == null) {
                logger.warn("Operation Retrieve Table user_info, user_account by query user_id = {} Result No Data",
                        teacher_id);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to find user with ID: " + teacher_id);
            }
            Test test = new Test(null, courseInfo, userInfo, description, section_name, minutes_allowed, passing_score,
                    examDate, exam_start_time, exam_end_time, exam_status, "false", "null");
            logger.info("Initiate to Operation Insert Table Test Data {}", test.display());
            testRepository.save(test);
            logger.info("Operation Insert Table Test Data {} Success", test.display());

            logger.info("Called saveExamByAdmin Success");
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
            UserInfo userInfo = userInfoRepository.findStudentById(teacher_id);
            if (userInfo == null) {
                logger.warn("Operation Retrieve Table user_info, user_account by query user_id = {} Result No Data",
                        teacher_id);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to find user with ID: " + teacher_id);
            }
            Test test = new Test(test_id, courseInfo, userInfo, description, section_name, minutes_allowed,
                    passing_score,
                    examDate, exam_start_time, exam_end_time, exam_status, "false", "null");
            logger.info("User ID {} Exam start to update test {}", userID, test);
            testRepository.save(test);
            logger.info("User ID {} Exam edited Successfully for course ID {} and exam ID {}", userID, course_id,
                    test_id);

            if (exam_status.equals("Result Released")) {
                List<TestStudent> testStudents = testStudentRepository.getStudentByTest(test_id);
                for (TestStudent student : testStudents) {
                    int total_acquired_mark = 0;
                    int total_mark = 0;
                    Result viewExamResult = resultRepo.getResultByTestIdAndUser(test_id,
                            student.getUserInfo().getUid());
                    if (viewExamResult == null) {
                        List<TestStudentAnswer> studentAnswerList = testStudentAnswerRepository
                                .getStudentAnswerListByTestAndStudent(student.getUserInfo().getUid(),
                                        test_id);
                        for (TestStudentAnswer studentAnswer : studentAnswerList) {
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
                        UserInfo studentInfo = userInfoRepository.findStudentById(student.getUserInfo().getUid());
                        if (fcalculate_percent > passing_score_percent) {
                            Result result = new Result(null, test, studentInfo, total_acquired_mark, "Passed", "");
                            resultRepo.save(result);
                        } else {
                            Result result = new Result(null, test, studentInfo, total_acquired_mark, "Failed", "");
                            resultRepo.save(result);
                        }
                    } else {
                        List<TestStudentAnswer> studentAnswerList = testStudentAnswerRepository
                                .getStudentAnswerListByTestAndStudent(student.getUserInfo().getUid(),
                                        test_id);
                        for (TestStudentAnswer studentAnswer : studentAnswerList) {
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
                        UserInfo studentInfo = userInfoRepository.findStudentById(student.getUserInfo().getUid());
                        if (fcalculate_percent > passing_score_percent) {

                            viewExamResult.setResult("Passed");
                            viewExamResult.setResultMark(total_acquired_mark);
                            resultRepo.save(viewExamResult);
                        } else {

                            viewExamResult.setResult("Failed");
                            viewExamResult.setResultMark(total_acquired_mark);
                            resultRepo.save(viewExamResult);
                        }

                    }

                }
            }

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
            Test test = new Test(test_id, courseInfo, userInfo, description, section_name, minutes_allowed,
                    passing_score,
                    examDate, exam_start_time, exam_end_time, exam_status, "false", "null");
            logger.info("User ID {} Exam start to update test {}", userID, test);
            testRepository.save(test);
            logger.info("User ID {} Exam edited Successfully for course ID {} and exam ID {}", userID, course_id,
                    test_id);

            if (exam_status.equals("Result Released")) {
                List<TestStudent> testStudents = testStudentRepository.getStudentByTest(test_id);
                for (TestStudent student : testStudents) {
                    int total_acquired_mark = 0;
                    int total_mark = 0;
                    Result viewExamResult = resultRepo.getResultByTestIdAndUser(test_id,
                            student.getUserInfo().getUid());
                    if (viewExamResult == null) {
                        List<TestStudentAnswer> studentAnswerList = testStudentAnswerRepository
                                .getStudentAnswerListByTestAndStudent(student.getUserInfo().getUid(),
                                        test_id);
                        for (TestStudentAnswer studentAnswer : studentAnswerList) {
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
                        UserInfo studentInfo = userInfoRepository.findStudentById(student.getUserInfo().getUid());

                        if (fcalculate_percent > passing_score_percent) {
                            Result result = new Result(null, test, studentInfo, total_acquired_mark, "Passed", "");
                            resultRepo.save(result);
                        } else {
                            Result result = new Result(null, test, studentInfo, total_acquired_mark, "Failed", "");
                            resultRepo.save(result);
                        }
                    } else {
                        List<TestStudentAnswer> studentAnswerList = testStudentAnswerRepository
                                .getStudentAnswerListByTestAndStudent(student.getUserInfo().getUid(),
                                        test_id);
                        for (TestStudentAnswer studentAnswer : studentAnswerList) {
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
                        UserInfo studentInfo = userInfoRepository.findStudentById(student.getUserInfo().getUid());
                        if (fcalculate_percent > passing_score_percent) {

                            viewExamResult.setResult("Passed");
                            viewExamResult.setResultMark(total_acquired_mark);
                            resultRepo.save(viewExamResult);
                        } else {

                            viewExamResult.setResult("Failed");
                            viewExamResult.setResultMark(total_acquired_mark);
                            resultRepo.save(viewExamResult);
                        }

                    }
                }
            }
            return ResponseEntity.ok(HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    private Long getUid() {
        Long uid = userSessionService.getUserAccount().getAccountId();
        return uid;
    }
}