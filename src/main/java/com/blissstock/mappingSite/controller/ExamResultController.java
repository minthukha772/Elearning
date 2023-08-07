package com.blissstock.mappingSite.controller;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.blissstock.mappingSite.entity.Test;
import com.blissstock.mappingSite.entity.TestExaminee;
import com.blissstock.mappingSite.entity.TestExamineeAnswer;
import com.blissstock.mappingSite.entity.TestQuestion;
import com.blissstock.mappingSite.entity.TestResult;
import com.blissstock.mappingSite.entity.UserInfo;
import com.blissstock.mappingSite.model.FileInfo;
import com.blissstock.mappingSite.model.StudentListForExamResult;
import com.blissstock.mappingSite.repository.CourseInfoRepository;
import com.blissstock.mappingSite.repository.JoinCourseUserRepository;
import com.blissstock.mappingSite.repository.ResultRepository;
import com.blissstock.mappingSite.repository.TestExamineeAnswerRepository;
import com.blissstock.mappingSite.repository.TestExamineeRepository;
import com.blissstock.mappingSite.repository.TestQuestionRepository;
import com.blissstock.mappingSite.repository.TestRepository;
import com.blissstock.mappingSite.repository.UserAccountRepository;
import com.blissstock.mappingSite.repository.UserRepository;
import com.blissstock.mappingSite.service.StorageService;
import com.blissstock.mappingSite.service.UserService;
import com.blissstock.mappingSite.service.UserSessionService;

@Controller
public class ExamResultController {

    private final static Logger logger = LoggerFactory.getLogger(ExamResultController.class);

    // @Autowired
    // private ResultService resultService;

    @Autowired
    StorageService storageService;

    @Autowired
    public ExamResultController(StorageService storageService) {
        this.storageService = storageService;
    }

    @Autowired
    private ResultRepository resultRepo;

    @Autowired
    private TestExamineeAnswerRepository testStudentAnswerRepository;

    @Autowired
    private TestQuestionRepository questionRepo;

    @Autowired
    private TestExamineeRepository testStudentRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private UserAccountRepository userAccountRepo;

    @Autowired
    private TestRepository testRepo;

    @Autowired
    private JoinCourseUserRepository joinCourseUserRepo;

    @Autowired
    private CourseInfoRepository courseInfoRepo;

    @Autowired
    private UserService userService;

    @Autowired
    UserSessionService userSessionService;

