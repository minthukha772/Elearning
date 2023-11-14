package com.blissstock.mappingSite.controller;

import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.text.DecimalFormat;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.CancellationException;

import javax.mail.Multipart;
import javax.servlet.http.HttpServletRequest;
import com.blissstock.mappingSite.dto.JoinCourseDTO;
import com.blissstock.mappingSite.entity.CourseInfo;
import com.blissstock.mappingSite.entity.CourseTime;
import com.blissstock.mappingSite.entity.JoinCourseUser;
import com.blissstock.mappingSite.entity.PaymentForTeacher;
import com.blissstock.mappingSite.entity.PaymentReceive;
import com.blissstock.mappingSite.entity.Review;
import com.blissstock.mappingSite.entity.Syllabus;
import com.blissstock.mappingSite.entity.Test;
import com.blissstock.mappingSite.entity.UserInfo;
import com.blissstock.mappingSite.entity.VideoClass;
import com.blissstock.mappingSite.enums.PaymentStatus;
import com.blissstock.mappingSite.enums.ClassType;
import com.blissstock.mappingSite.enums.UserRole;
import com.blissstock.mappingSite.exceptions.CourseNotFoundException;
import com.blissstock.mappingSite.model.FileInfo;
import com.blissstock.mappingSite.model.VideoClassList;
import com.blissstock.mappingSite.repository.CourseInfoRepository;
import com.blissstock.mappingSite.repository.CourseTimeRepository;
import com.blissstock.mappingSite.repository.JoinCourseUserRepository;
import com.blissstock.mappingSite.repository.PaymentRepository;
import com.blissstock.mappingSite.repository.SyllabusRepository;
import com.blissstock.mappingSite.repository.UserAccountRepository;
import com.blissstock.mappingSite.repository.UserInfoRepository;
import com.blissstock.mappingSite.repository.UserRepository;
import com.blissstock.mappingSite.repository.VideoClassRepository;
import com.blissstock.mappingSite.service.CourseService;
import com.blissstock.mappingSite.service.GoogleDriveService;
import com.blissstock.mappingSite.service.JoinCourseService;
import com.blissstock.mappingSite.service.StorageService;
import com.blissstock.mappingSite.service.UserService;
import com.blissstock.mappingSite.service.UserSessionService;

import com.google.api.client.http.InputStreamContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.FileList;
import com.google.api.services.drive.model.Permission;

import com.google.api.services.drive.model.File;
import com.google.api.client.http.InputStreamContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.FileList;
import com.blissstock.mappingSite.service.MailService;
import com.blissstock.mappingSite.service.PaymentForTeacherService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.multipart.MultipartFile;

import org.json.JSONObject;
import org.springframework.web.multipart.MultipartFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;

@Controller
public class CourseDetailsController {

    private static final Logger logger = LoggerFactory.getLogger(CourseDetailsController.class);
    @Autowired
    MailService mailService;
    @Autowired
    UserRepository userRepository;

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

    @Autowired
    StorageService storageService;

    @Autowired
    CourseTimeRepository courseTimeRepository;

    @Autowired
    private PaymentForTeacherService paymentForTeacherService;

    @Autowired
    private GoogleDriveService driveService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private VideoClassRepository videoClassRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Value("${com.blissstock.mapping-site.ENVIRONMENT}")
    String ENVIRONMENT;
    @Value("${com.blissstock.mapping-site.TESTING_DOMAIN}")
    String TESTING_DOMAIN;
    @Value("${com.blissstock.mapping-site.PRODUCTION_DOMAIN}")
    String PRODUCTION_DOMAIN;

