// package com.blissstock.mappingSite.controller;

// import org.checkerframework.checker.guieffect.qual.UI;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Controller;
// import org.springframework.transaction.annotation.Transactional;
// import org.springframework.ui.Model;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.ModelAttribute;
// import org.springframework.web.bind.annotation.PathVariable;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RequestMethod;
// import org.springframework.web.bind.annotation.RequestParam;

// import java.text.DateFormat;
// import java.text.SimpleDateFormat;
// import java.util.ArrayList;
// import java.util.Date;
// import java.util.List;
// import java.util.Optional;

// import com.blissstock.mappingSite.entity.CourseInfo;
// import com.blissstock.mappingSite.entity.CourseTime;
// import com.blissstock.mappingSite.entity.UserAccount;
// import com.blissstock.mappingSite.entity.UserInfo;
// import com.blissstock.mappingSite.repository.CourseRepository;
// import com.blissstock.mappingSite.repository.UserAccountRepository;
// import com.blissstock.mappingSite.repository.UserInfoRepository;
// import com.blissstock.mappingSite.service.CourseListService;

// @Controller

// public class CourseListController {

// @Autowired
// private CourseRepository courseRepo;

// @Autowired
// private CourseListService courseListService;

// @Autowired
// private UserAccountRepository userAccountRepo;

// @Autowired
// private UserInfoRepository userInfoRepo;

// @RequestMapping("/guest-course-list")
// private String getCourseListGuest(Model model) {

// List<CourseInfo> allList = courseRepo.findAll();
// System.out.print("Here is : " + allList.get(0).getAboutCourse());
// model.addAttribute("courseList", allList);
// return "CM0002_CourseListGuest";
// }

// // @GetMapping("/course-list-guest/{id}")
// // private String courseDetails(@PathVariable("id") Long courseId){
// // Optional<CourseInfo> courseInfo = courseRepo.findById(courseId);
// // System.out.println("testing" + courseInfo.get().getAboutCourse());
// // return "helloworld";
// // }

// @RequestMapping("/course-list")
// private String getCourseList() {

// // List<CourseInfo> allList = courseRepo.findAll();
// return "CM0002_CourseListGuest";
// // model.addAttribute("courseList", allList);
// // return "CM0002_CourseListGuest";
// }

// @RequestMapping("/student-course-list")
// private String getCourseListStudent() {
// return "CM0002_CourseListStudent";
// }

// @RequestMapping("/teacher-course-list")
// private String getCourseListTeacher() {
// return "CM0002_CourseListTeacher";
// }

// @RequestMapping(value = "/courseSearch", method = RequestMethod.POST)
// private String searchCourse(@RequestParam("course-name") String courseName,
// @RequestParam("teacher-name") String teacherName,
// @RequestParam("from-date") String fromDate,
// @RequestParam("to-date") String toDate, Model model) {
// DateFormat outputFormatter = new SimpleDateFormat("yyyy-MM-dd");
// String searchResultError = "Your search result exceed limit. We will show
// 1400 results";

// // find by course name
// if (courseName != "" && teacherName == "" && fromDate == "" && toDate == "")
// {
// List<CourseInfo> course = courseRepo.findByCourseName(courseName);
// if (course == null) {
// model.addAttribute("noCourse", "There is no course with the given name");
// model.addAttribute("resultSize", course.size());
// return "CM0002_CourseListGuest";
// } else {
// model.addAttribute("courseList", course);
// model.addAttribute("resultSize", course.size());

// }

// if (course.size() > 1400) {
// model.addAttribute("searchResultError", searchResultError);
// }

// }

// // Find by teacher name
// else if (courseName == "" && teacherName != "" && fromDate == "" && toDate ==
// "") {

// List<UserAccount> userAccountList = userAccountRepo.findByRole("teacher");
// List<UserInfo> userInfoList = new ArrayList<UserInfo>();

// List<CourseInfo> courseList = new ArrayList<CourseInfo>();