    @GetMapping("/student/exam-result/{testId}")
    // @GetMapping("/student/ExamResult")
    public String getExamResultForStudent(@PathVariable Long testId, Model model) {
        Long userID = getUid();
        Long courseID = getCid(testId);

        logger.info("Called CM0010_ExamResultStudent with parameter(test_id={})", testId);
        logger.info("user_id: {}, role: {}", userID, "Student");

        logger.info("Initiate Operation Retrieve Table result by Query: test_id={}, user_id={}", testId,
                userID);
        TestResult viewExamResult = resultRepo.getResultByTestIdAndUser(testId, userID);
        List<TestQuestion> viewQuestion = questionRepo.getQuestionByTest(testId);
        // StudentAnswer viewStuAns = stuAnsRepo.getResultByTestId(testId);

        if (viewExamResult != null) {
            String courseName = viewExamResult.getTest().getCourseInfo().getCourseName();
            String teacherName = viewExamResult.getTest().getCourseInfo().getUserInfo().getUserName();
            String examineeName = viewExamResult.getUser().getUserName();
            Long studentId = viewExamResult.getUser().getUid();
            Date examDate = viewExamResult.getTest().getDate();
            int timeAllowed = viewExamResult.getTest().getMinutes_allowed();
            String examStatus = viewExamResult.getTest().getExam_status();
            int maxMarks = 0;
            for (TestQuestion checkQuestionTable : viewQuestion) {
                int marksForEachQuest = checkQuestionTable.getMaximum_mark();
                maxMarks += marksForEachQuest;
            }
            // int maxMarks = viewQuestion.getMaximumMark();
            int stuMarks = viewExamResult.getResultMark();

            Double studentMarkPercent = ((double) stuMarks / (double) maxMarks) * 100;
            String formattedPercent = String.format("%.2f", studentMarkPercent);
            int passScorePercent = viewExamResult.getTest().getPassing_score_percent();
            String passOrfailResult = viewExamResult.getResult();
            String teacherComment = viewExamResult.getTeacherComment();

            model.addAttribute("courseName", courseName);
            model.addAttribute("teacherName", teacherName);
            model.addAttribute("examineeName", examineeName);
            model.addAttribute("studentId", studentId);
            model.addAttribute("examDate", examDate);
            model.addAttribute("timeAllowed", timeAllowed);
            model.addAttribute("examType", "");
            model.addAttribute("studentMarkPercent", formattedPercent);
            model.addAttribute("passScorePercent", passScorePercent);
            model.addAttribute("passOrfailResult", passOrfailResult);
            model.addAttribute("teacherComment", teacherComment);
            model.addAttribute("examStatus", examStatus);

            logger.info(
                    "Operation Retrieve Table result by Query: test_id={}, course_id={}, user_id={} Result: result_id={}, result={}, mark={}, teacher_comment={}, test_id={}, user_id={}  | Success",
                    testId, courseID, userID, viewExamResult.getResultId(), passOrfailResult, stuMarks, teacherComment,
                    testId, userID);

        } else {
            logger.warn(
                    "Failed to retrieved data from Table result where test_id: {}, course_id: {}, user_id: {} | Not Found",
                    testId, courseID, userID);
        }

        return "CM0010_ExamResultStudent";
    }

    private Long getUid() {
        Long uid = userSessionService.getUserAccount().getAccountId();
        return uid;
    }

    private Long getCid(Long testID) {
        Test viewTestTable = testRepo.getById(testID);
        Long courseId = viewTestTable.getCourseInfo().getCourseId();

        return courseId;
    }

    // @GetMapping("/exam/{testId}/examinee-list")
    // // @GetMapping("/Exam/ExamineeList")
    // public String getExamineeListForTeacherAndAdmin(@PathVariable Long testId,
    // Model model) {
    // // public String getExamineeListForTeacherAndAdmin( Model model) {

    // List<TestStudentList> testStudentLists = new ArrayList<>();
    // List<StudentListForExamineeList> studentListForExamineeLists = new
    // ArrayList<>();
    // List<TestStudent> viewStudents = testStudentRepo.getResultByTestId(testId);
    // List<UserAccount> viewAllStudents =
    // userAccountRepo.findByRole("ROLE_STUDENT");

    // Integer checkedStudent = 0;
    // Integer noOfEnrolledStudent = 0;

    // if (viewStudents.isEmpty()) {
    // model.addAttribute("noStudents", true);
    // } else {
    // for (TestStudent testStudent : viewStudents) {
    // String studentName = testStudent.getUser().getUserInfo().getUserName();
    // String studentPhoneNumber = testStudent.getUser().getUserInfo().getPhoneNo();
    // String studentEmail = testStudent.getUser().getMail();

    // Long testStudentId = testStudent.getTestStudentId();
    // MarkedAnswer viewMarkedAnswer =
    // markedAnswerRepo.getResultByTestStudentId(testStudentId);
    // if (viewMarkedAnswer != null) {
    // Integer checkedQuestion = viewMarkedAnswer.getMarkedQuestions();
    // Integer totalQuestion = viewMarkedAnswer.getTotalQuestions();
    // if (checkedQuestion.equals(totalQuestion)) {