    @GetMapping(value = { "/student/course-details/{courseId}", "/teacher/course-details/{courseId}",
            "/admin/course-details/{courseId}", "/guest/course-detail/{courseId}" })
    private String getCourseDetails(@PathVariable Long courseId, Model model) {
        Long userId;
        logger.info("Api name is: {}. Parameter is courseId: {} ", "getCourseDetails", courseId);

        logger.info("Initiate to Operation Retrieve Table: {} by query: findById={}", "courseInfo", courseId);
        // Get course by ID
        CourseInfo courseInfo = courseInfoRepository.findById(courseId).get();
        FileInfo fileInfo = storageService.loadCoursePhoto(courseInfo);
        logger.info("Initiate to Operation Retrieve Table: {} by query: findById={} Result  List : {} Success",
                "courseInfo", courseId, courseInfo.toString());

        // if profile is not found set as place holder
        if (fileInfo == null) {
            courseInfo.setCoursePhoto("https://via.placeholder.com/150");
        } else {

            courseInfo.setCoursePhoto(fileInfo.getUrl());
        }

        model.addAttribute("courseInfo", courseInfo);

        // get classlink
        String classLink = courseInfo.getClassLink();
        model.addAttribute("classLink", classLink);

        // get test links
        List<Test> testList = courseInfo.getTest();
        model.addAttribute("testList", testList);
        model.addAttribute("testListSize", testList.size());

        // isCourseApprove
        boolean courseNotApprove = courseInfo.getIsCourseApproved() == false;
        model.addAttribute("courseNotApprove", courseNotApprove);
        logger.info("The requested course approve boolean is {} ", courseNotApprove);

        // compare course start date to find course can be enrolled

        // Get trname and course name
        String trName = courseInfo.getUserInfo().getUserName();
        model.addAttribute("trName", trName);
        String courseName = courseInfo.getCourseName();
        model.addAttribute("courseName", courseName);

        // Get joinlist of the course

        // here
        List<JoinCourseUser> joinList = courseInfo.getJoin();
        List<Review> reviewList = new ArrayList<Review>();
        for (JoinCourseUser join : joinList) {
            reviewList.addAll(join.getReview());
        }

        // Get reveiwlist
        List<Review> courseReviewList = new ArrayList<Review>();
        List<FileInfo> userProfileList = new ArrayList<FileInfo>();
        for (Review courseReview : reviewList) {
            if (courseReview.getReviewType() == 0) {
                UserInfo tempUserInfo = courseReview.getJoin().getUserInfo();
                FileInfo profilePic = storageService.loadProfileAsFileInfo(tempUserInfo);
                tempUserInfo.setPhoto(profilePic.getUrl());
                courseReview.getJoin().setUserInfo(tempUserInfo);

                courseReviewList.add(courseReview);
                model.addAttribute("courseReviewList", courseReviewList);
            }
        }

        // first 3 reviews
        if (courseReviewList.size() >= 3) {
            List<Review> threeCourseReviewList = new ArrayList<>();
            threeCourseReviewList.add(courseReviewList.get(0));
            threeCourseReviewList.add(courseReviewList.get(1));
            threeCourseReviewList.add(courseReviewList.get(2));
            model.addAttribute("courseReviewList", threeCourseReviewList);
        } else {
            model.addAttribute("courseReviewList", courseReviewList);
        }

        logger.info("The review list is {}", courseReviewList.size());

        // reviewlist empty or not
        boolean courseReviewListEmpty = courseReviewList.isEmpty();
        boolean courseReveiwListNotEmpty = !courseReviewListEmpty;
        logger.info("The boolean of courseReviewListEmpty is {}", courseReviewListEmpty);
        model.addAttribute("courseReviewListEmpty", courseReviewListEmpty);
        model.addAttribute("courseReviewListNotEmpty", courseReveiwListNotEmpty);

        // average rating
        int total_stars = 0;
        int numCourseReviewList = courseReviewList.size();
        for (Review review : courseReviewList) {
            total_stars += review.getStar();
        }
        int average = 0;
        double averageDouble = 0;
        try {
            average = (int) total_stars / numCourseReviewList;
        } catch (ArithmeticException e) {
            e.getLocalizedMessage();
            average = 0;
        }
        try {
            averageDouble = (double) total_stars / numCourseReviewList;
        } catch (ArithmeticException e) {
            e.getLocalizedMessage();
            averageDouble = 0;
        }
        String averageFloat = String.format("%.2f", averageDouble);
        model.addAttribute("average", average);
        model.addAttribute("averageFloat", averageFloat);

        String classType = courseInfo.getClassType();
        boolean isLiveClass = classType.toUpperCase().equals("LIVE");
        model.addAttribute("isLiveClass", isLiveClass);

        if (classType.toUpperCase().equals(ClassType.LIVE.getValue())) {
            LocalDateTime now = LocalDateTime.now();
            Instant currentDate = now.toInstant(ZoneOffset.UTC);
            Instant startDate = courseInfo.getStartDate().toInstant();

            logger.info("Is current date is before start date {}", currentDate.isBefore(startDate));
            model.addAttribute("isCourseDateValid", currentDate.isBefore(startDate));
        }

        // Get Time segments for course

        List<CourseTime> courseTimeList = courseTimeRepository.searchTimeByCourseID(courseId);
        model.addAttribute("courseTimeList", courseTimeList);

        // Get syllabus
        List<Syllabus> syllabusList = courseInfo.getSyllabus();
        int syllabusSize = syllabusList.size();
        // model.addAttribute("syllabusList", syllabusList);
        model.addAttribute("syllabusSize", syllabusSize);

        // first syllabus
        try {
            if (syllabusSize > 0) {

                Syllabus firstSyllabus = syllabusList.get(0);

                // String firstContent = firstSyllabus.getContent().get(0).getContent();
                model.addAttribute("firstSyllabus", firstSyllabus);
                // model.addAttribute("firstContent", firstContent);
                syllabusList.remove(0);
            }
        } catch (Exception e) {
            e.getLocalizedMessage();
            // TODO: handle exception
            logger.info(e.toString());
        }

        model.addAttribute("syllabusList", syllabusList);

        // Get the remaining number of students who can join course
        Integer maxStudent = courseInfo.getMaxStu();
        List<UserInfo> studentList = new ArrayList<>();
        for (JoinCourseUser joinCourseUser : courseInfo.getJoin()) {
            if (joinCourseUser.getUserInfo().getUserAccount().getRole().equals(UserRole.STUDENT.getValue()))
                studentList.add(joinCourseUser.getUserInfo());
        }
        Integer stuListSize = studentList.size();
        Integer availableStuList;
        try {
            availableStuList = maxStudent - stuListSize;
        } catch (NullPointerException e) {
            e.getLocalizedMessage();
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
            logger.info("Initiate to Operation Retrieve Table: {} by query: findById={}", "user", userId);
            UserInfo user = userRepository.findById(userId).orElse(null);
            boolean studentRegistered = true;
            logger.info("Operation Retrieve Table: {} by query: findById={} Result  List : {} Success", "user", userId,
                    user.toString());

            List<JoinCourseUser> join = joinCourseService.getJoinCourseUser(userId, courseId);
            studentRegistered = join != null && !join.isEmpty();

            // payment status
            boolean paymentComplete = false;
            for (JoinCourseUser jcu : join) {
                // logging
                logger.info("the uid of joinlist is {} and session id is {}", jcu.getUserInfo().getUid(), userId);
                // logger.info("The status of joinlist of outter scope is
                // {}",jcu.getPaymentReceive().getPaymentStatus());

                // comparing two long values reference safe
                if (String.valueOf(jcu.getUserInfo().getUid()).equals(String.valueOf(userId))) {
                    // logger.info("The status of joinlist of scope id compare is
                    // {}",jcu.getPaymentReceive().getPaymentStatus());
                    if (jcu.getPaymentReceive() == null) {
                        paymentComplete = false;
                    } else if (jcu.getPaymentReceive().getPaymentStatus().equals(PaymentStatus.COMPLETE.getValue())) {
                        // logger.info("The status of joinlist of scope status compare is
                        // {}",jcu.getPaymentReceive().getPaymentStatus());
                        paymentComplete = true;
                    }
                }
            }
            model.addAttribute("paymentComplete", paymentComplete);
            logger.info("the boolean value for paymentComplete is {}", paymentComplete);

            logger.info("The boolean value for student registered is {} ", studentRegistered);
            boolean studentNotRegistered = !studentRegistered;
            model.addAttribute("studentNotRegistered", studentNotRegistered);
            model.addAttribute("studentRegistered", studentRegistered);
            model.addAttribute("student", "STUDENT");
            model.addAttribute("userId", userId);
            // Get stuReviews
            List<JoinCourseUser> joinUserList = user.getJoin();
            List<Review> stuReviews = new ArrayList<Review>();
            for (JoinCourseUser joinlist : joinUserList) {
                if (joinlist.getCourseInfo().getCourseId().equals(courseId)) {
                    stuReviews.addAll(joinlist.getReview());
                }
            }
            List<Review> studentReviewList = new ArrayList<Review>();
            for (Review studentReview : stuReviews) {
                if (studentReview.getStar() == 0) {
                    studentReviewList.add(studentReview);
                    model.addAttribute("stuReviews", studentReviewList);
                }
            }

        }

        else if (userSessionService.getRole() == UserRole.ADMIN
                || userSessionService.getRole() == UserRole.SUPER_ADMIN) {

            model.addAttribute("admin", "ADMIN");
            model.addAttribute("classlink", courseInfo.getClassLink());
            model.addAttribute("studentList", studentList);

        } else if (userSessionService.getRole() == UserRole.GUEST_USER) {
            model.addAttribute("guest", "GUEST");
        }

        if (classType.equals("VIDEO")) {
            List<VideoClassList> videoClassLists = new ArrayList<>();
            List<VideoClass> videoClass = videoClassRepository.getVideoListByCourseId(courseId);
            
            int noOfVideo = 0;
            if (!videoClass.isEmpty()) {
                for (VideoClass videolist : videoClass) {
                    String isDelete = videolist.getIs_deleted();

                    if (!isDelete.equals("true")) {
                        noOfVideo++;
                        Long id = videolist.getId();
                        Integer videoOrderNo = videolist.getVideo_order_no();
                        String thumbnail = videolist.getVideo_id();
                        String videoTitle = videolist.getVideo_title();
                        String videoDesc = videolist.getDescription();

                        videoClassLists.add(new VideoClassList(id, videoOrderNo, thumbnail, videoTitle, videoDesc));
                        model.addAttribute("videoClassLists", videoClassLists);
                        model.addAttribute("noOfVideo", noOfVideo);
                    }

                }
            } else {
                model.addAttribute("noOfVideo", noOfVideo);

            }

        }

        logger.info("Called getCourseDetails with parameter course : {} Success", courseId);
        return "CM0003_CourseDetails";
    }

