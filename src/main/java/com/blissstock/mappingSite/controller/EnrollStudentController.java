package com.blissstock.mappingSite.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.blissstock.mappingSite.dto.JoinCourseDTO;
import com.blissstock.mappingSite.dto.UserRegisterDTO;
import com.blissstock.mappingSite.entity.CourseInfo;
import com.blissstock.mappingSite.entity.JoinCourseUser;
import com.blissstock.mappingSite.entity.PaymentForTeacher;
import com.blissstock.mappingSite.entity.UserInfo;
import com.blissstock.mappingSite.enums.UserRole;
import com.blissstock.mappingSite.repository.CourseInfoRepository;
import com.blissstock.mappingSite.repository.UserInfoRepository;
import com.blissstock.mappingSite.service.JoinCourseService;
import com.blissstock.mappingSite.service.PaymentForTeacherService;
import com.blissstock.mappingSite.service.UserSessionService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class EnrollStudentController {

    private static final Logger logger = LoggerFactory.getLogger(
            EnrollStudentController.class);

    @Autowired
    UserSessionService userSessionService;

    @Autowired
    private UserInfoRepository userInfoRepository;
    @Autowired
    private JoinCourseService joinCourseService;

    @Autowired
    private CourseInfoRepository courseRepo;

    @Autowired
    private PaymentForTeacherService paymentForTeacherService;

    // @Autowired
    // private CourseTimeRepository courseTimeRepo;

    @GetMapping(value = { "/admin/enrollStudent/course/{id}/{status}", "/admin/enrollStudent/course/{id}" })
    private String enrollStudent(@PathVariable(name = "id", required = true) Long id,
            @PathVariable(name = "status", required = false) Optional<String> status, Model model) {
        logger.info("Called AD0002_EnrollStudent with parameter(course_id:{}, status:{})", id, status);
        UserRole userRole = userSessionService.getRole();

        if (userRole.equals(UserRole.SUPER_ADMIN) || userRole.equals(UserRole.ADMIN)) {
            if (id != null) {
                try {
                    Long userID = getUid();
                    logger.info("user_id: {}, role: {}", userID, userRole);
                    logger.info("Initiate to Operation Retrieve Table course_info by Query: course_id={}", id);

                    // http://localhost:8080/admin/enrollStudent/course/50001
                    Optional<CourseInfo> courseInfo = courseRepo.findById(id);
                    if (courseInfo.isPresent()) {
                        CourseInfo course = courseInfo.get();
                        logger.info(
                                "Operation Retrieve Table course_info by Query: course_id={} Result: course={} | Success",
                                id, course);
                        model.addAttribute("courseName", course.getCourseName());
                        // todo find teacher name
                        // course.getUserInfo();
                        model.addAttribute("teachername", "Teacher");
                        model.addAttribute("level", course.getLevel());
                        model.addAttribute("CourseType", course.getClassType());

                        logger.info("Initiate to Operation Retrieve Table user_info by Query: course_id={}", id);
                        List<UserInfo> userInfo = userInfoRepository.findStudentsToEnroll(id);

                        // System.out.println(userInfo.get(0).getUserName());
                        // //System.out.println(userInfo.toString());

                        List<UserRegisterDTO> userRegisterDTO = UserRegisterDTO.fromUserInfoList(userInfo);
                        logger.info(
                                "Operation Retrieve Table user_info by Query: course_id={} Result: student count={} | Success",
                                id, userRegisterDTO.size());
                        // System.out.println(userRegisterDTO.toString());
                        model.addAttribute("isCourseFound", "true");
                        model.addAttribute("students", userRegisterDTO);

                    } else {
                        logger.warn("Operation Retrieve Table user_info by Query: course_id{} Result: No Data");
                        model.addAttribute("isCourseFound", "false");
                        model.addAttribute("status", "error");
                        model.addAttribute("errorMsg", "Course with id : " + id + " is not found. ");

                    }
                } catch (Exception e) {
                    logger.info(e.getLocalizedMessage());
                }

            }
            if (status.isPresent()) {
                String state = status.get();
                if (state.equals("UserExists")) {
                    model.addAttribute("status", "error");
                    model.addAttribute("errorMsg", "User has already been enrolled");
                }
                if (state.equals("success")) {
                    model.addAttribute("status", "success");
                    model.addAttribute("Msg", "User has been enrolled Successfully");
                }
            }
            logger.info("Called AD0002_EnrollStudent with parameter(course_id:{}, status:{}) | Success", id, status);

            return "AD0002_EnrollStudent";
        } else {
            logger.error("Error: 404");
            return "redirect:/error/404";
        }

    }

    @GetMapping(value = { "/admin/enrollStudent/course/{cid}/enroll/{uid}",
            "/admin/enrollStudent/course/{cid}/success/enroll/{uid}",
            "/admin/enrollStudent/course/{cid}/UserExists/enroll/{uid}" })
    public String enorllStudent(Model model, @PathVariable(name = "cid", required = false) Long cid,
            @PathVariable(name = "uid", required = false) Long uid) {
        logger.info("Redirect AD0002_EnrollStudent with parameter(course_id:{}, user_id:{})", cid, uid);
        Long userID = getUid();
        // System.out.println("uid and cid is :" + uid + " " + cid);

        UserRole userRole = userSessionService.getRole();
        logger.info("user_id: {}, role: {}", userID, userRole);

        logger.info("Initiate to Operation Insert Table join_course_user by Query: user_id={}, course_id={}", uid, cid);
        // admin enroll course limit
        CourseInfo courseInfo = courseRepo.findById(cid).get();
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
            logger.warn(e.getLocalizedMessage());
            availableStuList = 0;
        }

        // TODO
        if (availableStuList <= 0) {
            logger.error("Available student is less than 0");
            return "redirect:/error/404";
        }

        if (userRole.equals(UserRole.SUPER_ADMIN) || userRole.equals(UserRole.ADMIN)) {
            JoinCourseDTO joinCourseDTO = new JoinCourseDTO();
            joinCourseDTO.setUid(uid);
            joinCourseDTO.setCourseId(cid);

            try {
                joinCourseService.enrollStudent(joinCourseDTO);
                logger.info(
                        "Operation Insert Table join_course_user by Query: user_id={}, course_id={} | Success",
                        uid, cid);
            } catch (Exception e) {
                logger.error(e.getLocalizedMessage());
                logger.warn("User already exist");
                return "redirect:/admin/enrollStudent/course/" + cid + "/UserExists";
            }
            logger.info("Initiate to Operation Update Table payment_for_teacher by Query: course_id={}", cid);
            List<PaymentForTeacher> paymentList = paymentForTeacherService.getPaymentForTeacherByCourseId(cid);

            for (PaymentForTeacher payment : paymentList) {

                // perform necessary updates on payment entity
                if (payment.getCalculateDateFrom() != null && payment.getStatus().equals("PENDING")) {
                    int noOfStudent = stuListSize + 1;
                    double courseFee = payment.getCourseInfo().getFees();
                    double totalAmount = courseFee * noOfStudent;
                    double totalAmountTenPercent = totalAmount * 0.90;

                    payment.setNoOfEnrollPerson(noOfStudent);
                    payment.setPaymentAmount(totalAmount);
                    payment.setPaymentAmountPercentage(totalAmountTenPercent);
                    paymentForTeacherService.savePaymentForTeacher(payment);

                } else {
                    System.out.println("Payment Update not needed.");
                }

            }
            logger.info("Operation to Update Table payment_for_teacher by Query: course_id={} | Success", cid);

            logger.info("Redirect AD0002_EnrollStudent with parameter(course_id:{}, user_id:{}) | Success", cid, uid);

            return "redirect:/admin/enrollStudent/course/" + cid + "/success";

        } else {
            logger.error("The role of user not supported");
            return "redirect:/error/404";
        }
    }

    private Long getUid() {
        Long uid = userSessionService.getUserAccount().getAccountId();
        return uid;
    }

}
