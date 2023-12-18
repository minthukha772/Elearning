package com.blissstock.mappingSite.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import com.blissstock.mappingSite.entity.Contact;
import com.blissstock.mappingSite.entity.UserAccount;
import com.blissstock.mappingSite.repository.ContactRepository;
import com.blissstock.mappingSite.repository.UserAccountRepository;
import com.blissstock.mappingSite.service.MailService;
import com.blissstock.mappingSite.service.UserSessionService;

@Controller
public class HelpAndSupportController {

    @Autowired
    UserSessionService userSessionService;

    @Autowired
    ContactRepository contactRepository;

    @Autowired
    UserAccountRepository userAccountRepository;

    @Autowired
    MailService mailService;

    private static final Logger logger = LoggerFactory.getLogger(HelpAndSupportController.class);

    @GetMapping("/guest/frequently_asked_questions")
    private String faqs() {

        logger.info("GET request");

        return "frequently_asked_questions";
    }

    @GetMapping("/guest/help_and_support")
    private String HelpSupport() {

        logger.info("GET request");

        return "help_and_support";
    }

    @GetMapping("/guest/contact")
    private String contact() {
        logger.info("Called contact with parameter: {} Success");
        return "CM0011_InquiryForm";
    }

    @GetMapping("/guest/confirmInquiry")
    private String confirmInquiry() {
        logger.info("Called confirmInquiry with parameter: {} Success");
        return "InquiryConfirmation";
    }

    @PostMapping("/guest/sendInquiryFormEmail")
    public ResponseEntity<String> sendEmailToAdmin(@RequestBody String data) {
        logger.info("Called sendEmailToAdmin with parameter: {}", "data");
        JSONObject jsonObject = new JSONObject(data);
        String name = jsonObject.getString("name");
        String email = jsonObject.getString("email");
        String phNo = jsonObject.getString("phNo");
        String detail = jsonObject.getString("detail");
        String description = jsonObject.getString("description");

        logger.info("Initiate to Operation Retrieve Table {} by query {}",
                "user_account",
                "userAccountRepository.findByRole(\"ROLE_ADMIN\")");
        List<UserAccount> adminUsers = userAccountRepository.findByRole("ROLE_ADMIN");
        logger.info("Operation Retrieve Table {} by query {} Result List {} Success",
                "user_account",
                "userAccountRepository.findByRole(\"ROLE_ADMIN\")",
                adminUsers);

        if (adminUsers != null) {
            // sent inquiry form notify email to all admins
            for (UserAccount adminUser : adminUsers) {
                try {
                    mailService.sendEmailToAdmin(
                            adminUser.getMail(),
                            name,
                            email,
                            phNo,
                            detail,
                            description);
                    logger.info("Inquiry form email sent to admin {} Success", adminUser.getMail());

                } catch (MessagingException e) {
                    e.printStackTrace();
                    logger.warn("Inquiry form email sent to admin {} Failed", adminUser.getMail());
                }
            }

            // sent inquiry form notify email to asker
            try {
                mailService.sendEmailToAsker(
                        name,
                        email,
                        phNo,
                        detail,
                        description);
                logger.info("Inquiry form email sent to asker {} Success", email);

            } catch (Exception e) {
                e.printStackTrace();
                logger.warn("Inquiry form email sent to asker {} Failed", email);
            }

            logger.info("Called sendEmailToAdmin with parameter: {} Success", "data");
            return ResponseEntity.ok("Emails sent successfully");
            
        } else {
            // Return an error response if no admin users found
            logger.error("Called sendEmailToAdmin with parameter: {} Failed", "data");
            logger.error("No admin users found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No admin users found");
        }
    }

    @GetMapping("/guest/SuccessfulInquiry")
    private String successfulInquiry() {
        logger.info("Called successfulInquiry with parameter: {} Success");
        return "InquirySuccessful";
    }

    private Long getUid() {
        Long uid = userSessionService.getUserAccount().getAccountId();
        return uid;
    }

}