    @PostMapping(value = { "/teacher/course-details/insert-class-link", "/admin/course-details/insert-class-link" })
    private String courseDetailsLink(@ModelAttribute("class-link") String classLink,
            @ModelAttribute("courseId") Long courseId, @ModelAttribute("roleLink") String roleLink, Model model) {
        logger.info("Api name is: {}. Parameter = classLink:{} courseId: {} roleLink:{}  ", "courseDetailsLink",
                classLink, courseId, roleLink);
        logger.info("Initiate to Operation Retrieve Table: {} by query: findById={} ", "courseInfo", courseId);
        CourseInfo courseInfo = courseInfoRepository.findById(courseId).get();
        logger.info("Operation Retrieve Table: {} by query: findById={} Result  List {} Success ", "courseInfo",
                courseId, courseInfo.toString());
        courseInfo.setClassLink(classLink);
        logger.info("Initiate to Operation Insert Table {} Data {}", "courseInfo", courseInfo);
        courseInfoRepository.save(courseInfo);
        logger.info("Operation Insert Table {} Data {} Success", "courseInfo", courseInfo);
        logger.info("Called courseDetailsLink with parameter classLink:{} courseId: {} roleLink:{} Success", classLink,
                courseId, roleLink);
        return "redirect:/" + roleLink + "/course-details/" + courseId;
    }

    // ### Temporarily disble to fix some changes in 'Test' entity class ###
    // @PostMapping("/admin/course-details/insert-test-link")
    // private String courseTestLink(@ModelAttribute("test-link") String testLink,
    // @ModelAttribute("courseId") Long courseId, Model model) {
    // CourseInfo courseInfo = courseInfoRepository.findById(courseId).get();
    // List<Test> testList = courseInfo.getTest();
    // logger.info("The size of test list is {}" + testList.size());
    // Test test = new Test();
    // test.setTestLink(testLink);
    // test.setCourseInfo(courseInfo);
    // testList.add(test);
    // courseInfo.setTest(testList);
    // courseInfoRepository.save(courseInfo);