// for (UserAccount userAccount : userAccountList) {
// UserInfo uInfo = userInfoRepo.findByNameAndAccount(teacherName,
// userAccount.getAccountId());
// userInfoList.add(uInfo);
// }

// for (UserInfo userInfo : userInfoList) {
// List<CourseInfo> cInfo = courseRepo.findByUserInfo(userInfo);

// for (CourseInfo courseInfo1 : cInfo) {
// courseList.add(courseInfo1);

// }
// }

// if (courseList.size() == 0) {
// model.addAttribute("noCourse", "There is no course with the given name");
// model.addAttribute("resultSize", courseList.size());
// return "CM0002_CourseListGuest";
// } else {
// model.addAttribute("courseList", courseList);
// model.addAttribute("resultSize", courseList.size());
// }

// }

// // Find by course name and teacher name
// else if (courseName != "" && teacherName != "" && fromDate == "" && toDate ==
// "") {
// List<UserAccount> userAccountList = userAccountRepo.findByRole("teacher");
// List<UserInfo> userInfoList = new ArrayList<UserInfo>();

// List<CourseInfo> courseList = new ArrayList<CourseInfo>();

// for (UserAccount userAccount : userAccountList) {
// UserInfo uInfo = userInfoRepo.findByNameAndAccount(teacherName,
// userAccount.getAccountId());
// userInfoList.add(uInfo);
// }

// for (UserInfo userInfo : userInfoList) {
// List<CourseInfo> cInfo = courseRepo.findByUserInfo(userInfo);

// for (CourseInfo courseInfo1 : cInfo) {
// if (courseInfo1.getCourseName().equals(courseName)) {
// courseList.add(courseInfo1);
// }

// }
// }

// if (courseList.size() == 0) {
// model.addAttribute("noCourse", "There is no course with the given name");
// model.addAttribute("resultSize", courseList.size());
// return "CM0002_CourseListGuest";
// } else {
// model.addAttribute("courseList", courseList);
// model.addAttribute("resultSize", courseList.size());
// }

// }

// // Find by course name, teacher name and start date
// else if (courseName != "" && teacherName != "" && fromDate != "" && toDate ==
// "") {
// List<UserAccount> userAccountList = userAccountRepo.findByRole("teacher");
// List<UserInfo> userInfoList = new ArrayList<UserInfo>();

// List<CourseInfo> courseList = new ArrayList<CourseInfo>();

// for (UserAccount userAccount : userAccountList) {
// UserInfo uInfo = userInfoRepo.findByNameAndAccount(teacherName,
// userAccount.getAccountId());
// userInfoList.add(uInfo);
// }

// for (UserInfo userInfo : userInfoList) {

// List<CourseInfo> cInfo = courseRepo.findByUserInfo(userInfo);

// for (CourseInfo courseInfo1 : cInfo) {

// String output = outputFormatter.format(courseInfo1.getStartDate());
// if (courseInfo1.getCourseName().equals(courseName) &&
// output.equals(fromDate)) {
// courseList.add(courseInfo1);
// }

// }
// }

// if (courseList.size() == 0) {
// model.addAttribute("noCourse", "There is no course with the given name");
// return "CM0002_CourseListGuest";
// } else {
// model.addAttribute("courseList", courseList);
// }

// }

// // Find by Course name, Teacher name, Start Date and End Date
// else if (courseName != "" && teacherName != "" && fromDate != "" && toDate !=
// "") {
// List<UserAccount> userAccountList = userAccountRepo.findByRole("teacher");
// List<UserInfo> userInfoList = new ArrayList<UserInfo>();

// List<CourseInfo> courseList = new ArrayList<CourseInfo>();

// for (UserAccount userAccount : userAccountList) {
// UserInfo uInfo = userInfoRepo.findByNameAndAccount(teacherName,
// userAccount.getAccountId());
// userInfoList.add(uInfo);
// }

// for (UserInfo userInfo : userInfoList) {

// List<CourseInfo> cInfo = courseRepo.findByUserInfo(userInfo);

