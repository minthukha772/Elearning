package com.blissstock.mappingSite.controller;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.lang.reflect.Array;
import java.nio.charset.UnsupportedCharsetException;
import java.security.SecureRandom;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import javax.validation.Valid;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity.BodyBuilder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.blissstock.mappingSite.entity.CourseInfo;
import com.blissstock.mappingSite.entity.GuestUser;
import com.blissstock.mappingSite.entity.JoinCourseUser;
import com.blissstock.mappingSite.entity.Test;
import com.blissstock.mappingSite.entity.TestQuestion;
import com.blissstock.mappingSite.entity.UserAccount;
import com.blissstock.mappingSite.entity.TestExaminee;
import com.blissstock.mappingSite.entity.UserInfo;
import com.blissstock.mappingSite.model.TestExamineeWithMarkedCountModel;
import com.blissstock.mappingSite.repository.GuestUserRepository;
import com.blissstock.mappingSite.repository.JoinCourseUserRepository;
import com.blissstock.mappingSite.repository.TestQuestionRepository;
import com.blissstock.mappingSite.repository.TestRepository;
import com.blissstock.mappingSite.repository.TestExamineeAnswerRepository;
import com.blissstock.mappingSite.repository.TestExamineeRepository;
import com.blissstock.mappingSite.repository.UserAccountRepository;
import com.blissstock.mappingSite.repository.UserInfoRepository;
import com.blissstock.mappingSite.service.MailService;
import com.blissstock.mappingSite.service.UserSessionService;
import com.google.gson.Gson;