    // return "redirect:/admin/course-details/" + courseId;
    // }

    // ### Temporarily disble to fix some changes in 'Test' entity class ###

    // @GetMapping("/admin/hello")
    // private String helloWorld(){
    // Long myId = (long) 1;
    // CourseInfo courseInfo = courseInfoRepository.findById(myId).get();
    // System.out.println("BlaBla" + courseInfo.getTest().size());
    // return "hello";
    // }

    // Store ongoing uploads for cancellation
    private final Map<String, CompletableFuture<File>> ongoingUploads = new ConcurrentHashMap<>();

    @PostMapping("/upload")
    public DeferredResult<ResponseEntity<Object>> uploadVideo(
            @RequestParam("file") MultipartFile multipartFile,
            @RequestParam("course_id") Long course_id,
            @RequestParam("order_no") String orderNo,
            @RequestParam("video_title") String videoTitle,
            @RequestParam("video_description") String videoDescription)
            throws IOException, GeneralSecurityException {
        DeferredResult<ResponseEntity<Object>> deferredResult = new DeferredResult<>(300000L);
        Long userID = getUid();
        logger.info("Called uploadVideo with parameter(user_id={})", userID);
        CompletableFuture<File> uploadFuture = CompletableFuture.supplyAsync(() -> {
            try {
                JSONObject formData = new JSONObject();
                formData.put("course_id", course_id);
                formData.put("order_no", orderNo);
                formData.put("video_title", videoTitle);
                formData.put("video_description", videoDescription);
                return uploadToDrive(multipartFile, formData, deferredResult);
            } catch (Exception e) {
                deferredResult.setErrorResult(e);
                return null;
            }
        });

        ongoingUploads.put(multipartFile.getOriginalFilename(), uploadFuture);

        uploadFuture.whenComplete((result, throwable) -> {
            if (throwable != null) {
                deferredResult.setErrorResult(throwable);
            } else {
                deferredResult.setResult(ResponseEntity.status(HttpStatus.OK).body(result.getId()));
            }
        });
        logger.info("Called uploadVideo with parameter(user_id={}) Success", userID);

        return deferredResult;
    }

    private final AtomicBoolean cancelRequested = new AtomicBoolean(false);

    @PostMapping("/cancelUpload")
    public ResponseEntity<String> cancelUpload() {
        Long userID = getUid();
        logger.info("Called cancelUpload with parameter(user_id={})", userID);
        cancelRequested.set(true);
        System.out.println("Itchy hand clicks the cancel button!!!");
        logger.info("Called cancelUpload with parameter(user_id={}) Success", userID);
        return ResponseEntity.ok("Upload canceled");
    }

    private File uploadToDrive(MultipartFile multipartFile, JSONObject formData,
            DeferredResult<ResponseEntity<Object>> deferredResult) throws IOException, GeneralSecurityException {

        Long courseId = formData.getLong("course_id");

        Integer orderNo = formData.getInt("order_no");
        String videoTitle = formData.getString("video_title");
        String videoDescription = formData.getString("video_description");

        CourseInfo courseinfo = courseInfoRepository.getById(courseId);

        // String parentFolderId = "15GoMoeJQqTUghpOhkniM3tfbStXi_Rhv";
        

        String parentFolderId = null;

        if (ENVIRONMENT.equals("production")) {
            parentFolderId = "1UZe-DL3103T84VZBPafzcVpq0yH5tXDv" ;

        }
        if (ENVIRONMENT.equals("testing")) {
            parentFolderId = "1jhi5bwEerrR3RsghQDAcdhVHQrAGcVqt" ;

        }
        if (ENVIRONMENT.equals("local")) {
            parentFolderId = "1kSCjOv-g-xwuT9c3X-LGAh1HN1Y4Pa4t" ;

        }

        String subfolderName = courseinfo.getCourseName() + ", " + courseId;
        String videoName = videoTitle;
        Drive service = driveService.getInstance();
        String subfolderId = getSubfolderId(service, parentFolderId, subfolderName);

        if (subfolderId == null) {

            File subfolder = new File();
            subfolder.setName(subfolderName);
            subfolder.setMimeType("application/vnd.google-apps.folder");
            subfolder.setParents(Collections.singletonList(parentFolderId));

            File createdSubfolder = service.files().create(subfolder).execute();
            subfolderId = createdSubfolder.getId();

            File filemeta = new File();

            filemeta.setParents(Collections.singletonList(subfolderId));
            filemeta.setName(videoName);

            DeferredResult<Double> progressResult = new DeferredResult<>();
            progressResult.setResult(0.0);

            File uploadedFile = null;
            try {
                uploadedFile = service.files().create(filemeta,
                        new InputStreamContent(multipartFile.getContentType(),
                                new MonitoringInputStream(multipartFile.getInputStream(), multipartFile.getSize(),
                                        (bytesRead, totalBytes) -> {
                                            if (cancelRequested.get()) {
                                                throw new CancellationException("Upload canceled");
                                            }

                                            double progress = ((double) bytesRead / totalBytes) * 100;

                                            DecimalFormat df = new DecimalFormat("#.#");
                                            progress = Double.parseDouble(df.format(progress));
                                            progressResult.setResult(progress);

                                            messagingTemplate.convertAndSend("/topic/progress", progress);
                                            System.out.println("Upload Progress: " + progress);
                                        })))
                        .setFields("id")
                        .execute();

                String fileId = uploadedFile.getId();
                Permission permission = new Permission();
                permission.setType("anyone");
                permission.setRole("reader");

                service.permissions().create(fileId, permission).execute();
                VideoClass videoClass = new VideoClass(courseId, courseinfo, fileId, videoTitle, subfolderId,
                        courseinfo.getCourseName(), orderNo, videoDescription, "false", null);
                videoClassRepository.save(videoClass);
                System.out.println("Upload Done!!!");

            } catch (CancellationException e) {

                cancelRequested.set(false);
                System.out.println("Upload canceled");

            }

            return uploadedFile;
        } else {

            File filemeta = new File();

            filemeta.setParents(Collections.singletonList(subfolderId));
            filemeta.setName(videoName);

            DeferredResult<Double> progressResult = new DeferredResult<>();
            progressResult.setResult(0.0);

            File uploadedFile = null;
            try {
                uploadedFile = service.files().create(filemeta,
                        new InputStreamContent(multipartFile.getContentType(),
                                new MonitoringInputStream(multipartFile.getInputStream(), multipartFile.getSize(),
                                        (bytesRead, totalBytes) -> {
                                            if (cancelRequested.get()) {
                                                throw new CancellationException("Upload canceled");
                                            }

                                            double progress = ((double) bytesRead / totalBytes) * 100;

                                            DecimalFormat df = new DecimalFormat("#.#");
                                            progress = Double.parseDouble(df.format(progress));
                                            progressResult.setResult(progress);

                                            messagingTemplate.convertAndSend("/topic/progress", progress);
                                            System.out.println("Upload Progress: " + progress);
                                        })))
                        .setFields("id")
                        .execute();

                String fileId = uploadedFile.getId();
                Permission permission = new Permission();
                permission.setType("anyone");
                permission.setRole("reader");

                service.permissions().create(fileId, permission).execute();
                VideoClass videoClass = new VideoClass(courseId, courseinfo, fileId, videoTitle, subfolderId,
                        courseinfo.getCourseName(), orderNo, videoDescription, "false", null);
                videoClassRepository.save(videoClass);
                System.out.println("Upload Done!!!");

            } catch (CancellationException e) {

                cancelRequested.set(false);
                System.out.println("Upload canceled");

            }

            return uploadedFile;

        }

    }