// for (CourseInfo courseInfo1 : cInfo) {

// String startDate = outputFormatter.format(courseInfo1.getStartDate());
// String endDate = outputFormatter.format(courseInfo1.getEndDate());
// if (courseInfo1.getCourseName().equals(courseName) &&
// startDate.equals(fromDate)
// && endDate.equals(toDate)) {
// courseList.add(courseInfo1);
// }

// }
// }

// if (courseList.size() == 0) {
// model.addAttribute("noCourse", "There is no course with the given name");
// return "CM0002_CourseListGuest";
// } else {
// model.addAttribute("courseList", courseList);
// }

// }

// // else if(courseName=="" && teacherName=="" && fromDate!="" && toDate==""){
// // // DateFormat nextFormatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
// // // String startDate = fromDate + " 00:00:00";
// // Date startDate= new SimpleDateFormat("yyyy-MM-dd
// HH:mm:ss").parse(fromDate);
// // // Date startDate1 = startDate;

// // List<CourseInfo> courseList = courseRepo.findByStartDate(startDate);
// // model.addAttribute("courseList", courseList);
// // }

// return "CM0002_CourseListGuest";
// }

// @PostMapping("/courseFilter")
// private String courseFilter(
// Model model, @ModelAttribute("level") String levelList,
// @ModelAttribute("category") String categoryList,
// @ModelAttribute("method") String methodList) {
// System.out.println(levelList);
// System.out.println(categoryList);
// System.out.println(methodList);
// if (!levelList.equals("") && categoryList.equals("") &&
// methodList.equals("")) {
// List<CourseInfo> courseList = courseRepo.findByLevel(levelList);
// model.addAttribute("courseList", courseList);
// model.addAttribute("level", levelList);

// }

// else if (levelList.equals("") && !categoryList.equals("") &&
// methodList.equals("")) {
// List<CourseInfo> courseList = courseRepo.findByCategory(categoryList);
// model.addAttribute("courseList", courseList);
// model.addAttribute("category", categoryList);

// }

// else if (levelList.equals("") && categoryList.equals("") &&
// !methodList.equals("")) {
// List<CourseInfo> courseList = courseRepo.findByClassType(methodList);
// model.addAttribute("courseList", courseList);
// model.addAttribute("classType", methodList);

// }

// else if (!levelList.equals("") && !categoryList.equals("") &&
// methodList.equals("")) {
// List<CourseInfo> courseList = courseRepo.findByLevelAndCategory(levelList,
// categoryList);
// model.addAttribute("courseList", courseList);
// model.addAttribute("category", categoryList);
// model.addAttribute("level", levelList);

// }

// else if (!levelList.equals("") && categoryList.equals("") &&
// !methodList.equals("")) {
// List<CourseInfo> courseList = courseRepo.findByLevelAndClassType(levelList,
// methodList);
// model.addAttribute("courseList", courseList);
// model.addAttribute("classType", methodList);
// model.addAttribute("level", levelList);

// }

// else if (levelList.equals("") && !categoryList.equals("") &&
// !methodList.equals("")) {
// List<CourseInfo> courseList =
// courseRepo.findByCategoryAndClassType(categoryList, methodList);
// model.addAttribute("courseList", courseList);
// model.addAttribute("classType", methodList);
// model.addAttribute("category", categoryList);

// }

// else if (!levelList.equals("") && !categoryList.equals("") &&
// !methodList.equals("")) {
// List<CourseInfo> courseList =
// courseRepo.findByLevelAndCategoryAndClassType(levelList, categoryList,
// methodList);
// model.addAttribute("courseList", courseList);
// model.addAttribute("classType", methodList);
// model.addAttribute("category", categoryList);
// model.addAttribute("level", levelList);

// }

// else {
// List<CourseInfo> courseList = courseRepo.findAll();
// model.addAttribute("courseList", courseList);
// }

// // System.out.println(levelList.get(0));
// // System.out.println(levelList.get(1));
// return "CM0002_CourseListGuest";
// }

