package com.blissstock.mappingSite.controller;

// import java.sql.Date;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.blissstock.mappingSite.entity.CourseInfo;
import com.blissstock.mappingSite.model.PaymentLists;
import com.blissstock.mappingSite.entity.PaymentReceive;
import com.blissstock.mappingSite.entity.UserInfo;
import com.blissstock.mappingSite.entity.JoinCourseUser;
import com.blissstock.mappingSite.repository.PaymentRepository;
import com.blissstock.mappingSite.repository.JoinCourseUserRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;

// import com.blissstock.mappingSite.entity.PaymentReceive;
// import com.blissstock.mappingSite.repository.PaymentRepository;

// import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
// import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

// import java.util.List;

@Controller
public class PaymentListController {

    private static final Logger logger = LoggerFactory.getLogger(PaymentListController.class);
    
    @Autowired
    PaymentRepository paymentRepo;
    
    @Autowired
    JoinCourseUserRepository joinCourseUserRepo;
    
    
    @RequestMapping("/admin/payment-list")
    public String PaymentList(Model model)
    {
        List<PaymentReceive> viewPayment = paymentRepo.findAll();

        logger.info("Payment Receive List {}",viewPayment);

        // List<String> breadcrumbList = new ArrayList<>();
        //     breadcrumbList.add("Top");
        //     breadcrumbList.add("Payment List");
        //     model.addAttribute("breadcrumbList",breadcrumbList);
            String nav_type = "fragments/adminnav";
            model.addAttribute("nav_type",nav_type);

        List<PaymentLists> payUserList = new ArrayList<>();
        SimpleDateFormat format  = new SimpleDateFormat("yyyy-MM-dd");
        // List<AllPaymentLists> allPayment = new AllPaymentLists();
        for(PaymentReceive paymentReceive:viewPayment)
        {
            String paymentDate = format.format(paymentReceive.getPaymentReceiveDate());
            String paymentStatus = paymentReceive.getPaymentStatus();

            JoinCourseUser joinCourseUser = paymentReceive.getJoin();

            UserInfo payUserInfo = joinCourseUser.getUserInfo();
            String userName = payUserInfo.getUserName();
            Long userId = payUserInfo.getUid();
            CourseInfo payCouresInfo = joinCourseUser.getCourseInfo();
            String courseName = payCouresInfo.getCourseName();
            String courseStartDate;
            String courseEndDate;
            try {
                courseStartDate = format.format(payCouresInfo.getStartDate());
                courseEndDate = format.format(payCouresInfo.getEndDate());
            } catch (Exception e) {
                courseStartDate = format.format(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
                courseEndDate = format.format(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
            }
            String teacherName = payCouresInfo.getUserInfo().getUserName();
            Long courseId = payCouresInfo.getCourseId();
            int courseFees = payCouresInfo.getFees();
            payUserList.add(new PaymentLists(paymentDate, courseStartDate, courseEndDate, paymentStatus, userName, teacherName, courseName, courseFees,userId,courseId));
        }

        logger.info("Payment Receive List including user's information {}",payUserList);

        // System.out.println("All Payments"+payUserList);
        model.addAttribute("allPaymentList",payUserList);
        
        return "AD0003_PaymentListScreen";
    }
}