    // checkedStudent += 1;
    // }
    // noOfEnrolledStudent += 1;
    // Long uid = testStudent.getUser().getAccountId();
    // UserInfo userInfo = userRepo.getById(uid);
    // try {
    // FileInfo profilePic = storageService.loadProfileAsFileInfo(userInfo);
    // // model.addAttribute("profilePic", profilePic);
    // testStudentLists.add(new TestStudentList(studentName, studentPhoneNumber,
    // studentEmail,
    // checkedQuestion, totalQuestion, profilePic, testStudentId));
    // model.addAttribute("students", testStudentLists);
    // } catch (Exception e) {
    // e.printStackTrace();
    // logger.info("unable to get profile {}", uid);
    // }

    // } else {
    // Integer checkedQuestion = 0;
    // Integer totalQuestion = 0;
    // if (checkedQuestion.equals(totalQuestion) && totalQuestion != 0) {

    // checkedStudent += 1;
    // }
    // noOfEnrolledStudent += 1;
    // Long uid = testStudent.getUser().getAccountId();
    // UserInfo userInfo = userRepo.getById(uid);
    // try {
    // FileInfo profilePic = storageService.loadProfileAsFileInfo(userInfo);
    // // model.addAttribute("profilePic", profilePic);
    // testStudentLists.add(new TestStudentList(studentName, studentPhoneNumber,
    // studentEmail,
    // checkedQuestion, totalQuestion, profilePic, testStudentId));
    // model.addAttribute("students", testStudentLists);
    // } catch (Exception e) {
    // e.printStackTrace();
    // logger.info("unable to get profile {}", uid);
    // }

    // }

    // }

    // }

    // if (!viewAllStudents.isEmpty()) {
    // for (UserAccount userAccountList : viewAllStudents) {
    // boolean userFound = false;
    // if (!viewStudents.isEmpty()) {
    // for (TestStudent testStudent : viewStudents) {
    // if
    // (userAccountList.getAccountId().equals(testStudent.getUser().getAccountId()))
    // {
    // userFound = true;
    // break;
    // }
    // }
    // if (!userFound) {
    // String studentName = userAccountList.getUserInfo().getUserName();
    // String studentPhoneNumber = userAccountList.getUserInfo().getPhoneNo();
    // String studentEmail = userAccountList.getMail();

    // Long uid = userAccountList.getAccountId();
    // UserInfo userInfo = userRepo.getById(uid);
    // try {
    // FileInfo profilePic = storageService.loadProfileAsFileInfo(userInfo);
    // // model.addAttribute("profilePic", profilePic);
    // studentListForExamineeLists.add(new StudentListForExamineeList(studentName,
    // studentPhoneNumber, studentEmail, profilePic, testId, uid));
    // model.addAttribute("studentLists", studentListForExamineeLists);
    // } catch (Exception e) {
    // e.printStackTrace();
    // logger.info("unable to get profile {}", uid);
    // }

    // } else {

    // continue;
    // }

    // } else {

    // String studentName = userAccountList.getUserInfo().getUserName();
    // String studentPhoneNumber = userAccountList.getUserInfo().getPhoneNo();
    // String studentEmail = userAccountList.getMail();

    // Long uid = userAccountList.getAccountId();
    // UserInfo userInfo = userRepo.getById(uid);
    // try {
    // FileInfo profilePic = storageService.loadProfileAsFileInfo(userInfo);
    // // model.addAttribute("profilePic", profilePic);
    // studentListForExamineeLists.add(new StudentListForExamineeList(studentName,
    // studentPhoneNumber, studentEmail, profilePic, testId, uid));
    // model.addAttribute("studentLists", studentListForExamineeLists);
    // } catch (Exception e) {
    // e.printStackTrace();
    // logger.info("unable to get profile {}", uid);
    // }

    // }

    // }
    // }

    // model.addAttribute("enrolledStudent", noOfEnrolledStudent);
    // model.addAttribute("checkedStudent", checkedStudent);
    // model.addAttribute("testId", testId);

    // return "AT0005_ExamineeList";
    // }