import org.springframework.ui.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class TestExamineeController {
    private static Logger logger = LoggerFactory.getLogger(
            TestExamineeController.class);

    @Autowired
    UserSessionService userSessionService;

    @Autowired
    TestExamineeRepository testExamineeRepository;

    @Autowired
    TestExamineeRepository testStudentRepository;

    @Autowired
    TestExamineeAnswerRepository testStudentAnswerRepository;

    @Autowired
    TestRepository testRepository;

    @Autowired
    TestQuestionRepository testQuestionRepository;

    @Autowired
    JoinCourseUserRepository joinCourseUserRepository;

    @Autowired
    UserAccountRepository userAccountRepository;

    @Autowired
    UserInfoRepository userInfoRepository;

    @Autowired
    GuestUserRepository guestUserRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    MailService mailService;

    @Valid
    @GetMapping(value = { "/teacher/exam/{test_id}/examinee", "/admin/exam/{test_id}/examinee" })
    private String getTestExaminee(@PathVariable Long test_id, Model model,

            @RequestParam(required = false) String name)
            throws ParseException {
        logger.info("API name : {}.Parameter : {}", "getTestExaminee", test_id);
        logger.info("Operation Retrieve Table {} by query : findByNameandTestId {} {}", "TestExaminee", name, test_id);
        logger.info("Initiate to Operation Retrieve Table {} by query : findByNameandTestId {}", "TestExaminee", name,
                test_id);
        List<TestExaminee> testStudents = new ArrayList<>();
        List<TestExamineeWithMarkedCountModel> testStudentList = new ArrayList<>();
        int checked_students = 0;
        int total_free_questions = 0;
        if (name == null) {

            testStudents = testStudentRepository.getStudentByTest(test_id);
        } else {
            logger.info("Initiate to Operation Retrieve Table {} by query :findByNameandTestId{}{}", "TestExaminee",
                    name, test_id);
            testStudents = testStudentRepository.findByNameandTestId(name, test_id);
            logger.info("Operation Retrieve Table {} by query :findByNameandTestId{}{}Result List : {} Success",
                    "TestExaminee", name, test_id, testStudents.toString());
        }
        total_free_questions = testQuestionRepository.getFreeAnswerCount(test_id);
        for (TestExaminee TestExaminee : testStudents) {
            Integer answerCount = testStudentAnswerRepository.getCountStudentAnswerListByTestAndStudent(test_id,
                    TestExaminee.getUserInfo().getUid());
            if (answerCount == 0) {
                TestExamineeWithMarkedCountModel testStudentWithMarkedCountModel = new TestExamineeWithMarkedCountModel(
                        TestExaminee.getId(), TestExaminee.getTest(), TestExaminee.getUserInfo(), total_free_questions,
                        0);
                testStudentList.add(testStudentWithMarkedCountModel);
            } else {
                int uncheck_free_questions = testStudentAnswerRepository.getUnCheckAnswerCountByTestAndStudent(
                        test_id,
                        TestExaminee.getUserInfo().getUid());
                TestExamineeWithMarkedCountModel testStudentWithMarkedCountModel = new TestExamineeWithMarkedCountModel(
                        TestExaminee.getId(), TestExaminee.getTest(), TestExaminee.getUserInfo(), total_free_questions,
                        total_free_questions - uncheck_free_questions);
                if (uncheck_free_questions == 0) {
                    checked_students++;
                }
                testStudentList.add(testStudentWithMarkedCountModel);
            }
        }
        logger.info("API name : {}. Parameter : {}", "getTestExaminee", test_id);
        logger.info("Initiate to Operation Save File {}", test_id);
        model.addAttribute("user_role", userSessionService.getRole());
        model.addAttribute("test_id", test_id);
        model.addAttribute("test_examinees", testStudentList);
        model.addAttribute("total_students", testStudents.size());
        model.addAttribute("check_students", checked_students);
        return "AT0005_TestExamineeList.html";
    }

    @Valid
    @GetMapping(value = { "/teacher/get-student", "/admin/get-student" })
    private ResponseEntity getCustomStudent(@RequestParam(value = "name") String name)
            throws ParseException {
        logger.info("API name : {}.Parameter : {}", "getCustomStudent", name);
        logger.info("Initiate to Operation Insert Table {} Data {}", "TestExaminee", name);
        String lowerName = name.toLowerCase();
        List<UserInfo> testStudents = userInfoRepository.findByName(name, lowerName);
        return ResponseEntity.ok(testStudents);
    }

    @Valid
    @GetMapping(value = { "/teacher/get-student-exam", "/admin/get-student-exam" })
    private ResponseEntity getCustomStudentExam(@RequestParam(value = "name") String name,
            @RequestParam(value = "test_id") Long test_id)
            throws ParseException {
        logger.info("API name : {}.Parameter : {}", "getCustomStudentExam", name);
        logger.info("Operation Retrieve Table {} by query : findByNameandTestId {} {}", "TestExaminee", name, test_id);

        List<UserInfo> testStudents = userInfoRepository.findByNameandTestId(name, test_id);
        return ResponseEntity.ok(testStudents);
    }

    @Valid
    @PostMapping(value = { "/teacher/set-enrolled-examinee", "/admin/set-enrolled-examinee" })
    private String setEnrolledStudents(@RequestBody String testid)
            throws ParseException {
        logger.info("API name : {}.Parameter : {}", "setEnrolledStudents", testid);
        logger.info("Operation Retrieve Table {} by query : findByNameandTestId {} {}", "TestExaminee", testid);
        logger.info("Initiate to Operation Retrieve Table {} by query : findByNameandTestId {}", "TestExaminee",
                testid);
        JSONObject jsonObject = new JSONObject(testid);
        Long test_id = jsonObject.getLong("test_id");
        Test test = testRepository.getTestByID(test_id);
        if (test.getExam_status().equals("Exam Created")) {
            CourseInfo course = test.getCourseInfo();
            List<JoinCourseUser> enrolledList = joinCourseUserRepository.findByStudentByCourseID(course.getCourseId());
            for (JoinCourseUser student : enrolledList) {
                TestExaminee checkStudent = testStudentRepository.getStudentByID(student.getUserInfo().getUid(),
                        test_id);
                if (checkStudent == null) {
                    TestExaminee TestExaminee = new TestExaminee(null, test, student.getUserInfo(), null, null);
                    testStudentRepository.save(TestExaminee);
                }
            }
        }
        logger.info("API name : {}. Parameter : {}", "setCustomStudents", testid);
        logger.info("Operation Insert Table {} Data {} Success", "TestExaminee", testid);
        logger.info("Operation Save File {} Success", testid);
        return "AT0005_TestExamineeList.html";
    }

    @Valid
    @PostMapping(value = { "/teacher/set-examinee", "/admin/set-examinee" })
    private String setCustomStudents(@RequestBody String testid)
            throws ParseException {
        logger.info("API name : {}.Parameter : {}", "setCustomStudents", testid);
        logger.info("Operation Retrieve Table {} by query : findByNameandTestId {} {}", "TestExaminee", testid);
        logger.info("Initiate to Operation Retrieve Table {} by query : findByNameandTestId {}", "TestExaminee",
                testid);
        logger.info("Initiate to Operation Insert Table {} Data {}", "TestExaminee", testid);
        logger.info("Initiate to Operation Save File {}", testid);
        JSONObject jsonObject = new JSONObject(testid);
        Long test_id = jsonObject.getLong("test_id");
        Long student_id = jsonObject.getLong("student_id");
        Test test = testRepository.getTestByID(test_id);
        UserInfo user = userInfoRepository.findStudentById(student_id);
        if (test.getExam_status().equals("Exam Created")) {
            TestExaminee existingStudent = testStudentRepository.getStudentByID(student_id, test_id);
            if (existingStudent == null) {
                logger.info("Initiate to Operation Retrieve Table {} by query :findByNameandTestId{}{}", "TestExaminee",
                        student_id, test_id);
                logger.info("Initiate to Operation Update Table {} Data {} By {} = {}", "TestExaminee", testid,
                        "student_id", student_id);
                TestExaminee TestExaminee = new TestExaminee(null, test, user, null, null);
                testStudentRepository.save(TestExaminee);
                logger.info("Operation Retrieve Table {} by query :findByNameandTestId{}{}Result List : {} Success",
                        "TestExaminee", student_id, test_id, TestExaminee.toString());
                logger.info("Operation Update Table {} Data {} By {} = {} Success", "TestExaminee", testid,
                        "student_id", student_id);
            }
        }
        logger.info("API name : {}. Parameter : {}", "setCustomStudents", testid);
        logger.info("Operation Insert Table {} Data {} Success", "TestExaminee", testid);
        logger.info("Operation Save File {} Success", testid);
        return "AT0005_TestExamineeList.html";
    }

    // Add multiple guest in an exam
    @Valid
    @PostMapping(value = { "/teacher/set-multi-guest-examinee", "/admin/set-multi-guest-examinee" })
    private ResponseEntity setMultiGuest(@RequestBody String data) throws ParseException {
        logger.info("set-multi-guest-examinee with parameter: {}", data);
        JSONObject jsonObject = new JSONObject(data);
        Long test_id = jsonObject.getLong("test_id");
        JSONArray exam_guest_users = jsonObject.getJSONArray("exam_guest_users");

        logger.info(
                "Initiate to Operation Retrieve Table {} by query {}",
                "test",
                "testRepository.getTestByID(test_id)");
        Test test = testRepository.getTestByID(test_id);
        logger.info(
                "Operation Retrieve Table {} by query {} Result List {} Success",
                "test",
                "testRepository.getTestByID(test_id)",
                test);

        if (test.getExam_status().equals("Exam Created") || test.getExam_status().equals("Questions Created")) {
            Gson gson = new Gson();
            try {
                String[][] guestUsers = gson.fromJson(exam_guest_users.toString(), String[][].class);
                for (int i = 0; i < guestUsers.length; i++) {
                    String email = guestUsers[i][1];

                    logger.info(
                            "Initiate to Operation Retrieve Table {} by query {}",
                            "user_account",
                            "userAccountRepository.findByMail(email)");
                    UserAccount registeredEmail = userAccountRepository.findByMail(email);
                    logger.info(
                            "Operation Retrieve Table {} by query {} Result List {} Success",
                            "user_account",
                            "userAccountRepository.findByMail(email)",
                            registeredEmail);

                    if (registeredEmail == null) {
                        logger.info(
                                "Initiate to Operation Retrieve Table {} by query {}",
                                "guest",
                                "GuestUserRepository.getGuestUserbyEmail(email)");
                        GuestUser emailExist = guestUserRepository.getGuestUserbyEmail(email);
                        logger.info(
                                "Operation Retrieve Table {} by query {} Result List {} Success",
                                "guest",
                                "GuestUserRepository.getGuestUserbyEmail(email)",
                                emailExist);

                        GuestUser guestUser;
                        String one_time_password = "";

                        if (emailExist == null) {
                            String name = guestUsers[i][0];
                            String phone_number = guestUsers[i][2];
                            one_time_password = createOneTimePassword();
                            String one_time_passwordEncoded = passwordEncoder.encode(one_time_password);
                            String password_update_date_time = getDateAndTime();

                            guestUser = new GuestUser(
                                    null,
                                    name,
                                    email,
                                    phone_number,
                                    one_time_passwordEncoded,
                                    password_update_date_time,
                                    null,
                                    null);

                            logger.info(
                                    "Initiate to Operation Insert Table {} Data {}",
                                    "guest",
                                    guestUser);
                            guestUserRepository.save(guestUser);
                            logger.info(
                                    " Operation Insert Table {} Data: name={}, mail={}, phone_no={} | Success",
                                    "guest",
                                    name,
                                    email,
                                    phone_number);

                            final String otp = one_time_password;

                            new Thread(new Runnable() {
                                public void run() {
                                    try {
                                        logger.info(
                                                "Initiate Operation to send One-Time-Password to mail={}",
                                                email);
                                        mailService.SendGuestOneTimePassword(guestUser, otp);
                                        logger.info(
                                                "Operation to send One-Time-Password to mail={} | Success",
                                                email);
                                    } catch (Exception e) {
                                        logger.error(e.getLocalizedMessage());
                                    }
                                }
                            }).start();

                        } else {
                            guestUser = emailExist;

                            logger.info(
                                    "Initiate to Operation Retrieve Table {} by query {}",
                                    "test_examinee",
                                    "testExamineeRepository.findByTestIdAndGuestId(test_id, guestUser.getGuest_id())");
                            TestExaminee testExamineeGuest = testExamineeRepository.findByTestIdAndGuestId(test_id,
                                    guestUser.getGuest_id());
                            logger.info(
                                    "Operation Retrieve Table {} by query {} Result List {} Success",
                                    "test_examinee",
                                    "testExamineeRepository.findByTestIdAndGuestId(test_id, guestUser.getGuest_id())",
                                    testExamineeGuest);

                            if (testExamineeGuest != null) {
                                continue;
                            }
                        }

                        TestExaminee testExaminee = new TestExaminee(
                                null,
                                test,
                                null,
                                guestUser,
                                null);

                        logger.info(
                                "Initiate to Operation Insert Table {} Data: test={}, guest_user={}",
                                "test_examinee",
                                test,
                                guestUser);
                        testExamineeRepository.save(testExaminee);
                        logger.info(
                                "Operation Insert Table {} Data: test={}, guest_user={} | Success",
                                "test_examinee",
                                test,
                                guestUser);
                    }

                    logger.info(
                            "set-multi-guest-examinee with parameter: test_id={}, data={} Success",
                            test_id,
                            exam_guest_users);
                }
            } catch (Exception e) {
                logger.error(e.getLocalizedMessage());
            }
        }
        return ResponseEntity.ok(HttpStatus.OK);
    }

    private String createOneTimePassword() {

        final int LENGTH = 8;
        SecureRandom random = new SecureRandom();
        String lowercase = "abcdefghijklmnopqrstuvwxyz";
        String uppercase = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String digits = "0123456789";
        String allChars = lowercase + uppercase + digits;

        StringBuilder password = new StringBuilder();

        for (int i = 0; i < LENGTH; i++) {
            int randomIndex = random.nextInt(allChars.length());
            char randomChar = allChars.charAt(randomIndex);
            password.append(randomChar);
        }

        return password.toString();
    }

    private String getDateAndTime() {
        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = dateFormat.format(currentDate);
        return formattedDate;
    }

    private Long getUid() {
        Long uid = userSessionService.getUserAccount().getAccountId();
        return uid;
    }
}