    private interface ProgressCallback {
        void updateProgress(long bytesRead, long totalBytes);
    }

    private class MonitoringInputStream extends InputStream {
        private final InputStream delegate;
        private final long totalSize;
        private final ProgressCallback callback;
        private long bytesRead = 0;

        public MonitoringInputStream(InputStream delegate, long totalSize, ProgressCallback callback) {
            this.delegate = delegate;
            this.totalSize = totalSize;
            this.callback = callback;
        }

        @Override
        public int read() throws IOException {
            int b = delegate.read();
            if (b != -1) {
                bytesRead++;
                callback.updateProgress(bytesRead, totalSize);
            }
            return b;
        }

        @Override
        public int read(byte[] b, int off, int len) throws IOException {
            int bytesRead = delegate.read(b, off, len);
            if (bytesRead != -1) {
                this.bytesRead += bytesRead;
                callback.updateProgress(this.bytesRead, totalSize);
            }
            return bytesRead;
        }

        @Override
        public void close() throws IOException {
            delegate.close();
        }
    }

    @PostMapping("/editVideo")
    public ResponseEntity<String> editVideo(@RequestBody String payload) throws IOException, GeneralSecurityException {
        Long userID = getUid();
        System.out.println("User ID" + userID);
        JSONObject jsonObject = new JSONObject(payload);
        Integer orderNumber = jsonObject.getInt("order_no");
        String videoTitle = jsonObject.getString("video_title");
        String description = jsonObject.getString("video_description");
        Long id = jsonObject.getLong("id");

        VideoClass videoClass = videoClassRepository.getById(id);
        videoClass.setVideo_order_no(orderNumber);
        videoClass.setVideo_title(videoTitle);
        videoClass.setDescription(description);
        videoClassRepository.save(videoClass);

        Drive service = driveService.getInstance();
        String videoId = videoClass.getVideo_id();
        File file = new File();
        file.setName(videoTitle);

        try {

            File updatedFile = service.files().update(videoId, file).execute();

            System.out.println("File renamed to: " + updatedFile.getName());
        } catch (Exception e) {
            e.printStackTrace();

        }

        return ResponseEntity.ok("Success.");
    }

    @PostMapping("/delete-video")
    public ResponseEntity<String> deleteVideo(@RequestBody String payload)
            throws IOException, GeneralSecurityException {
        JSONObject jsonObject = new JSONObject(payload);

        Long id = jsonObject.getLong("id");

        VideoClass videoClass = videoClassRepository.getById(id);
        String videoId = videoClass.getVideo_id();

        Drive service = driveService.getInstance();

        try {

            File videoFile = service.files().get(videoId).execute();
            service.files().delete(videoFile.getId()).execute();
            System.out.println("Video deleted successfully.");

            // videoClassRepository.deleteById(id);
            LocalDateTime currentDateTime = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String deletedAt = currentDateTime.format(formatter);
            videoClass.setIs_deleted("true");
            videoClass.setDeleted_At(deletedAt);
            videoClassRepository.save(videoClass);

        } catch (IOException e) {
            e.printStackTrace();
            LocalDateTime currentDateTime = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String deletedAt = currentDateTime.format(formatter);
            videoClass.setIs_deleted("true");
            videoClass.setDeleted_At(deletedAt);
            videoClassRepository.save(videoClass);

        }

        return ResponseEntity.ok("Success.");
    }