// @RequestMapping("/courseInfo")
// private String courseRegister(Model model) {
// // CourseInfo courseInfo = courseRepo.findById((long) 1).get();
// CourseInfo courseInfo = new CourseInfo();
// model.addAttribute("course", courseInfo);
// return "test";
// }

// @RequestMapping("/userInfo")
// private String userInfoRegister(Model model) {
// UserInfo userInfo = new UserInfo();
// model.addAttribute("userInfo", userInfo);
// return "testUserInfo";
// }

// @RequestMapping("/userAccount")
// private String userAccountRegister(Model model) {
// UserAccount userAccount = new UserAccount();
// userAccount.setMailVerified(true);
// model.addAttribute("userAccount", userAccount);
// return "userAccount";
// }

// @PostMapping("/coursePost")
// private String coursePost(@ModelAttribute("course") CourseInfo cInfo) {

// courseRepo.save(cInfo);
// return "helloworld";
// }

// @PostMapping("/userInfoPost")
// private String userInfoPost(@ModelAttribute("userInfo") UserInfo uInfo) {

// userInfoRepo.save(uInfo);
// return "helloworld";
// }

// @PostMapping("/userAccountPost")
// private String userAccountPost(@ModelAttribute("userAccount") UserAccount
// userAccount) {
// userAccountRepo.save(userAccount);
// return "helloworld";
// }

// @RequestMapping("/testUserInfo")
// private String userInfoUpload() {
// return "testUserInfo";
// }

// @RequestMapping("/accToUserInfoAdd")
// private String accToUserInfoAdd() {
// return "accToUserInfo";
// }

// @RequestMapping("/userAndCourseAdd")
// private String userAndCourseAdd() {
// return "userInfoAndCourseInfo";
// }

// @PostMapping("/accToUserInfo")
// private String accToUserInfo(@ModelAttribute("uid") Long uid,
// @ModelAttribute("accountId") Long accId) {
// UserInfo userInfo = userInfoRepo.findById(uid).get();
// UserAccount userAccount = userAccountRepo.findById(accId).get();
// userInfo.setUserAccount(userAccount);
// userInfoRepo.save(userInfo);
// return "helloworld";
// }

// @PostMapping("/userAndCourse")
// private String userAndCourse(@ModelAttribute("uid") Long uid,
// @ModelAttribute("courseId") Long courseId) {
// List<CourseInfo> courseInfoList = new ArrayList<>();
// List<UserInfo> userInfoList = new ArrayList<>();
// CourseInfo courseInfo = courseRepo.findById(courseId).get();
// UserInfo userInfo = userInfoRepo.findById(uid).get();
// courseInfoList.add(courseInfo);
// userInfoList.add(userInfo);
// userInfo.setCourseInfo(courseInfoList);
// userInfoRepo.save(userInfo);
// courseInfo.setUserInfo(userInfoList);
// courseRepo.save(courseInfo);
// return "helloworld";
// }

// @RequestMapping("/hello")
// private String helloTest() {
// Date date = new Date();
// // UserInfo userInfo = new UserInfo();
// // userInfo.setAddress("Yangon");
// // userInfo.setBirthDate(date);
// // userInfo.setCertificate("certificate");
// // userInfo.setCity("Ygn");
// // userInfo.setDivision("Thingangyun");
// // userInfo.setEducation("BED");
// // userInfo.setGender("Female");
// // userInfo.setNrc("nrc");
// // userInfo.setPhoneNo("09788999999");
// // userInfo.setPostalCode("11111");
// // userInfo.setSelfDescription("This is me");
// // userInfo.setUserName("Daw Aye");

// // UserAccount userAccount = new UserAccount();
// // userAccount.setAccountStatus("status");
// // userAccount.setMailVerified(true);
// // userAccount.setMail("abc@gmail.com");
// // userAccount.setPassword("1234");
// // userAccount.setPhoto("/abc");
// // userAccount.setRegisteredDate(date);
// // userAccount.setRole("teacher");