    // @PostMapping("/exam/examinee-list/delete-test-student/{testStudentId}")
    @PostMapping("/delete-teststudent/{testId}/{studentId}/{role}")
    public String deleteTestStudent(@PathVariable Long testId, @PathVariable Long studentId,
            @PathVariable String role) {
        Long userID = getUid();

        logger.info("Redirect /{}/exam/{}/examinee with parameter(test_id={}, user_id={}, role={})", "admin", testId,
                testId, studentId, role);

        logger.info("user_id: {}, role: {}", userID, role);

        logger.info("Initiate Operation Delete Table test_student by Query: test_id={}, user_id{}",
                testId, userID);
        String roles = "";
        TestExaminee viewTestStudent = testStudentRepo.findByTestIdAndUid(testId, studentId);
        testStudentRepo.delete(viewTestStudent);
        logger.info("Operation Delete Table test_student by Query: test_id={}, user_id{} | Success",
                testId, userID);
        if (role.equals("SUPER_ADMIN") || role.equals("ADMIN")) {
            roles = "admin";
        } else if (role.equals("TEACHER")) {
            roles = "teacher";
        }
        logger.info("Redirect /{}/exam/{}/examinee with parameter(test_id={}, user_id={}, role={}) | Success", roles,
                testId, testId, studentId, role);

        // return "redirect:/exam/" + redirectId + "/examinee-list/";
        return "redirect:/" + roles + "/exam/" + testId + "/examinee";
    }

    // @GetMapping("/exam/examinee-list/add-all-enrolled-students/{testId}")
    // public String addAllEnrolledStudents(@PathVariable Long testId) {
    // Test viewTestTable = testRepo.getById(testId);
    // Long courseId = viewTestTable.getCourseInfo().getCourseId();
    // CourseInfo courseInfo = courseInfoRepo.findById(courseId).get();

    // List<JoinCourseUser> viewJoinUser = courseInfo.getJoin();
    // List<TestStudent> viewTestStudents =
    // testStudentRepo.getResultByTestId(testId);

    // if (!viewJoinUser.isEmpty()) {
    // for (JoinCourseUser joinUser : viewJoinUser) {
    // boolean userFound = false;
    // if (!viewTestStudents.isEmpty()) {
    // for (TestStudent testStudent : viewTestStudents) {
    // if (joinUser.getUserInfo().getUserAccount().getAccountId()
    // .equals(testStudent.getUser().getAccountId())
    // || joinUser.getUserInfo().getUserAccount().getRole()
    // .equals(UserRole.TEACHER.getValue())) {
    // userFound = true;
    // break;
    // }
    // }
    // if (!userFound) {
    // TestStudent newTestStudentId = new TestStudent();
    // // Long newStudentId =
    // joinUser.getUserInfo().getUserAccount().getAccountId();
    // String accountEmail = joinUser.getUserInfo().getUserAccount().getMail();
    // UserAccount user = userAccountRepo.findByMail(accountEmail);
    // newTestStudentId.setTest(viewTestTable);
    // newTestStudentId.setUser(user);
    // testStudentService.createTestStudent(newTestStudentId);
    // // payHistory.setPaymentAmount(amountPaid);
    // } else {
    // System.out.println("Same User from Join Course found in Test Student
    // database.");
    // continue;
    // }

    // } else if
    // (joinUser.getUserInfo().getUserAccount().getRole().equals(UserRole.STUDENT.getValue()))
    // {
    // TestStudent newTestStudentId = new TestStudent();
    // // Long newStudentId =
    // joinUser.getUserInfo().getUserAccount().getAccountId();
    // String accountEmail = joinUser.getUserInfo().getUserAccount().getMail();
    // UserAccount user = userAccountRepo.findByMail(accountEmail);
    // newTestStudentId.setTest(viewTestTable);
    // newTestStudentId.setUser(user);
    // testStudentService.createTestStudent(newTestStudentId);
    // }

    // }
    // } else {
    // // handle case where one or both lists are empty
    // System.out.println("No Student found in Join Course database.");
    // }