    @GetMapping("/video-class/{courseId}/{videoId}")
    public String viewVideo(@PathVariable Long courseId, @PathVariable Long videoId, Model model) {
        String nav_type = "fragments/adminnav";
        List<VideoClassList> videoClassLists = new ArrayList<>();
        List<VideoClass> videoClass = videoClassRepository.getVideoListByCourseId(courseId);
        VideoClass video = videoClassRepository.getById(videoId);
        Long userID = getUid();
        UserInfo currentUser = userInfoRepository.findStudentById(userID);
        String userRole = currentUser.getUserAccount().getRole();

        if (userRole.equals("ROLE_STUDENT")) {
            CourseInfo courseInfo = courseInfoRepository.findByCourseID(courseId);
            JoinCourseUser joinCourseUser = joinCourseUserRepository.findByUserInfoAndCourseInfo(currentUser,
                    courseInfo);
            if (joinCourseUser != null) {
                Long joinID = joinCourseUser.getJoinId();
                PaymentReceive paymentReceive = paymentRepository.findByJoin(joinID);
                if (paymentReceive != null) {
                    String paymentStatus = paymentReceive.getPaymentStatus();
                    if (paymentStatus.equals("COMPLETED")) {
                        String vlink = video.getVideo_id();
                        String vTitle = video.getVideo_title();
                        String vDesc = video.getDescription();

                        for (VideoClass videolist : videoClass) {
                            if (videolist != null) {

                                String isDelete = videolist.getIs_deleted();

                                if (!isDelete.equals("true")) {

                                    Long id = videolist.getId();
                                    Integer videoOrderNo = videolist.getVideo_order_no();
                                    String thumbnail = videolist.getVideo_id();
                                    String videoTitle = videolist.getVideo_title();
                                    String videoDesc = videolist.getDescription();

                                    videoClassLists
                                            .add(new VideoClassList(id, videoOrderNo, thumbnail, videoTitle,
                                                    videoDesc));
                                    model.addAttribute("videoClassLists", videoClassLists);
                                }

                            }

                        }

                        model.addAttribute("v_link", vlink);
                        model.addAttribute("v_Title", vTitle);
                        model.addAttribute("v_Desc", vDesc);
                        model.addAttribute("course_Id", courseId);

                    } else {
                        String classAnnounce = "Please contact administrator for payment process. Sorry for any inconvenience caused.";
                        model.addAttribute("class_announce", classAnnounce);
                        model.addAttribute("course_Id", courseId);

                    }
                } else if (paymentReceive == null) {
                    String classAnnounce = "Please contact administrator for payment process. Sorry for any inconvenience caused.";
                    model.addAttribute("class_announce", classAnnounce);
                    model.addAttribute("course_Id", courseId);
                }
            }

            else {
                String classAnnounce = "Please enroll to the video class and complete payment process first. Sorry for any inconvenience caused.";
                model.addAttribute("class_announce", classAnnounce);
                model.addAttribute("course_Id", courseId);
            }
        } else if (userRole.equals("ROLE_ADMIN") || userRole.equals("ROLE_SUPER_ADMIN")) {

            String vlink = video.getVideo_id();
            String vTitle = video.getVideo_title();
            String vDesc = video.getDescription();

            for (VideoClass videolist : videoClass) {
                if (videolist != null) {

                    String isDelete = videolist.getIs_deleted();

                    if (!isDelete.equals("true")) {

                        Long id = videolist.getId();
                        Integer videoOrderNo = videolist.getVideo_order_no();
                        String thumbnail = videolist.getVideo_id();
                        String videoTitle = videolist.getVideo_title();
                        String videoDesc = videolist.getDescription();

                        videoClassLists.add(new VideoClassList(id, videoOrderNo, thumbnail, videoTitle, videoDesc));
                        model.addAttribute("videoClassLists", videoClassLists);
                    }

                }

            }

            model.addAttribute("v_link", vlink);
            model.addAttribute("v_Title", vTitle);
            model.addAttribute("v_Desc", vDesc);
            model.addAttribute("course_Id", courseId);

        } else if (userRole.equals("ROLE_TEACHER")) {
            CourseInfo courseInfo = courseInfoRepository.findByCourseIDUID(courseId, userID);
            if (courseInfo != null) {
                String vlink = video.getVideo_id();
                String vTitle = video.getVideo_title();
                String vDesc = video.getDescription();

                for (VideoClass videolist : videoClass) {
                    if (videolist != null) {

                        String isDelete = videolist.getIs_deleted();

                        if (!isDelete.equals("true")) {

                            Long id = videolist.getId();
                            Integer videoOrderNo = videolist.getVideo_order_no();
                            String thumbnail = videolist.getVideo_id();
                            String videoTitle = videolist.getVideo_title();
                            String videoDesc = videolist.getDescription();

                            videoClassLists.add(new VideoClassList(id, videoOrderNo, thumbnail, videoTitle, videoDesc));
                            model.addAttribute("videoClassLists", videoClassLists);
                        }

                    }

                }

                model.addAttribute("v_link", vlink);
                model.addAttribute("v_Title", vTitle);
                model.addAttribute("v_Desc", vDesc);
                model.addAttribute("course_Id", courseId);

            } else if (courseInfo == null) {
                String classAnnounce = "Teachers are allowed only to view of their video course.";
                model.addAttribute("class_announce", classAnnounce);
                model.addAttribute("course_Id", courseId);
            }
        }

        model.addAttribute("nav_type", nav_type);
        return "CM0012_Videoplaylist";
    }

