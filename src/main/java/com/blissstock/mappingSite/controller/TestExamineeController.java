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

import org.aspectj.weaver.tools.Trace;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.repository.CrudRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity.BodyBuilder;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

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
import com.blissstock.mappingSite.enums.UserRole;
import com.blissstock.mappingSite.model.FileInfo;
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
import com.blissstock.mappingSite.service.StorageService;
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
        TestExamineeRepository testExamineeRepository;

        @Autowired
        TestExamineeAnswerRepository testExamineeAnswerRepository;

        @Autowired
        GuestUserRepository guestUserRepository;

        @Autowired
        private PasswordEncoder passwordEncoder;

        @Autowired
        MailService mailService;

        @Autowired
        StorageService storageService;

        // @Valid
        // @GetMapping(value = { "/teacher/exam/{test_id}/examinee",
        // "/admin/exam/{test_id}/examinee" })
        // private String getTestExaminee(@PathVariable Long test_id, Model model,
        // @RequestParam(required = false) String name)
        // throws ParseException {
        // try {

        // logger.info("API name : {}.Parameter : {}", "getTestExaminee", test_id);

        // Test test = testRepository.getTestByID(test_id);
        // String examStatus = test.getExam_status();

        // logger.info("Operation Retrieve Table {} by query : findByNameandTestId {}
        // {}", "TestExaminee", name,
        // test_id);
        // logger.info("Initiate to Operation Retrieve Table {} by query :
        // findByNameandTestId {}", "TestExaminee",
        // name, test_id);
        // List<TestExaminee> testStudents = new ArrayList<>();
        // List<TestExamineeWithMarkedCountModel> testStudentList = new ArrayList<>();
        // int checked_students = 0;
        // int total_free_questions = 0;
        // if (name == null) {

        // testStudents = testExamineeRepository.getExamineeByTest(test_id);
        // } else {
        // logger.info("Initiate to Operation Retrieve Table {} by query
        // :findByNameandTestId{}{}",
        // "TestExaminee",
        // name, test_id);
        // testStudents = testExamineeRepository.findByNameandTestId(name, test_id);
        // logger.info("Operation Retrieve Table {} by query
        // :findByNameandTestId{}{}Result List : {} Success",
        // "TestExaminee", name, test_id, testStudents.toString());
        // }
        // total_free_questions = testQuestionRepository.getFreeAnswerCount(test_id);
        // for (TestExaminee TestExaminee : testStudents) {
        // Integer answerCount =
        // testExamineeAnswerRepository.getCountStudentAnswerListByTestAndStudent(
        // test_id,
        // TestExaminee.getUserInfo().getUid());
        // if (answerCount == 0) {
        // TestExamineeWithMarkedCountModel testStudentWithMarkedCountModel = new
        // TestExamineeWithMarkedCountModel(
        // TestExaminee.getId(), TestExaminee.getTest(), TestExaminee.getUserInfo(),
        // total_free_questions,
        // 0);
        // testStudentList.add(testStudentWithMarkedCountModel);
        // } else {
        // int uncheck_free_questions =
        // testExamineeAnswerRepository.getUnCheckAnswerCountByTestAndStudent(
        // test_id,
        // TestExaminee.getUserInfo().getUid());
        // TestExamineeWithMarkedCountModel testStudentWithMarkedCountModel = new
        // TestExamineeWithMarkedCountModel(
        // TestExaminee.getId(), TestExaminee.getTest(), TestExaminee.getUserInfo(),
        // total_free_questions,
        // total_free_questions - uncheck_free_questions);
        // if (uncheck_free_questions == 0) {
        // checked_students++;
        // }
        // testStudentList.add(testStudentWithMarkedCountModel);
        // }
        // }
        // logger.info("API name : {}. Parameter : {} Return to
        // \"AT0005_TestExamineeList.html\" | Success",
        // "getTestExaminee", test_id);
        // model.addAttribute("user_role", userSessionService.getRole());
        // model.addAttribute("test_id", test_id);
        // model.addAttribute("exam_status", examStatus);
        // model.addAttribute("test_examinees", testStudentList);
        // model.addAttribute("total_students", testStudents.size());
        // model.addAttribute("check_students", checked_students);

        // return "AT0005_TestExamineeList.html";

        // } catch (Exception e) {
        // logger.error(e.getLocalizedMessage());
        // return "500";
        // }
        // }

        // @Valid
        // @GetMapping(value = { "/teacher/get-student", "/admin/get-student" })
        // private ResponseEntity getCustomStudent(@RequestParam(value = "name") String
        // name)
        // throws ParseException {
        // logger.info("API name : {}.Parameter : {}", "getCustomStudent", name);
        // logger.info("Initiate to Operation Insert Table {} Data {}", "TestExaminee",
        // name);
        // String lowerName = name.toLowerCase();
        // List<UserInfo> testStudents = userInfoRepository.findByName(name, lowerName);
        // return ResponseEntity.ok(testStudents);
        // }

        // @Valid
        // @GetMapping(value = { "/teacher/get-student-exam", "/admin/get-student-exam"
        // })
        // private ResponseEntity getCustomStudentExam(@RequestParam(value = "name")
        // String name,
        // @RequestParam(value = "test_id") Long test_id)
        // throws ParseException {
        // logger.info("API name : {}.Parameter : {}", "getCustomStudentExam", name);
        // logger.info("Operation Retrieve Table {} by query : findByNameandTestId {}
        // {}", "TestExaminee", name, test_id);

        // List<UserInfo> testStudents = userInfoRepository.findByNameandTestId(name,
        // test_id);
        // return ResponseEntity.ok(testStudents);
        // }

        // @Valid
        // @PostMapping(value = { "/teacher/set-enrolled-examinee",
        // "/admin/set-enrolled-examinee" })
        // private String setEnrolledStudents(@RequestBody String testid)
        // throws ParseException {
        // logger.info("API name : {}.Parameter : {}", "setEnrolledStudents", testid);
        // logger.info("Operation Retrieve Table {} by query : findByNameandTestId {}
        // {}", "TestExaminee", testid);
        // logger.info("Initiate to Operation Retrieve Table {} by query :
        // findByNameandTestId {}", "TestExaminee",
        // testid);
        // JSONObject jsonObject = new JSONObject(testid);
        // Long test_id = jsonObject.getLong("test_id");
        // Test test = testRepository.getTestByID(test_id);
        // if (test.getExam_status().equals("Exam Created")) {
        // CourseInfo course = test.getCourseInfo();
        // List<JoinCourseUser> enrolledList =
        // joinCourseUserRepository.findByStudentByCourseID(course.getCourseId());
        // for (JoinCourseUser student : enrolledList) {
        // TestExaminee checkStudent =
        // testExamineeRepository.getStudentByID(student.getUserInfo().getUid(),
        // test_id);
        // if (checkStudent == null) {
        // TestExaminee TestExaminee = new TestExaminee(null, test,
        // student.getUserInfo(), null, null);
        // testExamineeRepository.save(TestExaminee);
        // }
        // }
        // }
        // logger.info("API name : {}. Parameter : {}", "setCustomStudents", testid);
        // logger.info("Operation Insert Table {} Data {} Success", "TestExaminee",
        // testid);
        // logger.info("Operation Save File {} Success", testid);
        // return "AT0005_TestExamineeList.html";
        // }

        // @Valid
        // @PostMapping(value = { "/teacher/set-examinee", "/admin/set-examinee" })
        // private String setCustomStudents(@RequestBody String testid)
        // throws ParseException {
        // logger.info("API name : {}.Parameter : {}", "setCustomStudents", testid);
        // logger.info("Operation Retrieve Table {} by query : findByNameandTestId {}
        // {}", "TestExaminee", testid);
        // logger.info("Initiate to Operation Retrieve Table {} by query :
        // findByNameandTestId {}", "TestExaminee",
        // testid);
        // logger.info("Initiate to Operation Insert Table {} Data {}", "TestExaminee",
        // testid);
        // logger.info("Initiate to Operation Save File {}", testid);
        // JSONObject jsonObject = new JSONObject(testid);
        // Long test_id = jsonObject.getLong("test_id");
        // Long student_id = jsonObject.getLong("student_id");
        // Test test = testRepository.getTestByID(test_id);
        // UserInfo user = userInfoRepository.findStudentById(student_id);
        // if (test.getExam_status().equals("Exam Created")) {
        // TestExaminee existingStudent =
        // testExamineeRepository.getStudentByID(student_id, test_id);
        // if (existingStudent == null) {
        // logger.info("Initiate to Operation Retrieve Table {} by query
        // :findByNameandTestId{}{}", "TestExaminee",
        // student_id, test_id);
        // logger.info("Initiate to Operation Update Table {} Data {} By {} = {}",
        // "TestExaminee", testid,
        // "student_id", student_id);
        // TestExaminee TestExaminee = new TestExaminee(null, test, user, null, null);
        // testExamineeRepository.save(TestExaminee);
        // logger.info("Operation Retrieve Table {} by query
        // :findByNameandTestId{}{}Result List : {} Success",
        // "TestExaminee", student_id, test_id, TestExaminee.toString());
        // logger.info("Operation Update Table {} Data {} By {} = {} Success",
        // "TestExaminee", testid,
        // "student_id", student_id);
        // }
        // }
        // logger.info("API name : {}. Parameter : {}", "setCustomStudents", testid);
        // logger.info("Operation Insert Table {} Data {} Success", "TestExaminee",
        // testid);
        // logger.info("Operation Save File {} Success", testid);
        // return "AT0005_TestExamineeList.html";
        // }

        // @GetMapping(value = { "admin/exam/{test_id}/guest/examinee" })
        // private String getTestGuestExaminee(@PathVariable Long test_id, Model model,
        // @RequestParam(required = false) String name) throws ParseException {

        // try {
        // logger.info("Called API name: getTestGuestExaminee by Parameters:
        // test_id={}", test_id);

        // Test test = testRepository.getTestByID(test_id);
        // String examStatus = test.getExam_status();
        // List<TestExaminee> testGuests = new ArrayList<>();
        // List<TestExamineeWithMarkedCountModel> testGuestList = new ArrayList<>();

        // int checked_guests = 0;
        // int total_free_questions = 0;
        // if (name == null) {
        // logger.info("Initiate Operation Retrieve Table: test_examinee by Query:
        // getExamineeByTest({})",
        // test_id);
        // testGuests = testExamineeRepository.getExamineeByTest(test_id);
        // logger.info(
        // "Operation Retrieve Table: test_examinee by Query: getExamineeByTest({})
        // Result: numberOfTestGuests={} | Success",
        // test_id, testGuests.size());
        // } else {
        // logger.info(
        // "Initiate Operation Retrieve Table: test_examinee by Query:
        // findByGuestNameandTestId({}, {})",
        // name, test_id);
        // testGuests = testExamineeRepository.findByGuestNameandTestId(name, test_id);
        // logger.info(
        // "Operation Retrieve Table: test_examinee by Query:
        // findByGuestNameandTestId({}, {}) Result: {} | Success",
        // name, test_id, testGuests);
        // }
        // logger.info("Initiate Operation Retrieve Table: test_question by Query:
        // getFreeAnswerCount({})",
        // test_id);
        // total_free_questions = testQuestionRepository.getFreeAnswerCount(test_id);
        // logger.info(
        // "Operation Retrieve Table: test_question by Query: getFreeAnswerCount({})
        // Result: total_free_questions={} | Success",
        // test_id, total_free_questions);

        // for (TestExaminee testExaminee : testGuests) {

        // Integer answerCount =
        // testExamineeAnswerRepository.getCountStudentAnswerListByTestAndGuest(test_id,
        // testExaminee.getGuestUser().getGuest_id());
        // if (answerCount == 0) {
        // TestExamineeWithMarkedCountModel testGuestWithMarkedCountModel = new
        // TestExamineeWithMarkedCountModel(
        // testExaminee.getId(), testExaminee.getTest(), testExaminee.getGuestUser(),
        // total_free_questions,
        // 0);
        // testGuestList.add(testGuestWithMarkedCountModel);
        // } else {
        // // Left
        // int uncheck_free_questions =
        // testExamineeAnswerRepository.getUnCheckAnswerCountByTestAndGuest(
        // test_id, testExaminee.getGuestUser().getGuest_id());

        // TestExamineeWithMarkedCountModel testGuestWithMarkedCoundtModel = new
        // TestExamineeWithMarkedCountModel(
        // testExaminee.getId(), testExaminee.getTest(), testExaminee.getGuestUser(),
        // total_free_questions,
        // total_free_questions - uncheck_free_questions);
        // if (uncheck_free_questions == 0) {
        // checked_guests++;
        // }
        // testGuestList.add(testGuestWithMarkedCoundtModel);
        // }

        // }
        // logger.info(
        // "Called API name : getTestGuestExaminee by Parameter : {} Return to
        // \"AT0005_TestGuestList.html\" | Success",
        // "getTestExaminee", test_id);
        // model.addAttribute("user_role", userSessionService.getRole());
        // model.addAttribute("test_id", test_id);
        // model.addAttribute("exam_status", examStatus);
        // model.addAttribute("test_guests", testGuestList);
        // model.addAttribute("total_guests", testGuests.size());
        // model.addAttribute("check_guests", checked_guests);

        // return "AT0005_TestGuestList.html";

        // } catch (Exception e) {
        // logger.error(e.getLocalizedMessage());
        // return "500";
        // }

        // }

        // @Valid
        // @PostMapping(value = { "/teacher/set-single-guest", "/admin/set-single-guest"
        // })
        // private ResponseEntity setSingleGuest(@RequestBody String testid) throws
        // ParseException {

        // try {
        // logger.info("Called API name: setSingleGuest with Parameters: {}", testid);

        // JSONObject jsonObject = new JSONObject(testid);
        // Long test_id = jsonObject.getLong("test_id");
        // String name = jsonObject.getString("guest_name");
        // String email = jsonObject.getString("guest_email").toLowerCase();
        // String phone_number = jsonObject.getString("guest_ph_no");

        // Test test = testRepository.getTestByID(test_id);

        // if (test.getExam_status().equals("Exam Created") ||
        // test.getExam_status().equals("Questions Created")) {

        // // logger.info(
        // // "Operation Insert Table: guest | Data name:{}, mail:{}, phone_no:{},
        // // one_time_password:{}, password_update_date_time:{}, updated_date_time:{},
        // // deleted_date_time:{}",
        // // name);

        // // Check if the email exists in the user_account table
        // List<String> userAccountEmails = userAccountRepository.getAllUserEmail();
        // if (userAccountEmails.contains(email)) {
        // logger.warn("Opearation Insert Table: guest Data: name={}, mail={},
        // phone_no={} | Failed", name,
        // email, phone_number);
        // logger.warn("Email: {} already existes in Table: user_account | Operation
        // Cancelled", email);
        // return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        // .body("{\"errorMessage\": \"Added Email is already associated with a Student
        // account\"}");
        // }

        // // Check if the email exists in the guest and test table
        // GuestUser existingGuest =
        // guestUserRepository.findGuestUserByGuestEmailAndTestId(email, test_id);
        // if (existingGuest != null) {
        // logger.warn("Opearation Insert Table: guest Data: name={}, mail={},
        // phone_no={} | Failed", name,
        // email, phone_number);
        // logger.warn("Email: {} already existes in Table: guest for test_id={} |
        // Operation Cancelled", email,
        // test_id);
        // return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        // .body("{\"errorMessage\": \"Added Email already associated with a Guest user
        // within the Exam\"}");
        // }

        // // Check if the guest user email is already in the database
        // GuestUser guestUser = guestUserRepository.getGuestUserbyEmail(email);
        // if (guestUser == null) {
        // logger.info("Initiate Operation Insert Table: guest Data: name={}, mail={},
        // phone_no={}", name,
        // email,
        // phone_number);
        // String one_time_password = createOneTimePassword();
        // String one_time_passwordEncoded = passwordEncoder.encode(one_time_password);
        // String password_update_date_time = getDateAndTime();

        // GuestUser newGuestUser = new GuestUser(null, name, email, phone_number,
        // one_time_passwordEncoded,
        // password_update_date_time,
        // null, null);
        // guestUserRepository.save(newGuestUser);
        // logger.info(" Operation Insert Table: guest Data: name={}, mail={},
        // phone_no={} | Success", name,
        // email,
        // phone_number);

        // TestExaminee testExaminee = new TestExaminee(null, test, null, newGuestUser,
        // null);
        // logger.info("Initiate Operation Insert Table: test_examinee Data: test_id={},
        // guest_user={}",
        // test.getTest_id(),
        // newGuestUser.display());
        // testExamineeRepository.save(testExaminee);
        // logger.info("Operation Insert Table: test_examinee Data: test_id={},
        // guest_user={} | Success",
        // test.getTest_id(), newGuestUser.display());

        // new Thread(new Runnable() {
        // public void run() {
        // try {
        // mailService.SendGuestOneTimePassword(newGuestUser, one_time_password);
        // logger.info("One-time-password (OTP) sent to mail={} | Success", email);
        // } catch (Exception e) {
        // logger.error(e.getLocalizedMessage());
        // }
        // }
        // }).start();

        // } else {
        // guestUser.setName(name);
        // guestUser.setPhone_no(phone_number);
        // guestUser.setUpdated_date_time(getDateAndTime());
        // logger.info("Guest User with email={} is updated with name={}, phone_no={}",
        // email, name,
        // phone_number);
        // guestUserRepository.save(guestUser);

        // TestExaminee testExaminee = new TestExaminee(null, test, null, guestUser,
        // null);
        // logger.info("Initiate Operation Insert Table: test_examinee Data: test_id={},
        // guest_user={}",
        // test.getTest_id(), guestUser.display());
        // testExamineeRepository.save(testExaminee);
        // logger.info("Operation Insert Table: test_examinee Data: test_id={},
        // guest_user={} | Success",
        // test.getTest_id(), guestUser.display());
        // }

        // logger.info("Called API name: setSingleGuest with Parameters: {} | Success",
        // testid);

        // return ResponseEntity.ok(HttpStatus.OK);

        // }
        // return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

        // } catch (Exception e) {
        // logger.error(e.getLocalizedMessage());
        // return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        // // return "500";
        // }
        // }

        // @Valid
        // @PostMapping(value = { "/teacher/edit-single-guest",
        // "/admin/edit-single-guest" })
        // private ResponseEntity editSingleGuest(@RequestBody String body)
        // throws ParseException {

        // try {

        // logger.info("Called API name: editSingleGuest with Parameters: {}", body);

        // JSONObject jsonObject = new JSONObject(body);
        // Long test_id = jsonObject.getLong("test_id");
        // Long guest_id = jsonObject.getLong("guest_id");
        // String name = jsonObject.getString("guest_name");
        // String email = jsonObject.getString("guest_email").toLowerCase();
        // String phone_number = jsonObject.getString("guest_ph_no");

        // logger.info("Initiate Opearation Update Table: guest For guest_id={} Data:
        // name={}, mail={}, phone_no={}",
        // guest_id, name, email, phone_number);

        // // Check if the email exists in the user_account table
        // List<String> userAccountEmails = userAccountRepository.getAllUserEmail();
        // if (userAccountEmails.contains(email)) {
        // logger.warn("Opearation Update Table: guest Data: guest_id={}, name={},
        // mail={}, phone_no={} | Failed",
        // guest_id, name, email, phone_number);
        // logger.warn("Email: {} already existes in Table: user_account", email);
        // return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        // .body("{\"errorMessage\": \"Added Email is already associated with a Student
        // account\"}");
        // }

        // GuestUser existingGuest = guestUserRepository.findByGuestId(guest_id);
        // String newEncodedOneTimePassword;
        // String passwordUpdatedDateTime;
        // String updatedDateTime = getDateAndTime();

        // // Compare the newly added email with the existing guest email
        // if (existingGuest.getMail().equals(email)) {
        // GuestUser newGuestUser = new GuestUser(guest_id, name,
        // existingGuest.getMail(), phone_number,
        // existingGuest.getOne_time_password(),
        // existingGuest.getPassword_update_date_time(), updatedDateTime, null);
        // guestUserRepository.save(newGuestUser);
        // } else {

        // // Create a new password if not email doesn't match with the existing email
        // logger.info("New one-time-password is created for guest_id={}", guest_id);
        // String newOneTimePassword = createOneTimePassword();
        // newEncodedOneTimePassword = passwordEncoder.encode(newOneTimePassword);
        // passwordUpdatedDateTime = getDateAndTime();

        // GuestUser updatedGuestUser = new GuestUser(guest_id, name, email,
        // phone_number,
        // newEncodedOneTimePassword,
        // passwordUpdatedDateTime, updatedDateTime, null);
        // Test test = testRepository.getTestByID(test_id);

        // guestUserRepository.save(updatedGuestUser);

        // new Thread(new Runnable() {
        // public void run() {
        // try {
        // mailService.SendGuestRemovedNotification(existingGuest, test);
        // logger.info(
        // "Notified Email of Removal of guest user guest_id={} from exam test_id={}
        // sent to mail={} | Success",
        // guest_id, test_id, existingGuest.getMail());
        // } catch (Exception e) {
        // logger.info(e.getLocalizedMessage());
        // }
        // }
        // }).start();

        // new Thread(new Runnable() {
        // public void run() {
        // try {
        // mailService.SendGuestOneTimePassword(updatedGuestUser, newOneTimePassword);
        // logger.info("One-time-password (OTP) sent to mail={} | Success", email);
        // } catch (Exception e) {
        // logger.info(e.getLocalizedMessage());
        // }
        // }
        // }).start();
        // }
        // logger.info("Opearation Update Table: guest Data: guest_id={}, name={},
        // mail={}, phone_no={} | Success",
        // guest_id, name, email, phone_number);
        // logger.info("Called API name: editSingleGuest with Parameters: {} | Success",
        // body);

        // return ResponseEntity.ok(HttpStatus.OK);

        // } catch (Exception e) {
        // logger.error(e.getLocalizedMessage());
        // return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        // }

        // }

        // @Valid
        // @PostMapping("/delete-guest/{testId}/{guestId}/{roles}")
        // public String deleteGuest(@PathVariable Long testId, @PathVariable Long
        // guestId,
        // @PathVariable String roles) {

        // try {
        // Long userID = getUid();

        // logger.info("user_id: {}, role: {}", userID, roles);
        // String role = "";
        // if (roles.equals("SUPER_ADMIN") || roles.equals("ADMIN")) {
        // role = "admin";
        // } else if (roles.equals("TEACHER")) {
        // role = "teacher";
        // }

        // logger.info("Called API name: deleteGuest with parameters: test_id={},
        // guest_id={}, role={}", testId,
        // guestId,
        // roles);
        // logger.info("Redirect /{}/exam/{}/guest/examinee with parameter(test_id={},
        // guest_id={}, role={})", role,
        // testId,
        // testId, guestId, role);

        // logger.info("Initiate Operation Delete Table: test_examinee by Query:
        // test_id={}, guest_id={}",
        // testId, guestId);
        // TestExaminee viewTestGuest =
        // testExamineeRepository.findByTestIdAndGuestId(testId, guestId);
        // testExamineeRepository.delete(viewTestGuest);
        // logger.info("Operation Delete Table: test_examinee by Query: test_id={},
        // guest_id={} | Success",
        // testId, guestId);

        // logger.info("Initiate Operation Update Table: guest by Query: guest_id={}",
        // guestId);
        // GuestUser guestUser = guestUserRepository.findByGuestId(guestId);
        // String deletedDateTime = getDateAndTime();
        // guestUser.setDeleted_date_time(deletedDateTime);
        // guestUserRepository.save(guestUser);
        // logger.info("Operation Update Table: guest by Query: guest_id={},
        // deleted_date_time={} | Success", guestId,
        // deletedDateTime);

        // Test test = testRepository.getTestByID(testId);

        // new Thread(new Runnable() {
        // public void run() {
        // try {
        // mailService.SendGuestRemovedNotification(guestUser, test);
        // logger.info(
        // "Notification of Removal of guest user from exam test_id={} sent to mail={} |
        // Success",
        // testId, guestUser.getMail());
        // } catch (Exception e) {
        // logger.error(e.getLocalizedMessage());
        // }
        // }
        // }).start();

        // logger.info(
        // "Redirect /{}/exam/{}/guest/examinee with parameter(test_id={}, guest_id={},
        // role={}) | Success",
        // role,
        // testId,
        // testId, guestId, role);

        // // return "redirect:/exam/" + redirectId + "/examinee-list/";
        // logger.info("Called API name: deleteGuest with parameters: test_id={},
        // guest_id={}, role={} | Success",
        // testId,
        // guestId, role);

        // return "redirect:/" + role + "/exam/" + testId + "/guest/examinee";

        // } catch (Exception e) {
        // logger.error(e.getLocalizedMessage());
        // return "500";
        // }
        // }

        // private String createOneTimePassword() {

        // final int LENGTH = 8;
        // SecureRandom random = new SecureRandom();
        // String lowercase = "abcdefghijklmnopqrstuvwxyz";
        // String uppercase = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        // String digits = "0123456789";
        // String allChars = lowercase + uppercase + digits;

        // StringBuilder password = new StringBuilder();

        // for (int i = 0; i < LENGTH; i++) {
        // int randomIndex = random.nextInt(allChars.length());
        // char randomChar = allChars.charAt(randomIndex);
        // password.append(randomChar);
        // }

        // return password.toString();
        // }

        // private String getDateAndTime() {
        // Date currentDate = new Date();
        // SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // String formattedDate = dateFormat.format(currentDate);
        // return formattedDate;
        // }

        // private Long getUid() {
        // Long uid = userSessionService.getUserAccount().getAccountId();
        // return uid;
        // }

        @Valid
        @GetMapping(value = { "/teacher/is-email-registered", "/admin/is-email-registered" })
        private ResponseEntity isEmailRegistered(@RequestParam(value = "email") String email)
                        throws ParseException {
                logger.info("API name : {}.Parameter : {}", "isEmailRegistered", email);
                // logger.info("Initiate to Operation Insert Table {} Data {}", "TestExaminee",
                // name);
                UserAccount registeredEmail = userAccountRepository.findByMail(email);
                boolean Registered;

                if (registeredEmail == null) {
                        Registered = false;
                } else {
                        Registered = true;
                }

                return ResponseEntity.ok(Registered);
        }

        // @Valid
        // @GetMapping(value = { "/teacher/exam/{test_id}/examinee",
        // "/admin/exam/{test_id}/examinee" })
        // private String getTestExaminee(@PathVariable Long test_id, Model model,
        // @RequestParam(required = false) String name)
        // throws ParseException {
        // try {

        // logger.info("API name : {}.Parameter : {}", "getTestExaminee", test_id);

        // Test test = testRepository.getTestByID(test_id);
        // String examStatus = test.getExam_status();

        // logger.info("Operation Retrieve Table {} by query : findByNameandTestId {}
        // {}", "TestExaminee", name,
        // test_id);
        // logger.info("Initiate to Operation Retrieve Table {} by query :
        // findByNameandTestId {}", "TestExaminee",
        // name, test_id);
        // List<TestExaminee> testStudents = new ArrayList<>();
        // List<TestExamineeWithMarkedCountModel> testStudentList = new ArrayList<>();
        // int checked_students = 0;
        // int total_free_questions = 0;
        // if (name == null) {

        // testStudents = testExamineeRepository.getExamineeByTest(test_id);
        // } else {
        // logger.info("Initiate to Operation Retrieve Table {} by query
        // :findByNameandTestId{}{}",
        // "TestExaminee",
        // name, test_id);
        // testStudents = testExamineeRepository.findByNameandTestId(name, test_id);
        // logger.info("Operation Retrieve Table {} by query
        // :findByNameandTestId{}{}Result List : {} Success",
        // "TestExaminee", name, test_id, testStudents.toString());
        // }
        // total_free_questions = testQuestionRepository.getFreeAnswerCount(test_id);
        // for (TestExaminee TestExaminee : testStudents) {
        // Integer answerCount =
        // testExamineeAnswerRepository.getCountStudentAnswerListByTestAndStudent(
        // test_id,
        // TestExaminee.getUserInfo().getUid());
        // if (answerCount == 0) {
        // TestExamineeWithMarkedCountModel testStudentWithMarkedCountModel = new
        // TestExamineeWithMarkedCountModel(
        // TestExaminee.getId(), TestExaminee.getTest(), TestExaminee.getUserInfo(),
        // total_free_questions,
        // 0);
        // testStudentList.add(testStudentWithMarkedCountModel);
        // } else {
        // int uncheck_free_questions =
        // testExamineeAnswerRepository.getUnCheckAnswerCountByTestAndStudent(
        // test_id,
        // TestExaminee.getUserInfo().getUid());
        // TestExamineeWithMarkedCountModel testStudentWithMarkedCountModel = new
        // TestExamineeWithMarkedCountModel(
        // TestExaminee.getId(), TestExaminee.getTest(), TestExaminee.getUserInfo(),
        // total_free_questions,
        // total_free_questions - uncheck_free_questions);
        // if (uncheck_free_questions == 0) {
        // checked_students++;
        // }
        // testStudentList.add(testStudentWithMarkedCountModel);
        // }
        // }
        // logger.info("API name : {}. Parameter : {} Return to
        // \"AT0005_TestExamineeList.html\" | Success",
        // "getTestExaminee", test_id);
        // model.addAttribute("user_role", userSessionService.getRole());
        // model.addAttribute("test_id", test_id);
        // model.addAttribute("exam_status", examStatus);
        // model.addAttribute("test_examinees", testStudentList);
        // model.addAttribute("total_students", testStudents.size());
        // model.addAttribute("check_students", checked_students);

        // return "AT0005_TestExamineeList.html";

        // } catch (Exception e) {
        // logger.error(e.getLocalizedMessage());
        // return "500";
        // }
        // }

        // @Valid
        // @GetMapping(value = { "/teacher/get-student", "/admin/get-student" })
        // private ResponseEntity getCustomStudent(@RequestParam(value = "name") String
        // name)
        // throws ParseException {
        // logger.info("API name : {}.Parameter : {}", "getCustomStudent", name);
        // logger.info("Initiate to Operation Insert Table {} Data {}", "TestExaminee",
        // name);
        // String lowerName = name.toLowerCase();
        // List<UserInfo> testStudents = userInfoRepository.findByName(name, lowerName);
        // return ResponseEntity.ok(testStudents);
        // }

        // @Valid
        // @GetMapping(value = { "/teacher/get-student-exam", "/admin/get-student-exam"
        // })
        // private ResponseEntity getCustomStudentExam(@RequestParam(value = "name")
        // String name,
        // @RequestParam(value = "test_id") Long test_id)
        // throws ParseException {
        // logger.info("API name : {}.Parameter : {}", "getCustomStudentExam", name);
        // logger.info("Operation Retrieve Table {} by query : findByNameandTestId {}
        // {}", "TestExaminee", name, test_id);

        // List<UserInfo> testStudents = userInfoRepository.findByNameandTestId(name,
        // test_id);
        // return ResponseEntity.ok(testStudents);
        // }

        // @Valid
        // @PostMapping(value = { "/teacher/set-enrolled-examinee",
        // "/admin/set-enrolled-examinee" })
        // private String setEnrolledStudents(@RequestBody String testid)
        // throws ParseException {
        // logger.info("API name : {}.Parameter : {}", "setEnrolledStudents", testid);
        // logger.info("Operation Retrieve Table {} by query : findByNameandTestId {}
        // {}", "TestExaminee", testid);
        // logger.info("Initiate to Operation Retrieve Table {} by query :
        // findByNameandTestId {}", "TestExaminee",
        // testid);
        // JSONObject jsonObject = new JSONObject(testid);
        // Long test_id = jsonObject.getLong("test_id");
        // Test test = testRepository.getTestByID(test_id);
        // if (test.getExam_status().equals("Exam Created")) {
        // CourseInfo course = test.getCourseInfo();
        // List<JoinCourseUser> enrolledList =
        // joinCourseUserRepository.findByStudentByCourseID(course.getCourseId());
        // for (JoinCourseUser student : enrolledList) {
        // TestExaminee checkStudent =
        // testExamineeRepository.getStudentByID(student.getUserInfo().getUid(),
        // test_id);
        // if (checkStudent == null) {
        // TestExaminee TestExaminee = new TestExaminee(null, test,
        // student.getUserInfo(), null, null);
        // testExamineeRepository.save(TestExaminee);
        // }
        // }
        // }
        // logger.info("API name : {}. Parameter : {}", "setCustomStudents", testid);
        // logger.info("Operation Insert Table {} Data {} Success", "TestExaminee",
        // testid);
        // logger.info("Operation Save File {} Success", testid);
        // return "AT0005_TestExamineeList.html";
        // }

        // @Valid
        // @PostMapping(value = { "/teacher/set-examinee", "/admin/set-examinee" })
        // private String setCustomStudents(@RequestBody String testid)
        // throws ParseException {
        // logger.info("API name : {}.Parameter : {}", "setCustomStudents", testid);
        // logger.info("Operation Retrieve Table {} by query : findByNameandTestId {}
        // {}", "TestExaminee", testid);
        // logger.info("Initiate to Operation Retrieve Table {} by query :
        // findByNameandTestId {}", "TestExaminee",
        // testid);
        // logger.info("Initiate to Operation Insert Table {} Data {}", "TestExaminee",
        // testid);
        // logger.info("Initiate to Operation Save File {}", testid);
        // JSONObject jsonObject = new JSONObject(testid);
        // Long test_id = jsonObject.getLong("test_id");
        // Long student_id = jsonObject.getLong("student_id");
        // Test test = testRepository.getTestByID(test_id);
        // UserInfo user = userInfoRepository.findStudentById(student_id);
        // if (test.getExam_status().equals("Exam Created")) {
        // TestExaminee existingStudent =
        // testExamineeRepository.getStudentByID(student_id, test_id);
        // if (existingStudent == null) {
        // logger.info("Initiate to Operation Retrieve Table {} by query
        // :findByNameandTestId{}{}", "TestExaminee",
        // student_id, test_id);
        // logger.info("Initiate to Operation Update Table {} Data {} By {} = {}",
        // "TestExaminee", testid,
        // "student_id", student_id);
        // TestExaminee TestExaminee = new TestExaminee(null, test, user, null, null);
        // testExamineeRepository.save(TestExaminee);
        // logger.info("Operation Retrieve Table {} by query
        // :findByNameandTestId{}{}Result List : {} Success",
        // "TestExaminee", student_id, test_id, TestExaminee.toString());
        // logger.info("Operation Update Table {} Data {} By {} = {} Success",
        // "TestExaminee", testid,
        // "student_id", student_id);
        // }
        // }
        // logger.info("API name : {}. Parameter : {}", "setCustomStudents", testid);
        // logger.info("Operation Insert Table {} Data {} Success", "TestExaminee",
        // testid);
        // logger.info("Operation Save File {} Success", testid);
        // return "AT0005_TestExamineeList.html";
        // }

        // @GetMapping(value = { "admin/exam/{test_id}/guest/examinee" })
        // private String getTestGuestExaminee(@PathVariable Long test_id, Model model,
        // @RequestParam(required = false) String name) throws ParseException {

        // try {
        // logger.info("Called API name: getTestGuestExaminee by Parameters:
        // test_id={}", test_id);

        // Test test = testRepository.getTestByID(test_id);
        // String examStatus = test.getExam_status();
        // List<TestExaminee> testGuests = new ArrayList<>();
        // List<TestExamineeWithMarkedCountModel> testGuestList = new ArrayList<>();

        // int checked_guests = 0;
        // int total_free_questions = 0;
        // if (name == null) {
        // logger.info("Initiate Operation Retrieve Table: test_examinee by Query:
        // getExamineeByTest({})",
        // test_id);
        // testGuests = testExamineeRepository.getExamineeByTest(test_id);
        // logger.info(
        // "Operation Retrieve Table: test_examinee by Query: getExamineeByTest({})
        // Result: numberOfTestGuests={} | Success",
        // test_id, testGuests.size());
        // } else {
        // logger.info(
        // "Initiate Operation Retrieve Table: test_examinee by Query:
        // findByGuestNameandTestId({}, {})",
        // name, test_id);
        // testGuests = testExamineeRepository.findByGuestNameandTestId(name, test_id);
        // logger.info(
        // "Operation Retrieve Table: test_examinee by Query:
        // findByGuestNameandTestId({}, {}) Result: {} | Success",
        // name, test_id, testGuests);
        // }
        // logger.info("Initiate Operation Retrieve Table: test_question by Query:
        // getFreeAnswerCount({})",
        // test_id);
        // total_free_questions = testQuestionRepository.getFreeAnswerCount(test_id);
        // logger.info(
        // "Operation Retrieve Table: test_question by Query: getFreeAnswerCount({})
        // Result: total_free_questions={} | Success",
        // test_id, total_free_questions);

        // for (TestExaminee testExaminee : testGuests) {

        // Integer answerCount =
        // testExamineeAnswerRepository.getCountStudentAnswerListByTestAndGuest(test_id,
        // testExaminee.getGuestUser().getGuest_id());
        // if (answerCount == 0) {
        // TestExamineeWithMarkedCountModel testGuestWithMarkedCountModel = new
        // TestExamineeWithMarkedCountModel(
        // testExaminee.getId(), testExaminee.getTest(), testExaminee.getGuestUser(),
        // total_free_questions,
        // 0);
        // testGuestList.add(testGuestWithMarkedCountModel);
        // } else {
        // // Left
        // int uncheck_free_questions =
        // testExamineeAnswerRepository.getUnCheckAnswerCountByTestAndGuest(
        // test_id, testExaminee.getGuestUser().getGuest_id());

        // TestExamineeWithMarkedCountModel testGuestWithMarkedCoundtModel = new
        // TestExamineeWithMarkedCountModel(
        // testExaminee.getId(), testExaminee.getTest(), testExaminee.getGuestUser(),
        // total_free_questions,
        // total_free_questions - uncheck_free_questions);
        // if (uncheck_free_questions == 0) {
        // checked_guests++;
        // }
        // testGuestList.add(testGuestWithMarkedCoundtModel);
        // }

        // }
        // logger.info(
        // "Called API name : getTestGuestExaminee by Parameter : {} Return to
        // \"AT0005_TestGuestList.html\" | Success",
        // "getTestExaminee", test_id);
        // model.addAttribute("user_role", userSessionService.getRole());
        // model.addAttribute("test_id", test_id);
        // model.addAttribute("exam_status", examStatus);
        // model.addAttribute("test_guests", testGuestList);
        // model.addAttribute("total_guests", testGuests.size());
        // model.addAttribute("check_guests", checked_guests);

        // return "AT0005_TestGuestList.html";

        // } catch (Exception e) {
        // logger.error(e.getLocalizedMessage());
        // return "500";
        // }

        // }

        // @Valid
        // @PostMapping(value = { "/teacher/set-single-guest", "/admin/set-single-guest"
        // })
        // private ResponseEntity setSingleGuest(@RequestBody String testid) throws
        // ParseException {

        // try {
        // logger.info("Called API name: setSingleGuest with Parameters: {}", testid);

        // JSONObject jsonObject = new JSONObject(testid);
        // Long test_id = jsonObject.getLong("test_id");
        // String name = jsonObject.getString("guest_name");
        // String email = jsonObject.getString("guest_email").toLowerCase();
        // String phone_number = jsonObject.getString("guest_ph_no");

        // Test test = testRepository.getTestByID(test_id);

        // if (test.getExam_status().equals("Exam Created") ||
        // test.getExam_status().equals("Questions Created")) {

        // // logger.info(
        // // "Operation Insert Table: guest | Data name:{}, mail:{}, phone_no:{},
        // // one_time_password:{}, password_update_date_time:{}, updated_date_time:{},
        // // deleted_date_time:{}",
        // // name);

        // // Check if the email exists in the user_account table
        // List<String> userAccountEmails = userAccountRepository.getAllUserEmail();
        // if (userAccountEmails.contains(email)) {
        // logger.warn("Opearation Insert Table: guest Data: name={}, mail={},
        // phone_no={} | Failed", name,
        // email, phone_number);
        // logger.warn("Email: {} already existes in Table: user_account | Operation
        // Cancelled", email);
        // return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        // .body("{\"errorMessage\": \"Added Email is already associated with a Student
        // account\"}");
        // }

        // // Check if the email exists in the guest and test table
        // GuestUser existingGuest =
        // guestUserRepository.findGuestUserByGuestEmailAndTestId(email, test_id);
        // if (existingGuest != null) {
        // logger.warn("Opearation Insert Table: guest Data: name={}, mail={},
        // phone_no={} | Failed", name,
        // email, phone_number);
        // logger.warn("Email: {} already existes in Table: guest for test_id={} |
        // Operation Cancelled", email,
        // test_id);
        // return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        // .body("{\"errorMessage\": \"Added Email already associated with a Guest user
        // within the Exam\"}");
        // }

        // // Check if the guest user email is already in the database
        // GuestUser guestUser = guestUserRepository.getGuestUserbyEmail(email);
        // if (guestUser == null) {
        // logger.info("Initiate Operation Insert Table: guest Data: name={}, mail={},
        // phone_no={}", name,
        // email,
        // phone_number);
        // String one_time_password = createOneTimePassword();
        // String one_time_passwordEncoded = passwordEncoder.encode(one_time_password);
        // String password_update_date_time = getDateAndTime();

        // GuestUser newGuestUser = new GuestUser(null, name, email, phone_number,
        // one_time_passwordEncoded,
        // password_update_date_time,
        // null, null);
        // guestUserRepository.save(newGuestUser);
        // logger.info(" Operation Insert Table: guest Data: name={}, mail={},
        // phone_no={} | Success", name,
        // email,
        // phone_number);

        // TestExaminee testExaminee = new TestExaminee(null, test, null, newGuestUser,
        // null);
        // logger.info("Initiate Operation Insert Table: test_examinee Data: test_id={},
        // guest_user={}",
        // test.getTest_id(),
        // newGuestUser.display());
        // testExamineeRepository.save(testExaminee);
        // logger.info("Operation Insert Table: test_examinee Data: test_id={},
        // guest_user={} | Success",
        // test.getTest_id(), newGuestUser.display());

        // new Thread(new Runnable() {
        // public void run() {
        // try {
        // mailService.SendGuestOneTimePassword(newGuestUser, one_time_password);
        // logger.info("One-time-password (OTP) sent to mail={} | Success", email);
        // } catch (Exception e) {
        // logger.error(e.getLocalizedMessage());
        // }
        // }
        // }).start();

        // } else {
        // guestUser.setName(name);
        // guestUser.setPhone_no(phone_number);
        // guestUser.setUpdated_date_time(getDateAndTime());
        // logger.info("Guest User with email={} is updated with name={}, phone_no={}",
        // email, name,
        // phone_number);
        // guestUserRepository.save(guestUser);

        // TestExaminee testExaminee = new TestExaminee(null, test, null, guestUser,
        // null);
        // logger.info("Initiate Operation Insert Table: test_examinee Data: test_id={},
        // guest_user={}",
        // test.getTest_id(), guestUser.display());
        // testExamineeRepository.save(testExaminee);
        // logger.info("Operation Insert Table: test_examinee Data: test_id={},
        // guest_user={} | Success",
        // test.getTest_id(), guestUser.display());
        // }

        // logger.info("Called API name: setSingleGuest with Parameters: {} | Success",
        // testid);

        // return ResponseEntity.ok(HttpStatus.OK);

        // }
        // return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

        // } catch (Exception e) {
        // logger.error(e.getLocalizedMessage());
        // return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        // // return "500";
        // }
        // }

        // @Valid
        // @PostMapping(value = { "/teacher/edit-single-guest",
        // "/admin/edit-single-guest" })
        // private ResponseEntity editSingleGuest(@RequestBody String body)
        // throws ParseException {

        // try {

        // logger.info("Called API name: editSingleGuest with Parameters: {}", body);

        // JSONObject jsonObject = new JSONObject(body);
        // Long test_id = jsonObject.getLong("test_id");
        // Long guest_id = jsonObject.getLong("guest_id");
        // String name = jsonObject.getString("guest_name");
        // String email = jsonObject.getString("guest_email").toLowerCase();
        // String phone_number = jsonObject.getString("guest_ph_no");

        // logger.info("Initiate Opearation Update Table: guest For guest_id={} Data:
        // name={}, mail={}, phone_no={}",
        // guest_id, name, email, phone_number);

        // // Check if the email exists in the user_account table
        // List<String> userAccountEmails = userAccountRepository.getAllUserEmail();
        // if (userAccountEmails.contains(email)) {
        // logger.warn("Opearation Update Table: guest Data: guest_id={}, name={},
        // mail={}, phone_no={} | Failed",
        // guest_id, name, email, phone_number);
        // logger.warn("Email: {} already existes in Table: user_account", email);
        // return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        // .body("{\"errorMessage\": \"Added Email is already associated with a Student
        // account\"}");
        // }

        // GuestUser existingGuest = guestUserRepository.findByGuestId(guest_id);
        // String newEncodedOneTimePassword;
        // String passwordUpdatedDateTime;
        // String updatedDateTime = getDateAndTime();

        // // Compare the newly added email with the existing guest email
        // if (existingGuest.getMail().equals(email)) {
        // GuestUser newGuestUser = new GuestUser(guest_id, name,
        // existingGuest.getMail(), phone_number,
        // existingGuest.getOne_time_password(),
        // existingGuest.getPassword_update_date_time(), updatedDateTime, null);
        // guestUserRepository.save(newGuestUser);
        // } else {

        // // Create a new password if not email doesn't match with the existing email
        // logger.info("New one-time-password is created for guest_id={}", guest_id);
        // String newOneTimePassword = createOneTimePassword();
        // newEncodedOneTimePassword = passwordEncoder.encode(newOneTimePassword);
        // passwordUpdatedDateTime = getDateAndTime();

        // GuestUser updatedGuestUser = new GuestUser(guest_id, name, email,
        // phone_number,
        // newEncodedOneTimePassword,
        // passwordUpdatedDateTime, updatedDateTime, null);
        // Test test = testRepository.getTestByID(test_id);

        // guestUserRepository.save(updatedGuestUser);

        // new Thread(new Runnable() {
        // public void run() {
        // try {
        // mailService.SendGuestRemovedNotification(existingGuest, test);
        // logger.info(
        // "Notified Email of Removal of guest user guest_id={} from exam test_id={}
        // sent to mail={} | Success",
        // guest_id, test_id, existingGuest.getMail());
        // } catch (Exception e) {
        // logger.info(e.getLocalizedMessage());
        // }
        // }
        // }).start();

        // new Thread(new Runnable() {
        // public void run() {
        // try {
        // mailService.SendGuestOneTimePassword(updatedGuestUser, newOneTimePassword);
        // logger.info("One-time-password (OTP) sent to mail={} | Success", email);
        // } catch (Exception e) {
        // logger.info(e.getLocalizedMessage());
        // }
        // }
        // }).start();
        // }
        // logger.info("Opearation Update Table: guest Data: guest_id={}, name={},
        // mail={}, phone_no={} | Success",
        // guest_id, name, email, phone_number);
        // logger.info("Called API name: editSingleGuest with Parameters: {} | Success",
        // body);

        // return ResponseEntity.ok(HttpStatus.OK);

        // } catch (Exception e) {
        // logger.error(e.getLocalizedMessage());
        // return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        // }

        // }

        // @Valid
        // @PostMapping("/delete-guest/{testId}/{guestId}/{roles}")
        // public String deleteGuest(@PathVariable Long testId, @PathVariable Long
        // guestId,
        // @PathVariable String roles) {

        // try {
        // Long userID = getUid();

        // logger.info("user_id: {}, role: {}", userID, roles);
        // String role = "";
        // if (roles.equals("SUPER_ADMIN") || roles.equals("ADMIN")) {
        // role = "admin";
        // } else if (roles.equals("TEACHER")) {
        // role = "teacher";
        // }

        // logger.info("Called API name: deleteGuest with parameters: test_id={},
        // guest_id={}, role={}", testId,
        // guestId,
        // roles);
        // logger.info("Redirect /{}/exam/{}/guest/examinee with parameter(test_id={},
        // guest_id={}, role={})", role,
        // testId,
        // testId, guestId, role);

        // logger.info("Initiate Operation Delete Table: test_examinee by Query:
        // test_id={}, guest_id={}",
        // testId, guestId);
        // TestExaminee viewTestGuest =
        // testExamineeRepository.findByTestIdAndGuestId(testId, guestId);
        // testExamineeRepository.delete(viewTestGuest);
        // logger.info("Operation Delete Table: test_examinee by Query: test_id={},
        // guest_id={} | Success",
        // testId, guestId);

        // logger.info("Initiate Operation Update Table: guest by Query: guest_id={}",
        // guestId);
        // GuestUser guestUser = guestUserRepository.findByGuestId(guestId);
        // String deletedDateTime = getDateAndTime();
        // guestUser.setDeleted_date_time(deletedDateTime);
        // guestUserRepository.save(guestUser);
        // logger.info("Operation Update Table: guest by Query: guest_id={},
        // deleted_date_time={} | Success", guestId,
        // deletedDateTime);

        // Test test = testRepository.getTestByID(testId);

        // new Thread(new Runnable() {
        // public void run() {
        // try {
        // mailService.SendGuestRemovedNotification(guestUser, test);
        // logger.info(
        // "Notification of Removal of guest user from exam test_id={} sent to mail={} |
        // Success",
        // testId, guestUser.getMail());
        // } catch (Exception e) {
        // logger.error(e.getLocalizedMessage());
        // }
        // }
        // }).start();

        // logger.info(
        // "Redirect /{}/exam/{}/guest/examinee with parameter(test_id={}, guest_id={},
        // role={}) | Success",
        // role,
        // testId,
        // testId, guestId, role);

        // // return "redirect:/exam/" + redirectId + "/examinee-list/";
        // logger.info("Called API name: deleteGuest with parameters: test_id={},
        // guest_id={}, role={} | Success",
        // testId,
        // guestId, role);

        // return "redirect:/" + role + "/exam/" + testId + "/guest/examinee";

        // } catch (Exception e) {
        // logger.error(e.getLocalizedMessage());
        // return "500";
        // }
        // }

        // private String createOneTimePassword() {

        // final int LENGTH = 8;
        // SecureRandom random = new SecureRandom();
        // String lowercase = "abcdefghijklmnopqrstuvwxyz";
        // String uppercase = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        // String digits = "0123456789";
        // String allChars = lowercase + uppercase + digits;

        // StringBuilder password = new StringBuilder();

        // for (int i = 0; i < LENGTH; i++) {
        // int randomIndex = random.nextInt(allChars.length());
        // char randomChar = allChars.charAt(randomIndex);
        // password.append(randomChar);
        // }

        // return password.toString();
        // }

        // private String getDateAndTime() {
        // Date currentDate = new Date();
        // SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // String formattedDate = dateFormat.format(currentDate);
        // return formattedDate;
        // }

        // private Long getUid() {
        // Long uid = userSessionService.getUserAccount().getAccountId();
        // return uid;
        // }

        @Valid
        @GetMapping(value = { "/teacher/exam/{test_id}/examinee", "/admin/exam/{test_id}/examinee" })
        private String getTestExaminee(@PathVariable Long test_id, Model model,

                        @RequestParam(required = false) String name)
                        throws ParseException {
                logger.info("API name : {}.Parameter : {}", "getTestExaminee", test_id);
                logger.info("Operation Retrieve Table {} by query : findByNameandTestId {} {}", "TestExaminee", name,
                                test_id);
                // logger.info("Initiate to Operation Retrieve Table {} by query :
                // findByNameandTestId {}", "TestExaminee",
                // name,
                // test_id);
                List<TestExaminee> testStudents = new ArrayList<>();
                List<TestExamineeWithMarkedCountModel> testStudentList = new ArrayList<>();
                int checked_students = 0;
                int total_free_questions = 0;
                if (name == null) {
                        logger.info("Initiate to Operation Retrieve Table {} by query : findByNameandTestId {},{}",
                                        "TestExaminee",
                                        name,
                                        test_id);
                        testStudents = testExamineeRepository.getExamineeByTest(test_id);
                        logger.info("Operation Retrieve Table {} by query : findByNameandTestId {},{} | Success",
                                        "TestExaminee",
                                        name,
                                        test_id);
                } else {
                        logger.info("Initiate to Operation Retrieve Table {} by query :findByNameandTestId{}{}",
                                        "TestExaminee",
                                        name, test_id);
                        testStudents = testExamineeRepository.findByNameandTestId(name, test_id);
                        logger.info("Operation Retrieve Table {} by query :findByNameandTestId{}{}Result List : {} Success",
                                        "TestExaminee", name, test_id, testStudents.toString());
                }

                logger.info("Initiate to Operation Retrieve Table {} by query : getFreeAnswerCount {}",
                                "test_question",
                                test_id);
                total_free_questions = testQuestionRepository.getFreeAnswerCount(test_id);
                logger.info("Operation Retrieve Table {} by query : getFreeAnswerCount {} | Success",
                                "test_question",
                                test_id);

                logger.info("Initiate to Operation Retrieve Table {} by query {}",
                                "test",
                                "getTestByID");
                Test test = testRepository.getTestByID(test_id);
                logger.info("Operation Retrieve Table {} by query {} Result List {} Success",
                                "test",
                                "getTestByID",
                                test);
                String examStatus = test.getExam_status();
                Integer examTarget = test.getExam_target();
                System.out.println("BACKEND :" + examTarget + "/");

                // Student Exam
                if (examTarget == 0) {
                        System.out.println("STUDENT EXAM");
                        // for (TestExaminee TestExaminee : testStudents) {
                        // logger.info("Initiate to Operation Retrieve Table {} by query : {} {},{}",
                        // "test_examinee_answer",
                        // "getCountStudentAnswerListByTestAndStudent",
                        // test_id,
                        // TestExaminee.getUserInfo().getUid());
                        // Integer answerCount = testStudentAnswerRepository
                        // .getCountStudentAnswerListByTestAndStudent(
                        // test_id,
                        // TestExaminee.getUserInfo().getUid());
                        // logger.info("Operation Retrieve Table {} by query : {} {},{} | Success",
                        // "test_examinee_answer",
                        // "getCountStudentAnswerListByTestAndStudent",
                        // test_id,
                        // TestExaminee.getUserInfo().getUid());

                        // if (answerCount == 0) {
                        // TestExamineeWithMarkedCountModel testStudentWithMarkedCountModel = new
                        // TestExamineeWithMarkedCountModel(
                        // TestExaminee.getId(), TestExaminee.getTest(),
                        // TestExaminee.getUserInfo(), total_free_questions,
                        // 0);
                        // testStudentList.add(testStudentWithMarkedCountModel);
                        // } else {
                        // logger.info("Initiate to Operation Retrieve Table {} by query : {} {},{}",
                        // "test_examinee_answer",
                        // "getUnCheckAnswerCountByTestAndStudent",
                        // test_id,
                        // TestExaminee.getUserInfo().getUid());
                        // int uncheck_free_questions = testStudentAnswerRepository
                        // .getUnCheckAnswerCountByTestAndStudent(
                        // test_id,
                        // TestExaminee.getUserInfo().getUid());
                        // logger.info("Operation Retrieve Table {} by query : {} {},{} | Success",
                        // "test_examinee_answer",
                        // "getUnCheckAnswerCountByTestAndStudent",
                        // test_id,
                        // TestExaminee.getUserInfo().getUid());

                        // TestExamineeWithMarkedCountModel testStudentWithMarkedCountModel = new
                        // TestExamineeWithMarkedCountModel(
                        // TestExaminee.getId(), TestExaminee.getTest(),
                        // TestExaminee.getUserInfo(), total_free_questions,
                        // total_free_questions - uncheck_free_questions);
                        // if (uncheck_free_questions == 0) {
                        // checked_students++;
                        // }
                        // testStudentList.add(testStudentWithMarkedCountModel);
                        // }
                        // }
                        test = testRepository.getTestByID(test_id);
                        examStatus = test.getExam_status();

                        testStudents = new ArrayList<>();
                        testStudentList = new ArrayList<>();
                        checked_students = 0;
                        total_free_questions = 0;
                        if (name == null) {

                                testStudents = testExamineeRepository.getExamineeByTest(test_id);
                        } else {

                                testStudents = testExamineeRepository.findByNameandTestId(name, test_id);

                        }
                        total_free_questions = testQuestionRepository.getFreeAnswerCount(test_id);
                        for (TestExaminee TestExaminee : testStudents) {
                                Integer answerCount = testExamineeAnswerRepository
                                                .getCountStudentAnswerListByTestAndStudent(
                                                                test_id,
                                                                TestExaminee.getUserInfo().getUid());

                                try {
                                        
                                
                                if (answerCount == 0) {
                                        FileInfo profilePic = storageService.loadProfileAsFileInfo(TestExaminee.getUserInfo());
                                        TestExamineeWithMarkedCountModel testStudentWithMarkedCountModel = new TestExamineeWithMarkedCountModel(
                                                        TestExaminee.getId(), TestExaminee.getTest(),
                                                        TestExaminee.getUserInfo(),
                                                        total_free_questions,
                                                        0, profilePic);
                                        testStudentList.add(testStudentWithMarkedCountModel);
                                } else {
                                        FileInfo profilePic = storageService.loadProfileAsFileInfo(TestExaminee.getUserInfo());
                                        int uncheck_free_questions = testExamineeAnswerRepository
                                                        .getUnCheckAnswerCountByTestAndStudent(
                                                                        test_id,
                                                                        TestExaminee.getUserInfo().getUid());
                                        TestExamineeWithMarkedCountModel testStudentWithMarkedCountModel = new TestExamineeWithMarkedCountModel(
                                                        TestExaminee.getId(), TestExaminee.getTest(),
                                                        TestExaminee.getUserInfo(),
                                                        total_free_questions,
                                                        total_free_questions - uncheck_free_questions, profilePic);
                                        if (uncheck_free_questions == 0) {
                                                checked_students++;
                                        }
                                        testStudentList.add(testStudentWithMarkedCountModel);
                                }
                                } catch (Exception e) {
                                        e.printStackTrace();
                                        logger.info("unable to get profile {}", TestExaminee.getUserInfo());
                                }
                        }

                        model.addAttribute("user_role", userSessionService.getRole());
                        model.addAttribute("test_id", test_id);
                        model.addAttribute("exam_status", examStatus);
                        model.addAttribute("test_examinees", testStudentList);
                        model.addAttribute("total_students", testStudents.size());
                        model.addAttribute("check_students", checked_students);

                        return "AT0005_TestExamineeList.html";
                }

                // Guest Exam
                else if (examTarget == 1) {
                        for (TestExaminee TestExaminee : testStudents) {
                                logger.info("Initiate to Operation Retrieve Table {} by query : {} {},{}",
                                                "test_examinee_answer",
                                                "getCountStudentAnswerListByTestAndStudent",
                                                test_id,
                                                TestExaminee.getGuestUser().getGuest_id());
                                Integer answerCount = testStudentAnswerRepository
                                                .getCountStudentAnswerListByTestAndStudent(
                                                                test_id,
                                                                TestExaminee.getGuestUser().getGuest_id());
                                logger.info("Operation Retrieve Table {} by query : {} {},{} | Success",
                                                "test_examinee_answer",
                                                "getCountStudentAnswerListByTestAndStudent",
                                                test_id,
                                                TestExaminee.getGuestUser().getGuest_id());

                                if (answerCount == 0) {
                                        TestExamineeWithMarkedCountModel testStudentWithMarkedCountModel = new TestExamineeWithMarkedCountModel(
                                                        TestExaminee.getId(), TestExaminee.getTest(),
                                                        TestExaminee.getGuestUser(), total_free_questions,
                                                        0);
                                        testStudentList.add(testStudentWithMarkedCountModel);
                                } else {
                                        logger.info("Initiate to Operation Retrieve Table {} by query : {} {},{}",
                                                        "test_examinee_answer",
                                                        "getUnCheckAnswerCountByTestAndStudent",
                                                        test_id,
                                                        TestExaminee.getGuestUser().getGuest_id());
                                        int uncheck_free_questions = testStudentAnswerRepository
                                                        .getUnCheckAnswerCountByTestAndStudent(
                                                                        test_id,
                                                                        TestExaminee.getGuestUser().getGuest_id());
                                        logger.info("Operation Retrieve Table {} by query : {} {},{} | Success",
                                                        "test_examinee_answer",
                                                        "getUnCheckAnswerCountByTestAndStudent",
                                                        test_id,
                                                        TestExaminee.getGuestUser().getGuest_id());

                                        TestExamineeWithMarkedCountModel testStudentWithMarkedCountModel = new TestExamineeWithMarkedCountModel(
                                                        TestExaminee.getId(), TestExaminee.getTest(),
                                                        TestExaminee.getGuestUser(), total_free_questions,
                                                        total_free_questions - uncheck_free_questions);
                                        if (uncheck_free_questions == 0) {
                                                checked_students++;
                                        }
                                        testStudentList.add(testStudentWithMarkedCountModel);
                                }
                        }
                }

                // logger.info("API name : {}. Parameter : {}", "getTestExaminee", test_id);
                logger.info("Initiate to Operation Save File {}", test_id);
                model.addAttribute("user_role", userSessionService.getRole());
                model.addAttribute("test_id", test_id);
                model.addAttribute("exam_target", examTarget);
                model.addAttribute("test_examinees", testStudentList);
                model.addAttribute("total_students", testStudents.size());
                model.addAttribute("check_students", checked_students);
                model.addAttribute("exam_status", examStatus);
                logger.info("Called {} with parameter: {},{} Success",
                                "getTestExaminee",
                                test_id,
                                name);
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
                logger.info("Operation Retrieve Table {} by query : findByNameandTestId {} {}", "TestExaminee", name,
                                test_id);

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
                        List<JoinCourseUser> enrolledList = joinCourseUserRepository
                                        .findByStudentByCourseID(course.getCourseId());
                        for (JoinCourseUser student : enrolledList) {
                                TestExaminee checkStudent = testExamineeRepository.getStudentByID(
                                                student.getUserInfo().getUid(),
                                                test_id);
                                if (checkStudent == null) {
                                        Long testExamineeMaxid = testExamineeRepository.getExamineeTableMaxID();
                                        TestExaminee TestExaminee = new TestExaminee(testExamineeMaxid, test, student.getUserInfo(),
                                                        null, null);
                                        testExamineeRepository.save(TestExaminee);
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
                if (test.getExam_status().equals("Exam Created") || test.getExam_status().equals("Questions Created")) {
                        TestExaminee existingStudent = testExamineeRepository.getStudentByID(student_id, test_id);
                        if (existingStudent == null) {
                                logger.info("Initiate to Operation Retrieve Table {} by query :findByNameandTestId{}{}",
                                                "TestExaminee",
                                                student_id, test_id);
                                logger.info("Initiate to Operation Update Table {} Data {} By {} = {}", "TestExaminee",
                                                testid,
                                                "student_id", student_id);
                                TestExaminee TestExaminee = new TestExaminee(null, test, user, null, null);
                                testExamineeRepository.save(TestExaminee);
                                logger.info("Operation Retrieve Table {} by query :findByNameandTestId{}{}Result List : {} Success",
                                                "TestExaminee", student_id, test_id, TestExaminee.toString());
                                logger.info("Operation Update Table {} Data {} By {} = {} Success", "TestExaminee",
                                                testid,
                                                "student_id", student_id);
                        }
                }
                logger.info("API name : {}. Parameter : {}", "setCustomStudents", testid);
                logger.info("Operation Insert Table {} Data {} Success", "TestExaminee", testid);
                logger.info("Operation Save File {} Success", testid);
                return "AT0005_TestExamineeList.html";
        }

        // Add multiple guests to an exam
        @Valid
        @PostMapping(value = { "/teacher/set-multi-guest-examinee", "/admin/set-multi-guest-examinee" })
        private ResponseEntity setMultiGuest(@RequestBody String data) throws ParseException {
                logger.info("set-multi-guest-examinee with parameter: {}", data);
                JSONObject jsonObject = new JSONObject(data);
                Long test_id = jsonObject.getLong("test_id");
                JSONArray exam_guest_users = jsonObject.getJSONArray("exam_guest_users");

                logger.info("Initiate to Operation Retrieve Table {} by query {}",
                                "test",
                                "testRepository.getTestByID(test_id)");
                Test test = testRepository.getTestByID(test_id);
                logger.info("Operation Retrieve Table {} by query {} Result List {} Success",
                                "test",
                                "testRepository.getTestByID(test_id)",
                                test);

                if (test.getExam_status().equals("Exam Created") || test.getExam_status().equals("Questions Created")) {
                        Gson gson = new Gson();
                        try {
                                String[][] guestUsers = gson.fromJson(exam_guest_users.toString(), String[][].class);
                                for (int i = 0; i < guestUsers.length; i++) {
                                        String name = guestUsers[i][0];
                                        String email = guestUsers[i][1];
                                        String phone_number = guestUsers[i][2];
                                        String one_time_password = createOneTimePassword();
                                        String one_time_passwordEncoded = passwordEncoder.encode(one_time_password);
                                        String password_update_date_time = getDateAndTime();
                                        GuestUser guestUser = guestUserRepository.findByMail(email);
                                        TestExaminee checkExaminee = guestUserRepository
                                                        .findGuestUserByGuestEmailAndTestId(email, test_id);
                                        if (guestUser == null) {
                                                GuestUser newGuestUser = new GuestUser(null, name, email, phone_number,
                                                                one_time_passwordEncoded,
                                                                password_update_date_time,
                                                                null, null);
                                                guestUserRepository.save(newGuestUser);

                                                new Thread(new Runnable() {
                                                        public void run() {
                                                                try {
                                                                        mailService.SendGuestOneTimePassword(
                                                                                        newGuestUser,
                                                                                        one_time_password);
                                                                        logger.info("One-time-password (OTP) sent to mail={} | Success",
                                                                                        email);
                                                                } catch (Exception e) {
                                                                        logger.info(e.getLocalizedMessage());
                                                                }
                                                        }
                                                }).start();

                                                if (checkExaminee == null) {
                                                        TestExaminee testExaminee = new TestExaminee(null, test, null,
                                                                        newGuestUser, null);
                                                        testExamineeRepository.save(testExaminee);

                                                }

                                        } else {
                                                guestUser.setOne_time_password(one_time_passwordEncoded);
                                                guestUserRepository.save(guestUser);

                                                new Thread(new Runnable() {
                                                        public void run() {
                                                                try {
                                                                        mailService.SendGuestOneTimePassword(guestUser,
                                                                                        one_time_password);
                                                                        logger.info("One-time-password (OTP) sent to mail={} | Success",
                                                                                        email);
                                                                } catch (Exception e) {
                                                                        logger.info(e.getLocalizedMessage());
                                                                }
                                                        }
                                                }).start();

                                                if (checkExaminee == null) {
                                                        TestExaminee testExaminee = new TestExaminee(null, test, null,
                                                                        guestUser, null);
                                                        testExamineeRepository.save(testExaminee);

                                                }
                                        }

                                        // new Thread(new Runnable() {
                                        // public void run() {
                                        // try {
                                        // mailService.SendGuestOneTimePassword(guestUser,
                                        // one_time_password);
                                        // logger.info("One-time-password (OTP) sent to mail={} | Success",
                                        // email);
                                        // } catch (Exception e) {
                                        // logger.info(e.getLocalizedMessage());
                                        // }
                                        // }
                                        // }).start();
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

        @GetMapping(value = { "admin/exam/{test_id}/guest/examinee" })
        private String getTestGuestExaminee(@PathVariable Long test_id, Model model,
                        @RequestParam(required = false) String name) throws ParseException {

                try {
                        logger.info("Called API name: getTestGuestExaminee by Parameters: test_id={}", test_id);

                        Test test = testRepository.getTestByID(test_id);
                        String examStatus = test.getExam_status();
                        List<TestExaminee> testGuests = new ArrayList<>();
                        List<TestExamineeWithMarkedCountModel> testGuestList = new ArrayList<>();

                        int checked_guests = 0;
                        int total_free_questions = 0;
                        if (name == null) {
                                logger.info("Initiate Operation Retrieve Table: test_examinee by Query: getExamineeByTest({})",
                                                test_id);
                                testGuests = testExamineeRepository.getExamineeByTest(test_id);
                                logger.info(
                                                "Operation Retrieve Table: test_examinee by Query: getExamineeByTest({}) Result: numberOfTestGuests={} | Success",
                                                test_id, testGuests.size());
                        } else {
                                logger.info(
                                                "Initiate Operation Retrieve Table: test_examinee by Query: findByGuestNameandTestId({}, {})",
                                                name, test_id);
                                testGuests = testExamineeRepository.findByGuestNameandTestId(name, test_id);
                                logger.info(
                                                "Operation Retrieve Table: test_examinee by Query: findByGuestNameandTestId({}, {}) Result: {} | Success",
                                                name, test_id, testGuests);
                        }
                        logger.info("Initiate Operation Retrieve Table: test_question by Query: getFreeAnswerCount({})",
                                        test_id);
                        total_free_questions = testQuestionRepository.getFreeAnswerCount(test_id);
                        logger.info(
                                        "Operation Retrieve Table: test_question by Query: getFreeAnswerCount({}) Result: total_free_questions={} | Success",
                                        test_id, total_free_questions);

                        for (TestExaminee testExaminee : testGuests) {

                                Integer answerCount = testExamineeAnswerRepository
                                                .getCountStudentAnswerListByTestAndGuest(test_id,
                                                                testExaminee.getGuestUser().getGuest_id());
                                if (answerCount == 0) {
                                        TestExamineeWithMarkedCountModel testGuestWithMarkedCountModel = new TestExamineeWithMarkedCountModel(
                                                        testExaminee.getId(), testExaminee.getTest(),
                                                        testExaminee.getGuestUser(),
                                                        total_free_questions,
                                                        0);
                                        testGuestList.add(testGuestWithMarkedCountModel);
                                } else {
                                        // Left
                                        int uncheck_free_questions = testExamineeAnswerRepository
                                                        .getUnCheckAnswerCountByTestAndGuest(
                                                                        test_id,
                                                                        testExaminee.getGuestUser().getGuest_id());

                                        TestExamineeWithMarkedCountModel testGuestWithMarkedCoundtModel = new TestExamineeWithMarkedCountModel(
                                                        testExaminee.getId(), testExaminee.getTest(),
                                                        testExaminee.getGuestUser(),
                                                        total_free_questions,
                                                        total_free_questions - uncheck_free_questions);
                                        if (uncheck_free_questions == 0) {
                                                checked_guests++;
                                        }
                                        testGuestList.add(testGuestWithMarkedCoundtModel);
                                }

                        }
                        logger.info("Called API name : getTestGuestExaminee by Parameter : {} Return to \"AT0005_TestGuestList.html\" | Success",
                                        "getTestExaminee", test_id);
                        model.addAttribute("user_role", userSessionService.getRole());
                        model.addAttribute("test_id", test_id);
                        model.addAttribute("exam_status", examStatus);
                        model.addAttribute("test_guests", testGuestList);
                        model.addAttribute("total_guests", testGuests.size());
                        model.addAttribute("check_guests", checked_guests);
                        if (total_free_questions == 0) {
                                model.addAttribute("total_check_quesetion", 0);
                        } else {
                                model.addAttribute("total_check_quesetion", testGuests.size());
                        }

                        // return "AT0005_TestGuestList.html";
                        return "AT0005_TestExamineeList.html";

                } catch (Exception e) {
                        logger.error(e.getLocalizedMessage());
                        return "500";
                }

        }

        // Add single guest to an exam
        @Valid
        @PostMapping(value = { "/teacher/set-single-guest", "/admin/set-single-guest" })
        private ResponseEntity setSingleGuest(@RequestBody String testid) throws ParseException {

                try {
                        logger.info("Called API name: setSingleGuest with Parameters: {}", testid);

                        JSONObject jsonObject = new JSONObject(testid);
                        Long test_id = jsonObject.getLong("test_id");
                        String name = jsonObject.getString("guest_name");
                        String email = jsonObject.getString("guest_email").toLowerCase();
                        String phone_number = jsonObject.getString("guest_ph_no");
                        String one_time_password = createOneTimePassword();
                        String one_time_passwordEncoded = passwordEncoder.encode(one_time_password);
                        String password_update_date_time = getDateAndTime();

                        logger.info("Initiate to Operation Retrieve Table {} by query {}",
                                        "test",
                                        "getTestByID(test_id)");
                        Test test = testRepository.getTestByID(test_id);
                        logger.info("Operation Retrieve Table {} by query {} Result List {} Success",
                                        "test",
                                        "getTestByID(test_id)",
                                        test);

                        if (test.getExam_status().equals("Exam Created")
                                        || test.getExam_status().equals("Questions Created")) {
                                // GuestUser guestUser = new GuestUser(null, name, email, phone_number,
                                // one_time_passwordEncoded,
                                // password_update_date_time,
                                // null, null);
                                // logger.info(
                                // "Operation Insert Table: guest | Data name:{}, mail:{}, phone_no:{},
                                // one_time_password:{}, password_update_date_time:{}, updated_date_time:{},
                                // deleted_date_time:{}",
                                // name);
                                logger.info("Initiate Operation Insert Table: guest Data: name={}, mail={}, phone_no={}",
                                                name, email,
                                                phone_number);

                                // // Check if the email exists in the user_account table
                                // List<String> userAccountEmails = userAccountRepository.getAllUserEmail();
                                // if (userAccountEmails.contains(email)) {
                                // logger.warn("Opearation Insert Table: guest Data: name={}, mail={},
                                // phone_no={} | Failed",
                                // name,
                                // email, phone_number);
                                // logger.warn("Email: {} already existes in Table: user_account | Operation
                                // Cancelled",
                                // email);
                                // return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                // .body("{\"errorMessage\": \"Added Email is already associated with a Student
                                // account\"}");
                                // }

                                logger.info("Initiate to Operation Retrieve Table {} by query {}",

                                                "guest",
                                                "findGuestUserByGuestEmailAndTestId(email, test_id)");
                                GuestUser guestUser = guestUserRepository.findByMail(email);
                                TestExaminee checkExaminee = guestUserRepository
                                                .findGuestUserByGuestEmailAndTestId(email, test_id);
                                logger.info("Operation Retrieve Table {} by query {} Result List {} Success",
                                                "guest",
                                                "findGuestUserByGuestEmailAndTestId(email, test_id)",
                                                checkExaminee);

                                // Check if guest user is already added to exam or not

                                if (guestUser == null) {
                                        Long maxGuestID = guestUserRepository.getGuestableMaxID();
                                        GuestUser newGuestUser = new GuestUser(maxGuestID, name, email, phone_number,
                                                        one_time_passwordEncoded,
                                                        password_update_date_time,
                                                        null, null);
                                        guestUserRepository.save(newGuestUser);
                                } else if (guestUser != null) {
                                        GuestUser existingGuestUser = guestUserRepository.getGuestUserbyEmail(email);
                                        existingGuestUser.setOne_time_password(one_time_passwordEncoded);
                                        guestUserRepository.save(existingGuestUser);
                                }
                                if (checkExaminee == null) {
                                        Long maxTestExamineeID = testExamineeRepository.getExamineeTableMaxID();
                                        GuestUser newGuestUser = guestUserRepository.findByMail(email);
                                        TestExaminee testExaminee = new TestExaminee(maxTestExamineeID, test, null, newGuestUser,
                                                        null);
                                        testExamineeRepository.save(testExaminee);
                                }

                                new Thread(new Runnable() {
                                        public void run() {
                                                try {
                                                        if (guestUser == null) {
                                                                GuestUser newGuestUser = guestUserRepository
                                                                                .findByMail(email);
                                                                mailService.SendGuestOneTimePassword(newGuestUser,
                                                                                one_time_password);
                                                                logger.info("One-time-password (OTP) sent to mail={} | Success",
                                                                                email);
                                                        } else {
                                                                mailService.SendGuestOneTimePassword(guestUser,
                                                                                one_time_password);
                                                                logger.info("One-time-password (OTP) sent to mail={} | Success",
                                                                                email);
                                                        }
                                                } catch (Exception e) {
                                                        logger.info(e.getLocalizedMessage());
                                                }
                                        }
                                }).start();
                                return ResponseEntity.ok(HttpStatus.OK);

                        }
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

                } catch (

                Exception e) {
                        logger.error(e.getLocalizedMessage());
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
                        // return "500";
                }
        }

        // Edit a guest in an exam
        @Valid
        @PostMapping(value = { "/teacher/edit-single-guest", "/admin/edit-single-guest" })
        private ResponseEntity editSingleGuest(@RequestBody String body) throws ParseException {
                try {
                        logger.info("Called API name: editSingleGuest with Parameters: {}", body);

                        JSONObject jsonObject = new JSONObject(body);
                        Long test_id = jsonObject.getLong("test_id");
                        Long guest_id = jsonObject.getLong("guest_id");
                        String name = jsonObject.getString("guest_name");
                        String email = jsonObject.getString("guest_email").toLowerCase();
                        String phone_number = jsonObject.getString("guest_ph_no");

                        GuestUser savedGuestUser;
                        String one_time_password = createOneTimePassword();
                        String one_time_passwordEncoded = passwordEncoder.encode(one_time_password);
                        String password_update_date_time = getDateAndTime();
                        String updatedDateTime = getDateAndTime();

                        // logger.info("Initiate Opearation Update Table: guest For guest_id={} Data:
                        // name={}, mail={}, phone_no={}",
                        // guest_id, name, email, phone_number);

                        // // Check if the email exists in the user_account table
                        // List<String> userAccountEmails = userAccountRepository.getAllUserEmail();
                        // if (userAccountEmails.contains(email)) {
                        // logger.warn("Opearation Update Table: guest Data: guest_id={}, name={},
                        // mail={}, phone_no={} | Failed",
                        // guest_id, name, email, phone_number);
                        // logger.warn("Email: {} already existes in Table: user_account", email);
                        // return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        // .body("{\"errorMessage\": \"Added Email is already associated with a Student
                        // account\"}");
                        // }

                        GuestUser oldGuestUser = guestUserRepository.findByGuestId(guest_id);
                        Test test = testRepository.getById(test_id);
                        TestExaminee deletedExaminee = testExamineeRepository.findByTestIdAndGuestId(test_id, guest_id);
                        testExamineeRepository.delete(deletedExaminee);
                        new Thread(new Runnable() {
                                public void run() {
                                        try {
                                                mailService.SendGuestRemovedNotification(oldGuestUser, test);
                                        } catch (Exception e) {
                                                logger.error(e.getLocalizedMessage());
                                        }
                                }
                        }).start();

                        logger.info("Initiate to Operation Retrieve Table {} by query {}",
                                        "guest",
                                        "findByGuestId(guest_id)");
                        GuestUser existingGuest = guestUserRepository.findByGuestId(guest_id);
                        logger.info("Operation Retrieve Table {} by query {} Result List {} Success",
                                        "guest",
                                        "findByGuestId(guest_id)",
                                        existingGuest);
                        GuestUser checkGuestByEmail = guestUserRepository.findByMail(email);
                        TestExaminee testExaminee;
                        if (checkGuestByEmail == null) {
                                GuestUser newGuestUser = new GuestUser(null, name, email, phone_number,
                                                one_time_passwordEncoded,
                                                password_update_date_time,
                                                null, null);
                                savedGuestUser = guestUserRepository.save(newGuestUser);
                                testExaminee = new TestExaminee(null, test, null, newGuestUser, null);
                                testExamineeRepository.save(testExaminee);
                                new Thread(new Runnable() {
                                        public void run() {
                                                try {
                                                        logger.info("Initiate Operation to send One-Time-Password to mail={}",
                                                                        email);
                                                        mailService.SendGuestOneTimePassword(
                                                                        savedGuestUser, one_time_password);
                                                        logger.info("Operation to send One-Time-Password to mail={} | Success",
                                                                        email);
                                                } catch (Exception e) {
                                                        logger.warn(e.getLocalizedMessage());
                                                }
                                        }
                                }).start();
                        } else {
                                Long maxTestExamineeID = testExamineeRepository.getExamineeTableMaxID();
                                testExaminee = new TestExaminee(maxTestExamineeID, test, null, checkGuestByEmail, null);
                                testExamineeRepository.save(testExaminee);
                                new Thread(new Runnable() {
                                        public void run() {
                                                try {
                                                        logger.info("Initiate Operation to send One-Time-Password to mail={}",
                                                                        email);
                                                        mailService.SendGuestOneTimePassword(
                                                                        checkGuestByEmail, one_time_password);
                                                        logger.info("Operation to send One-Time-Password to mail={} | Success",
                                                                        email);
                                                } catch (Exception e) {
                                                        logger.warn(e.getLocalizedMessage());
                                                }
                                        }
                                }).start();
                        }

                        // // Check the newly added email with the existing guest email
                        // if (existingGuest.getMail().equals(email)) {
                        // newGuestUser = new GuestUser(guest_id, name, existingGuest.getMail(),
                        // phone_number,
                        // existingGuest.getOne_time_password(),
                        // existingGuest.getPassword_update_date_time(), updatedDateTime,
                        // existingGuest.getDeleted_date_time());

                        // logger.info("Initiate to Operation Update Table {} Data {}",
                        // "guest",
                        // newGuestUser);
                        // guestUserRepository.save(newGuestUser);
                        // logger.info("Operation Update Table {} Data {} Success",
                        // "guest",
                        // newGuestUser);

                        // } else {
                        // // Check if the email exists in the guest table
                        // logger.info("Initiate to Operation Retrieve Table {} by query {}",
                        // "guest",
                        // "getGuestUserbyEmail(email)");
                        // GuestUser checkExistingGuest =
                        // guestUserRepository.getGuestUserbyEmail(email);
                        // logger.info("Operation Retrieve Table {} by query {} Result List {} Success",
                        // "guest",
                        // "getGuestUserbyEmail(email)",
                        // checkExistingGuest);

                        // // If the email exists in the table, delete the existing examinee and insert
                        // the
                        // // guest associated with the email
                        // if (checkExistingGuest != null) {
                        // logger.warn("Email: {} already existes in Table: guest", email);
                        // newGuestUser = new GuestUser(
                        // checkExistingGuest.getGuest_id(), name,
                        // checkExistingGuest.getMail(), phone_number,
                        // checkExistingGuest.getOne_time_password(),
                        // checkExistingGuest.getPassword_update_date_time(),
                        // updatedDateTime,
                        // checkExistingGuest.getDeleted_date_time());

                        // Test test = testRepository.getTestByID(test_id);

                        // // Insert new test_examinee data
                        // TestExaminee newTestExaminee = new TestExaminee(
                        // null,
                        // test,
                        // null,
                        // newGuestUser,
                        // null);
                        // logger.info("Initiate to Operation Insert Table: test_examinee Data: {}",
                        // newTestExaminee);
                        // testExamineeRepository.save(newTestExaminee);
                        // logger.info("Operation Insert Table: test_examinee Data: {} | Success",
                        // newTestExaminee);

                        // // Update guest data
                        // logger.info("Initiate to Operation Update Table: guest Data: guest_id={},
                        // name={}, email={}, phone_number={}",
                        // guest_id, name, email, phone_number);
                        // guestUserRepository.save(newGuestUser);
                        // logger.info("Operation Update Table: guest Data: guest_id={}, name={},
                        // email={}, phone_number={} | Success",
                        // guest_id, name, email, phone_number);

                        // logger.info("Initiate to Operation Retrieve Table {} by query {}",
                        // "test_examinee",
                        // "findByTestIdAndGuestId(testId, guestId)");
                        // TestExaminee viewTestGuest = testExamineeRepository
                        // .findByTestIdAndGuestId(test_id, guest_id);
                        // logger.info("Operation Retrieve Table {} by query {} Result List {} } Success
                        // Result List: {}",
                        // "test_examinee",
                        // "findByTestIdAndGuestId(testId, guestId)",
                        // viewTestGuest);
                        // // Delete old test_examinee data
                        // logger.info("Initiate to Operation Delete Table {} by query ",
                        // "test_examinee");
                        // testExamineeRepository.delete(viewTestGuest);
                        // logger.info("Initiate to Operation Delete Table {} {}",
                        // "test_examinee",
                        // "findByTestIdAndGuestId(testId, guestId)");

                        // new Thread(new Runnable() {
                        // public void run() {
                        // try {
                        // mailService.SendGuestRemovedNotification(existingGuest,
                        // test);
                        // logger.info("Notification of Removal of guest user from exam test_id={} sent
                        // to mail={} | Success",
                        // test_id, existingGuest.getMail());
                        // } catch (Exception e) {
                        // logger.info(e.getLocalizedMessage());
                        // }
                        // }
                        // }).start();
                        // }
                        // // Create a new password if the email does not exist in the table
                        // else {
                        // logger.info("New one-time-password is created for guest_id={}", guest_id);
                        // newOneTimePassword = createOneTimePassword();
                        // newEncodedOneTimePassword = passwordEncoder.encode(newOneTimePassword);
                        // passwordUpdatedDateTime = getDateAndTime();
                        // newGuestUser = new GuestUser(guest_id, name, email, phone_number,
                        // newEncodedOneTimePassword,
                        // passwordUpdatedDateTime, updatedDateTime, null);

                        // logger.info("Initiate to Operation Update Table {} Data {}",
                        // "guest",
                        // newGuestUser);
                        // guestUserRepository.save(newGuestUser);
                        // logger.info("Operation Update Table {} Data {} Success",
                        // "guest",
                        // newGuestUser);

                        // new Thread(new Runnable() {
                        // public void run() {
                        // try {
                        // mailService.SendGuestRemovedNotification(existingGuest,
                        // testRepository.getTestByID(test_id));
                        // logger.info(
                        // "Notification of Removal of guest user from exam test_id={} sent to mail={} |
                        // Success",
                        // test_id, existingGuest.getMail());
                        // } catch (Exception e) {
                        // logger.info(e.getLocalizedMessage());
                        // }

                        // try {
                        // mailService.SendGuestOneTimePassword(newGuestUser,
                        // newOneTimePassword);
                        // logger.info("One-time-password (OTP) sent to mail={} | Success",
                        // email);
                        // } catch (Exception e) {
                        // logger.info(e.getLocalizedMessage());
                        // }
                        // }
                        // }).start();
                        // }

                        // }
                        logger.info("Opearation Update Table: guest Data: guest_id={}, name={}, mail={}, phone_no={} | Success",
                                        guest_id, name, email, phone_number);
                        logger.info("Called API name: editSingleGuest with Parameters: {} | Success", body);

                        return ResponseEntity.ok(HttpStatus.OK);

                } catch (Exception e) {
                        logger.error(e.getLocalizedMessage());
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
                }

        }

        // Delete a guest from an exam
        @Valid
        @PostMapping("/delete-guest/{testId}/{guestId}/{roles}")
        public String deleteGuest(@PathVariable Long testId, @PathVariable Long guestId,
                        @PathVariable String roles) {

                try {
                        Long userID = getUid();
                        String deletedDateTime = getDateAndTime();

                        logger.info("user_id: {}, role: {}", userID, roles);
                        String role = "";
                        if (roles.equals("SUPER_ADMIN") || roles.equals("ADMIN")) {
                                role = "admin";
                        } else if (roles.equals("TEACHER")) {
                                role = "teacher";
                        }

                        logger.info("Called API name: deleteGuest with parameters: test_id={}, guest_id={}, role={}",
                                        testId,
                                        guestId,
                                        roles);
                        logger.info("Redirect /{}/exam/{}/guest/examinee with parameter(test_id={}, guest_id={}, role={})",
                                        role, testId,
                                        testId, guestId, role);

                        logger.info("Initiate to Operation Retrieve Table {} by query {}",
                                        "test_examinee",
                                        "findByTestIdAndGuestId(testId, guestId)");
                        TestExaminee viewTestGuest = testExamineeRepository.findByTestIdAndGuestId(testId, guestId);
                        logger.info("Operation Retrieve Table {} by query {} Result List {} Success",
                                        "test_examinee",
                                        "findByTestIdAndGuestId(testId, guestId)",
                                        viewTestGuest);

                        // Delete guest from test_examinee table
                        logger.info("Initiate Operation Delete Table: test_examinee by Query: test_id={}, guest_id={}",
                                        testId, guestId);
                        testExamineeRepository.delete(viewTestGuest);
                        logger.info("Operation Delete Table: test_examinee by Query: test_id={}, guest_id={} | Success",
                                        testId, guestId);

                        // Update guest deleted_date_time
                        logger.info("Initiate to Operation Retrieve Table {} by query {}",
                                        "guest",
                                        "findByGuestId(guestId)");
                        GuestUser guestUser = guestUserRepository.findByGuestId(guestId);
                        logger.info("Operation Retrieve Table {} by query {} Result List {} Success",
                                        "guest",
                                        "findByGuestId(guestId)",
                                        guestUser);

                        logger.info("Initiate to Operation Update Table guest Data: deleted_date_time={}",
                                        deletedDateTime);
                        guestUser.setDeleted_date_time(deletedDateTime);
                        guestUserRepository.save(guestUser);
                        logger.info("Operation Update Table guest Data: deleted_date_time={} | Success",
                                        deletedDateTime);

                        logger.info("Initiate to Operation Retrieve Table {} by query {}",
                                        "test",
                                        "getTestByID(testId)");
                        Test test = testRepository.getTestByID(testId);
                        logger.info("Operation Retrieve Table {} by query {} Result List {} Success",
                                        "test",
                                        "getTestByID(testId)",
                                        test);

                        // Send examinee removal email to guest examinee
                        new Thread(new Runnable() {
                                public void run() {
                                        try {
                                                mailService.SendGuestRemovedNotification(guestUser, test);
                                                logger.info(
                                                                "Notification of Removal of guest user from exam test_id={} sent to mail={} | Success",
                                                                testId, guestUser.getMail());
                                        } catch (Exception e) {
                                                logger.error(e.getLocalizedMessage());
                                        }
                                }
                        }).start();

                        logger.info("Redirect /{}/exam/{}/guest/examinee with parameter(test_id={}, guest_id={}, role={}) | Success",
                                        role,
                                        testId,
                                        testId, guestId, role);

                        // return "redirect:/exam/" + redirectId + "/examinee-list/";
                        logger.info("Called API name: deleteGuest with parameters: test_id={}, guest_id={}, role={} | Success",
                                        testId,
                                        guestId, role);

                        return "redirect:/" + role + "/exam/" + testId + "/guest/examinee";

                } catch (Exception e) {
                        logger.error(e.getLocalizedMessage());
                        return "500";
                }
        }

        // Renew a guest user's one-time password and sent it to guest user's email
        // address
        @Valid
        @PostMapping("/admin/renew-one-time-password")
        public ResponseEntity renewOneTimePassword(@RequestBody String data) throws ParseException {

                try {
                        logger.info("Called renewOneTimePassword with parameter: {}", data);

                        JSONObject jsonObject = new JSONObject(data);
                        String email = jsonObject.getString("guest_email").toLowerCase();
                        String one_time_password = createOneTimePassword();
                        String one_time_passwordEncoded = passwordEncoder.encode(one_time_password);
                        String password_update_date_time = getDateAndTime();

                        logger.info("Initiate to Operation Retrieve Table {} by query {}",
                                        "guest",
                                        "getGuestUserbyEmail(email)");
                        GuestUser guestUser = guestUserRepository.getGuestUserbyEmail(email);
                        logger.info("Operation Retrieve Table {} by query {} Result List {} Success",
                                        "guest",
                                        "getGuestUserbyEmail(email)",
                                        guestUser);

                        // Check if guest user actually exists
                        if (guestUser != null) {
                                guestUser.setPassword_update_date_time(password_update_date_time);
                                guestUser.setOne_time_password(one_time_passwordEncoded);

                                // Update one-time password and password update date time of guest user in guest
                                // table
                                logger.info("Initiate to Operation Update Table {} Data {} By {} = {}, {} = {}",
                                                "guest",
                                                guestUser,
                                                "one_time_password",
                                                one_time_passwordEncoded,
                                                "password_update_date_time",
                                                password_update_date_time);
                                guestUserRepository.save(guestUser);
                                logger.info("Operation Update Table {} Data {} By {} = {}, {} = {} Success",
                                                "guest",
                                                guestUser,
                                                "one_time_password",
                                                one_time_passwordEncoded,
                                                "password_update_date_time",
                                                password_update_date_time);

                                // Send a new one-time password to guest user
                                new Thread(new Runnable() {
                                        public void run() {
                                                try {
                                                        logger.info("Initiate Operation to send One-Time-Password to mail={}",
                                                                        email);
                                                        mailService.SendGuestOneTimePassword(
                                                                        guestUser, one_time_password);
                                                        logger.info("Operation to send One-Time-Password to mail={} | Success",
                                                                        email);
                                                } catch (Exception e) {
                                                        logger.warn(e.getLocalizedMessage());
                                                }
                                        }
                                }).start();
                        }

                } catch (Exception e) {
                        logger.warn("Called renewOneTimePassword with parameter: {} Failed", data);
                        logger.warn(e.getLocalizedMessage());
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                        .body("{\"errorMessage\": \"Something went wrong. Please try again.\"}");
                }

                logger.warn("Called renewOneTimePassword with parameter: {} Success", data);
                return ResponseEntity.ok(HttpStatus.OK);
        }
}
