package com.blissstock.mappingSite.controller;

// import java.sql.Date;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;

import com.blissstock.mappingSite.entity.CourseInfo;
<<<<<<< HEAD
import com.blissstock.mappingSite.model.PaymentLists;
import com.blissstock.mappingSite.entity.PaymentReceive;
import com.blissstock.mappingSite.entity.UserInfo;
import com.blissstock.mappingSite.entity.JoinCourseUser;
import com.blissstock.mappingSite.repository.PaymentRepository;
import com.blissstock.mappingSite.repository.JoinCourseUserRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
=======
import com.blissstock.mappingSite.entity.PaymentLists;
import com.blissstock.mappingSite.entity.PaymentReceive;
import com.blissstock.mappingSite.entity.UserInfo;
import com.blissstock.mappingSite.repository.PaymentRepository;
>>>>>>> de9d4e27d27b5edff44f1328c1783237d3565af7

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
<<<<<<< HEAD

    private static final Logger logger = LoggerFactory.getLogger(PaymentListController.class);
=======
>>>>>>> de9d4e27d27b5edff44f1328c1783237d3565af7
    
    @Autowired
    PaymentRepository paymentRepo;
    
<<<<<<< HEAD
    @Autowired
    JoinCourseUserRepository joinCourseUserRepo;
    
=======
>>>>>>> de9d4e27d27b5edff44f1328c1783237d3565af7
    
    @RequestMapping("/PaymentList")
    public String PaymentList(Model model)
    {
        List<PaymentReceive> viewPayment = paymentRepo.findAll();
<<<<<<< HEAD

        logger.info("Payment Receive List {}",viewPayment);

=======
>>>>>>> de9d4e27d27b5edff44f1328c1783237d3565af7
        List<PaymentLists> payUserList = new ArrayList<>();
        // List<AllPaymentLists> allPayment = new AllPaymentLists();
        for(PaymentReceive paymentReceive:viewPayment)
        {
            Date paymentDate = paymentReceive.getPaymentReceiveDate();
            String paymentStatus = paymentReceive.getPaymentStatus();
<<<<<<< HEAD

            JoinCourseUser joinCourseUser = paymentReceive.getJoin();

            UserInfo payUserInfo = joinCourseUser.getUserInfo();
            String userName = payUserInfo.getUserName();
            CourseInfo payCouresInfo = joinCourseUser.getCourseInfo();
=======
            UserInfo payUserInfo = paymentReceive.getUserInfo();
            String userName = payUserInfo.getUserName();
            CourseInfo payCouresInfo = paymentReceive.getCourseInfo();
>>>>>>> de9d4e27d27b5edff44f1328c1783237d3565af7
            String courseName = payCouresInfo.getCourseName();
            int courseFees = payCouresInfo.getFees();
            payUserList.add(new PaymentLists(paymentDate, paymentStatus, userName, courseName, courseFees));
        }
<<<<<<< HEAD

        logger.info("Payment Receive List including user's information {}",payUserList);

=======
>>>>>>> de9d4e27d27b5edff44f1328c1783237d3565af7
        // System.out.println("All Payments"+payUserList);
        model.addAttribute("allPaymentList",payUserList);
        return "AD0003_PaymentListScreen";
    }
}