    private String getSubfolderId(Drive service, String parentFolderId, String subfolderName) throws IOException {
        String query = String.format(
                "'%s' in parents and mimeType = 'application/vnd.google-apps.folder' and name = '%s'",
                parentFolderId, subfolderName);

        FileList result = service.files().list()
                .setQ(query)
                .setFields("files(id)")
                .execute();

        if (result.getFiles() != null && !result.getFiles().isEmpty()) {
            return result.getFiles().get(0).getId();
        }

        return null;
    }

    @DeleteMapping("/admin/course-details/delete/")
    public ResponseEntity<Object> deleteCourse(
            Model model,
            Long courseId,
            HttpServletRequest httpServletRequest) {
        logger.info("Api name is: {}. Parameter is courseId: {} ", "deleteCourse", courseId);
        logger.info("DELETE Request for course {}", courseId);
        try {
            // UserInfo userInfo = userService.getUserInfoByID(uid);
            logger.info("Initiate to Operation Retrieve Table: {} by query: findById={} ", "courseInfo", courseId);
            CourseInfo courseInfo = courseInfoRepository.findById(courseId).get();
            if (courseInfo == null) {
                throw new CourseNotFoundException();
            }
            courseService.deleteCourseInfo(courseInfo);
            logger.info("Operation Delete Table {} by {} = {} Success", "courseInfo", courseId, courseService);

        } catch (CourseNotFoundException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Course Not Found");
        } catch (ObjectOptimisticLockingFailureException e) {
            e.printStackTrace();

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something went wrong");
        }
        logger.info(
                "Called deleteCourse [ResponseEntity.status(HttpStatus.OK).body(\"operation success\"] with parameter courseId:{} Success",
                courseId);
        return ResponseEntity.status(HttpStatus.OK).body("operation success");

    }

    @PostMapping("/admin/course-details/verify/")
    public ResponseEntity<Object> verifyCourse(
            Model model,
            Long courseId,
            HttpServletRequest httpServletRequest) {
        logger.info("VERIFY Request for course {}", courseId);
        try {
            // UserInfo userInfo = userService.getUserInfoByID(uid);
            logger.info("Initiate to Operation Retrieve Table: {} by query: findById={} ", "courseInfo", courseId);
            CourseInfo courseInfo = courseInfoRepository.findById(courseId).get();

            if (courseInfo == null) {
                throw new CourseNotFoundException();
            }
            courseService.verifyCourseInfo(courseInfo);
        } catch (CourseNotFoundException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Course Not Found");
        } catch (ObjectOptimisticLockingFailureException e) {
            e.printStackTrace();

        } catch (Exception e) {
            e.getLocalizedMessage();
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something went wrong");
        }
        logger.info("Called verifyCourse with parameter courseId:{} Success", courseId);
        return ResponseEntity.status(HttpStatus.OK).body("operation success");

    }

    @RequestMapping("/student/enroll/{courseId}/{userId}")

