package com.blissstock.mappingSite.controller;

import java.text.ParseException;
import java.security.SecureRandom;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.ui.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.blissstock.mappingSite.dto.LoginDTO;
import com.blissstock.mappingSite.entity.GuestUser;
import com.blissstock.mappingSite.repository.GuestUserRepository;
import com.blissstock.mappingSite.service.MailServiceImpl;

@Controller

public class GuestController {

    private static Logger logger = LoggerFactory.getLogger(
            GuestController.class);

    @Autowired
    GuestUserRepository guestUserRepository;

    @Autowired
    MailServiceImpl mailService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping(value = "/guest-exam/{emailAddress}_{examID}")
    public String getGuestUserLoginPage(Model model, String guestMessage,
            @PathVariable String emailAddress, @PathVariable String examID)
            throws ParseException {

        try {

            logger.info("Called getGuestUserLoginPage with parameter(guestEmail={}) ,(GuestExamID={})", emailAddress,
                    examID);

            logger.info("Called getGuestUserLoginPage with parameter(user_id={}) Success");
            guestMessage = "* If you are Guest user, please login with the one time password that the admin is sent to you by email in advance";

            model.addAttribute("guestMessage", guestMessage);
            model.addAttribute("userInfo", new LoginDTO());
            logger.info("Called getGuestUserLoginPage with parameter(guestEmail={}) ,(GuestExamID={}) | Success",
                    emailAddress, examID);
            return "CM0005_login.html";

        } catch (Exception e) {
            logger.error(e.getLocalizedMessage());
            return "500";
        }
    }

    @GetMapping(value = "/check_email/reset_onetime_password")

    public String resetOnetimePassword(Model model, String status, String paragraph, String header3, String header5,
            @RequestParam String email, @RequestParam String examID) {

        logger.info("Called resetOnetimePassword with parameter(guestEmail={}) ,(GuestExamID={})", email, examID);
        GuestUser guestUser = guestUserRepository.getGuestUserbyEmail(email);

        String loginlink = "/guest-exam/" + email + "_" + examID;

        if (guestUser != null) {
            String guestUserName = guestUser.getName();
            String guestUserPhoneNumber = guestUser.getPhone_no();
            String oneTimePassword = generateRandomText(12);

            LocalDateTime currentDateTime = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String passwordUpdatedDateTime = currentDateTime.format(formatter);

            status = "success";
            header3 = "One-time Password renewed !";
            paragraph = "Your guest account's one-time password has been renewed successfully. New one-time password has been sent to your email. Please kindly check in your email and login with new password to continue.";

            guestUser.setOne_time_password(passwordEncoder.encode(oneTimePassword));
            guestUser.setPassword_update_date_time(passwordUpdatedDateTime);

            logger.info("Initiate to Operation Insert Table guest Data {}", guestUser.display());
            guestUserRepository.save(guestUser);
            logger.info("Operation Insert Table guest Data {} | Success", guestUser.display());

            try {
                mailService.guestResetOneTimePassword(guestUserName, email, examID, guestUserPhoneNumber,
                        oneTimePassword);

            } catch (Exception e) {
                logger.info(e.toString());
                logger.warn(e.getLocalizedMessage());
            }

        } else {

            status = "invalid";
            paragraph = "This accounts doesn't exist. Please try again.";
            header3 = "Guest Email account not found!";

        }

        model.addAttribute("loginlink", loginlink);
        model.addAttribute("status", status);
        model.addAttribute("paragraph", paragraph);
        model.addAttribute("header3", header3);
        model.addAttribute("header5", header5);
        
        logger.info("Called resetOnetimePassword with parameter(guestEmail={}) ,(GuestExamID={}) | Success", email, examID);
        return "MailVerify";

    }

    @GetMapping(value = "/guest-exam/{encodedEmailAddress}_{encodedPassword}_{examID}/login")
    public String getGuestUserLogin(Model model,
            @PathVariable String encodedEmailAddress, @PathVariable String encodedPassword, @PathVariable String examID)
            throws ParseException {

        try {

            logger.info("Called getGuestUserLogin with parameter(guestEmail={}) ,(Password={},(examID={})",
                    encodedEmailAddress, encodedPassword, examID);
            String loginlink = "/guest-exam/" + encodedEmailAddress + "_" + examID;

            GuestUser guestUser = guestUserRepository.getGuestUserbyEmail(encodedEmailAddress);

            if (guestUser != null) {

                String oneTimePassword = guestUser.getOne_time_password();

                if (passwordEncoder.matches(encodedPassword, oneTimePassword)) {
                    String status = "success";
                    String header3 = "Entered password is correct !";
                    String paragraph = "Guest Account is verified successfully!";

                    model.addAttribute("loginlink", loginlink);
                    model.addAttribute("status", status);
                    model.addAttribute("paragraph", paragraph);
                    model.addAttribute("header3", header3);

                    String redirectUrl = "/guest-exam/" + examID + "/questions";
                    return "redirect:" + redirectUrl;


                } else {
                    String status = "invalid";
                    String header3 = "Incorrect Password !";
                    String paragraph = "Incorrect Password ! Please try again.";

                    model.addAttribute("loginlink", loginlink);
                    model.addAttribute("status", status);
                    model.addAttribute("paragraph", paragraph);
                    model.addAttribute("header3", header3);

                }

            } else if (guestUser == null) {
                String status = "invalid";
                String header3 = "User Account not found !";
                String paragraph = "User Account not found ! Please try again.";

                model.addAttribute("loginlink", loginlink);
                model.addAttribute("status", status);
                model.addAttribute("paragraph", paragraph);
                model.addAttribute("header3", header3);

            }
            logger.info("Called getGuestUserLogin with parameter(guestEmail={}) ,(Password={},(examID={}) | Success",
                    encodedEmailAddress, encodedPassword, examID);
            return "MailVerify";

        } catch (Exception e) {
            logger.error(e.getLocalizedMessage());
            return "500";
        }
    }

    private String generateRandomText(int length) {
        logger.info("Called generateRandomText with parameter(Password Length={})",
                   length);
        String allowedCharacters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        SecureRandom random = new SecureRandom();
        StringBuilder randomText = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(allowedCharacters.length());
            randomText.append(allowedCharacters.charAt(randomIndex));
        }
        logger.info("Called generateRandomText with parameter(Password Length={})  | Success",
                   length);
        return randomText.toString();
    }

}