    // return "redirect:/exam/" + testId + "/examinee-list/";
    // }

    // @GetMapping("/exam/examinee-list/add-custom-student/{testId}/{email}")
    // public String addCustomStudent(@PathVariable("testId") Long testId,
    // @PathVariable("email") String email) {

    // TestStudent newTestStudentId = new TestStudent();
    // UserAccount user = userAccountRepo.findByMail(email);
    // Test viewTestTable = testRepo.getById(testId);
    // newTestStudentId.setTest(viewTestTable);
    // newTestStudentId.setUser(user);
    // testStudentService.createTestStudent(newTestStudentId);

    // return "redirect:/exam/" + testId + "/examinee-list/";
    // }

    @GetMapping({ "admin/exam/{testId}/exam-result-list", "teacher/exam/{testId}/exam-result-list" })
    // @GetMapping("/Exam/exam-result-list")
    public String getExamResultListForTeacherAndAdmin(@PathVariable Long testId,
            Model model) {
        Long userID = getUid();
        // public String getExamResultListForTeacherAndAdmin( Model model) {
        logger.info("Called AT0008_ExamResultList with parameter(test_id={})", testId);
        logger.info("user_id: {}, role: {}", userID, userSessionService.getRole());
        logger.info(
                "Initiate Operation Retrieve Table test, test_student, result, test_student_answer, test_question by Query: test_id={}",
                testId);

        Test viewTest = testRepo.getById(testId);
        List<TestExaminee> viewTestStudents = testStudentRepo.getStudentByTest(testId);
        List<TestResult> viewResults = resultRepo.getListByTestId(testId);
        List<TestExamineeAnswer> viewStudentAnswer = testStudentAnswerRepository.getStudentAnswerListByTest(testId);
        List<TestQuestion> viewQuestions = questionRepo.getQuestionByTest(testId);
        List<StudentListForExamResult> studentListForExamResults = new ArrayList<>();

        String examTitle = viewTest.getDescription();
        String examStatus = viewTest.getExam_status();

        Date examDate = viewTest.getDate();
        LocalDate localExamDate = examDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        String startTime = viewTest.getStart_time();
        String endTime = viewTest.getEnd_time();

        int totalQuestion = 0;
        int correctAnswer = 0;

        int totalExamineeList = 0;
        int answeredStudents = 0;
        int passedStudents = 0;

        if (!viewTestStudents.isEmpty()) {

            for (TestExaminee testStudent : viewTestStudents) {
                totalExamineeList = totalExamineeList + 1;
                if (!viewResults.isEmpty()) {

                    for (TestResult result : viewResults) {
                        if (testStudent.getUserInfo().getUid()
                                .equals(result.getUser().getUid())
                                && result.getResultMark() != 0) {
                            answeredStudents = answeredStudents + 1;

                        }
                        if (testStudent.getUserInfo().getUid()
                                .equals(result.getUser().getUid())
                                && result.getResult().equals("Passed")) {
                            passedStudents = passedStudents + 1;

                        }
                    }

                } else {
                    logger.warn("Data Retrieved from Table result by Query: test_id={}, user_id={} is empty", testId,
                            userID);
                }

            }

            int notAnswered = totalExamineeList - answeredStudents;
            double passRate = ((double) passedStudents / (double) totalExamineeList) *
                    100;
            String formattedPassedPercent = String.format("%.1f", passRate);

            model.addAttribute("examTitle", examTitle);
            model.addAttribute("examStatus", examStatus);
            model.addAttribute("totalExamineeList", totalExamineeList);
            model.addAttribute("answeredStudents", answeredStudents);
            model.addAttribute("passRate", formattedPassedPercent);
            model.addAttribute("examDate", localExamDate);
            model.addAttribute("notAnswered", notAnswered);
            model.addAttribute("startTime", startTime);
            model.addAttribute("endTime", endTime);
        } else {
            logger.warn("Data retrieved from Table test_student by Query: test_id={}, user_id={} is empty", testId,
                    userID);
        }

        List<String> trueArr = new ArrayList<>();

        if (!viewQuestions.isEmpty()) {

            for (TestQuestion question : viewQuestions) {
                totalQuestion = 0;
                correctAnswer = 0;

                if (!viewStudentAnswer.isEmpty()) {

                    for (TestExamineeAnswer studentAnswer : viewStudentAnswer) {
                        if (question.getId().equals(studentAnswer.getQuestion().getId())) {
                            totalQuestion = totalQuestion + 1;
                            if (question.getId().equals(studentAnswer.getQuestion().getId())
                                    && studentAnswer.getCorrect_status().equals("TRUE")) {
                                correctAnswer = correctAnswer + 1;
                            }

                        }
                    }

                } else {
                    logger.warn(
                            "Data retrieved from Table test_student_answer by Query: question_id={} test_id={}, user_id={} is empty",
                            question.getId(), testId, userID);
                }
                if (totalQuestion != 0) {
                    double truePercent = ((double) correctAnswer / (double) totalQuestion) * 100;
                    String formattedPercent = String.format("%.2f", truePercent);

                    trueArr.add(formattedPercent);
                }

            }

        } else {
            logger.warn("Data retreived from Table test_question by Query: test_id={} is empty", testId);
        }
        model.addAttribute("trueArr", trueArr);

        if (!viewTestStudents.isEmpty()) {

            for (TestExaminee testStudent : viewTestStudents) {
                String studentEmail = testStudent.getUserInfo().getUserAccount().getMail();
                String studentName = testStudent.getUserInfo().getUserName();
                String studentPhone = testStudent.getUserInfo().getPhoneNo();

                Integer maxMarks = 0;
                for (TestQuestion checkQuestionTable : viewQuestions) {
                    int marksForEachQuest = checkQuestionTable.getMaximum_mark();
                    maxMarks += marksForEachQuest;
                }
                Long userId = testStudent.getUserInfo().getUid();

                logger.info("Initiate Operation Retrieve Table result by Query: test_id={}, user_id={}", testId,
                        userID);
                TestResult viewExamResult = resultRepo.getResultByTestIdAndUser(testId, userId);

                if (viewExamResult != null) {

                    Integer stuMarks = viewExamResult.getResultMark();
                    String examResult = viewExamResult.getResult();

                    Long uid = testStudent.getUserInfo().getUid();
                    UserInfo userInfo = userRepo.getById(uid);

                    try {
                        FileInfo profilePic = storageService.loadProfileAsFileInfo(userInfo);
                        // model.addAttribute("profilePic", profilePic);
                        studentListForExamResults.add(new StudentListForExamResult(studentName,
                                studentEmail,
                                studentPhone, examResult, stuMarks, maxMarks, profilePic, uid));
                        model.addAttribute("students", studentListForExamResults);

                    } catch (Exception e) {
                        e.printStackTrace();
                        logger.info("unable to get profile {}", uid);
                    }

                    logger.info(
                            "Operation Retrieve Table result by Query: test_id={}, user_id={} Result: result_id={}, result={}, mark={}, teacher_comment={} | Success",
                            testId,
                            userID);

                } else {
                    logger.warn("Data Retrieved from Table result by Query: test_id={}, user_id={} is empty", testId,
                            userID);
                }
            }
        } else {
            logger.warn("Data retrieved from Table test_student by Query: test_id={}, user_id={} is empty", testId,
                    userID);
        }
        model.addAttribute("user_role", userSessionService.getRole());
        model.addAttribute("test_id", testId);
        logger.info(
                "Operation Retrieve Table test, test_student, result, test_student_answer, test_question by Query: test_id={}\nResult {} | Success",
                testId, model);

        logger.info("Called AT0008_ExamResultList with parameter(test_id={}) | Success", testId);

        return "AT0008_ExamResultList";
    }

    private void info(String string, Long testId) {
    }

}