    public String enrollStudent(HttpServletRequest request, @PathVariable Long courseId, @PathVariable Long userId,
            Model model) {
        logger.info("Api name is: {}. Parameter is courseId: {}, userId : {} ", "enrollStudent", courseId, userId);
        logger.info("Request");
        JoinCourseDTO joinCourseDTO = new JoinCourseDTO();
        joinCourseDTO.setUid(userId);
        joinCourseDTO.setCourseId(courseId);
        try {

            if (joinCourseService.enrollStudent(joinCourseDTO) == null) {
                logger.info("null user");
                return "redirect:/student/course-details/" + courseId + "/?error";
            }
        } catch (NoSuchElementException e) {
            e.getLocalizedMessage();
            e.printStackTrace();
            return "redirect:/student/course-details/" + courseId + "/?error";
        } catch (Exception e) {
            e.getLocalizedMessage();
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "redirect:/student/course-details/" + courseId + "/?error";
        }

        logger.info("Initiate to Operation Retrieve Table: {} by query: findById={} ", "courseInfo", courseId);
        CourseInfo courseInfo = courseInfoRepository.findById(courseId).get();
        logger.info("Operation Retrieve Table: {} by query: findById={} Result  List {} Success ", "courseInfo",
                courseId, courseInfo.toString());

        List<UserInfo> studentList = new ArrayList<>();
        for (JoinCourseUser joinCourseUser : courseInfo.getJoin()) {
            if (joinCourseUser.getUserInfo().getUserAccount().getRole().equals(UserRole.STUDENT.getValue()))
                studentList.add(joinCourseUser.getUserInfo());
        }

        Integer stuListSize = studentList.size();
        System.out.println("Student size is : " + stuListSize);

        List<PaymentForTeacher> paymentList = paymentForTeacherService.getPaymentForTeacherByCourseId(courseId);

        for (PaymentForTeacher payment : paymentList) {

            if (payment.getCourseInfo().getStartDate() == null && payment.getStatus().equals("PENDING")) {
                int studentFromDatabase = payment.getNoOfEnrollPerson();
                int noOfStudent = studentFromDatabase + 1;

                LocalDate enrollDate = LocalDate.now();

                LocalDate firstDayOfMonth = enrollDate.withDayOfMonth(1);
                Date firstDay = Date.from(firstDayOfMonth.atStartOfDay(ZoneId.systemDefault()).toInstant());

                LocalDate lastDayOfMonth = enrollDate.withDayOfMonth(enrollDate.lengthOfMonth());
                Date lastDay = Date.from(lastDayOfMonth.atStartOfDay(ZoneId.systemDefault()).toInstant());

                LocalDate paymentDate = enrollDate.plusMonths(1).withDayOfMonth(5);
                if (paymentDate.getDayOfWeek() == DayOfWeek.SATURDAY) {
                    paymentDate = paymentDate.plusDays(2);
                } else if (paymentDate.getDayOfWeek() == DayOfWeek.SUNDAY) {
                    paymentDate = paymentDate.plusDays(1);
                }

                double courseFee = payment.getCourseInfo().getFees();
                double totalAmount = courseFee * noOfStudent;
                double totalAmountTenPercent = totalAmount * 0.90;

                payment.setNoOfEnrollPerson(noOfStudent);
                payment.setPaymentDate(paymentDate);
                payment.setCalculateDateFrom(firstDay);
                payment.setCalculateDateTo(lastDay);
                payment.setPaymentAmount(totalAmount);
                payment.setPaymentAmountPercentage(totalAmountTenPercent);
                paymentForTeacherService.savePaymentForTeacher(payment);
                logger.info("Operation Update Table : {} = Data : {} Success", "PaymentForTeacher", payment);

            } else if (payment.getCourseInfo().getStartDate() == null && payment.getStatus().equals("COMPLETE")) {

                PaymentForTeacher paymentForTeacher = new PaymentForTeacher();
                paymentForTeacher.setCourseInfo(courseInfo);
                paymentForTeacher.setNoOfEnrollPerson(1);

                LocalDate enrollDate = LocalDate.now();

                LocalDate firstDayOfMonth = enrollDate.withDayOfMonth(1);
                Date firstDay = Date.from(firstDayOfMonth.atStartOfDay(ZoneId.systemDefault()).toInstant());

                LocalDate lastDayOfMonth = enrollDate.withDayOfMonth(enrollDate.lengthOfMonth());
                Date lastDay = Date.from(lastDayOfMonth.atStartOfDay(ZoneId.systemDefault()).toInstant());

                LocalDate paymentDate = enrollDate.plusMonths(1).withDayOfMonth(5);
                if (paymentDate.getDayOfWeek() == DayOfWeek.SATURDAY) {
                    paymentDate = paymentDate.plusDays(2);
                } else if (paymentDate.getDayOfWeek() == DayOfWeek.SUNDAY) {
                    paymentDate = paymentDate.plusDays(1);
                }

                double courseFee = paymentForTeacher.getCourseInfo().getFees();
                double totalAmount = courseFee * 1;
                double totalAmountTenPercent = totalAmount * 0.90;
                String paymentStatus = "PENDING";
                paymentForTeacher.setCalculateDateFrom(firstDay);
                paymentForTeacher.setCalculateDateTo(lastDay);
                paymentForTeacher.setCourseFee(courseFee);
                paymentForTeacher.setPaymentDate(paymentDate);
                paymentForTeacher.setPaymentAmount(totalAmount);
                paymentForTeacher.setPaymentAmountPercentage(totalAmountTenPercent);
                paymentForTeacher.setStatus(paymentStatus);
                paymentForTeacher.setPaymentVerify(false);
                paymentForTeacherService.savePaymentForTeacher(paymentForTeacher);
                logger.info("Operation Update Table : {} = Data : {} Success", "PaymentForTeacher", payment);

            }

            else if (payment.getCalculateDateFrom() != null && payment.getStatus().equals("PENDING")) {
                int noOfStudent = stuListSize;
                double courseFee = payment.getCourseInfo().getFees();
                double totalAmount = courseFee * noOfStudent;
                double totalAmountTenPercent = totalAmount * 0.90;
                payment.setNoOfEnrollPerson(noOfStudent);
                payment.setPaymentAmount(totalAmount);
                payment.setPaymentAmountPercentage(totalAmountTenPercent);
                paymentForTeacherService.savePaymentForTeacher(payment);
                logger.info("Operation Update Table : {} = Data : {} Success", "PaymentForTeacher", payment);

            }

        }

        new Thread(new Runnable() {
            public void run() {
                try {
                    UserInfo userInfo = userService.getUserInfoByID(userId);
                    logger.info("Initiate to Operation Retrieve Table: {} by query: findById={} ", "courseInfo",
                            courseId);
                    CourseInfo courseInfo = courseInfoRepository.findById(courseId).get();
                    logger.info("Operation Retrieve Table: {} by query: findById={} Result  List {} Success ",
                            "courseInfo", courseId, courseInfo.toString());

                    String appUrl = request.getServerName() + // "localhost"
                            ":" +
                            request.getServerPort(); // "8080"
                    // mailService.sendVerificationMail(
                    // savedUserInfo.getUserAccount(),
                    // appUrl);

                    mailService.SendAdminNewStudentEnroll(userInfo, courseId, courseInfo);
                    mailService.SendStudentEnrollCourse(userInfo, courseInfo);
                    mailService.SendTeacherNewStudentEnroll(userInfo, courseInfo);

                } catch (Exception e) {
                    e.getLocalizedMessage();
                    logger.info(e.toString());
                }
            }
        }).start();

        logger.info("Called getCourseDetails[redirect:/student/course-details/] with parameter course : {} Success",
                courseId);
        logger.info("Called getCourseDetails[redirect:/student/course-details/] with parameter course : {} Success",
                courseId);
        return "redirect:/student/course-details/" + courseId;
    }

    private Long getUid() {
        Long uid = userSessionService.getUserAccount().getAccountId();
        return uid;
    }

}