// // // userAccountRepo.save(userAccount);
// // userInfo.setUserAccount(userAccount);

// // UserInfo userInfo1 = new UserInfo();
// // userInfo1.setAddress("Yangon");
// // userInfo1.setBirthDate(date);
// // userInfo1.setCertificate("certificate");
// // userInfo1.setCity("Ygn");
// // userInfo1.setDivision("Thingangyun");
// // userInfo1.setEducation("BED");
// // userInfo1.setGender("Female");
// // userInfo1.setNrc("nrc");
// // userInfo1.setPhoneNo("09788999999");
// // userInfo1.setPostalCode("11111");
// // userInfo1.setSelfDescription("This is me");
// // userInfo1.setUserName("Daw Aye Thaung");

// // UserAccount userAccount1 = new UserAccount();
// // userAccount1.setAccountStatus("status");
// // userAccount1.setMailVerified(true);
// // userAccount1.setMail("abc@gmail.com");
// // userAccount1.setPassword("1234");
// // userAccount1.setPhoto("/abc");
// // userAccount1.setRegisteredDate(date);
// // userAccount1.setRole("teacher");

// // // userAccountRepo.save(userAccount1);
// // userInfo1.setUserAccount(userAccount1);

// // CourseInfo cInfo = new CourseInfo();
// // cInfo.setAboutCourse("aboutCourse");
// // cInfo.setCategory("Programming");
// // cInfo.setClassLink("localhost");
// // cInfo.setClassType("Live");
// // cInfo.setCourseName("Basic Programming");
// // cInfo.setEndDate(date);
// // cInfo.setFees(100);
// // cInfo.setIsCourseApproved("true");
// // cInfo.setLevel("Beginner");
// // cInfo.setStartDate(date);

// // CourseInfo cInfo1 = new CourseInfo();
// // cInfo1.setAboutCourse("aboutCourse");
// // cInfo1.setCategory("Programming");
// // cInfo1.setClassLink("localhost");
// // cInfo1.setClassType("Live");
// // cInfo1.setCourseName("Intermediate Programming");
// // cInfo1.setEndDate(date);
// // cInfo1.setFees(100);
// // cInfo1.setIsCourseApproved("true");
// // cInfo1.setLevel("Beginner");
// // cInfo1.setStartDate(date);

// // userInfo.set(cInfo);
// // userInfo.set(cInfo1);

// // cInfo.set(userInfo);
// // cInfo1.getUserInfo().add(userInfo);

// // userInfoRepo.save(userInfo);
// // userInfoRepo.save(userInfo1);

// // courseRepo.save(cInfo);
// // courseRepo.save(cInfo1);

// // List<CourseInfo> cInfoList = courseRepo.findAll();
// // cInfoList.add(cInfo);
// // cInfoList.add(cInfo1);

// // Long uInfoId = (long) 7;
// // UserInfo uInfo = userInfoRepo.findById(uInfoId).get();
// // uInfo.setCourseInfo(cInfoList);
// // userInfoRepo.save(uInfo);
// // System.out.println("Heehee" + userInfo);
// // System.out.print("BlaBla" + cInfo.getUserInfo());

// // List<UserInfo> userInfo = userInfoRepo.findAll();
// // CourseInfo cInfo = courseRepo.findById((long) 10).get();
// // cInfo.setUserInfo(userInfo);
// // courseRepo.save(cInfo);
// // System.out.println("SthSth" + userInfo.get(0).getCourseInfo());

// UserInfo helloUser = userInfoRepo.findById((long) 13).get();
// for (int i = 0; i < helloUser.getCourseInfo().size(); i++) {
// System.out.println("YayYay" +
// helloUser.getCourseInfo().get(i).getCourseName());
// }

// CourseInfo cInfo = courseRepo.findById((long) 10).get();
// for (int i = 0; i < cInfo.getUserInfo().size(); i++) {
// System.out.println("YayYay" + cInfo.getUserInfo().get(i).getUserName());
// }

// return "helloworld";
// }
// }
