package com.blissstock.mappingSite.controller;

import java.io.IOException;
import java.util.Locale;

import javax.mail.MessagingException;

import com.blissstock.mappingSite.entity.UserAccount;
import com.blissstock.mappingSite.service.MailServiceImpl;
import com.blissstock.mappingSite.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/")
public class MailCheckController {
    @Autowired
    UserService userService;

    @Autowired
    MailServiceImpl mailServiceImpl;

    @GetMapping(path = { "/verify_password" })
    public String mailVerify(
            @RequestParam(value = "token", required = true) String token, Model model) {
        System.out.println("mail verify called" + token);
        final String tokenType = "VERIFICATION";
        try {
            System.out.println("get token callsed");
            UserAccount userAccount = userService.getUserAccountByToken(token, tokenType);
            System.out.println(userAccount.toString());

            if (userAccount.isMailVerified()) {
                System.out.println("user  has already been verified");
                String header3 = "Mail has already been verified ";
                String header5 = "";
                String paragraph = "Please login with email and password to continue.";
                model.addAttribute("status", "alreadyverified");
                model.addAttribute("header3", header3);
                model.addAttribute("header5", header5);
                model.addAttribute("paragraph", paragraph);
                return "MailVerify.html";
            } else {

                System.out.println("mail verified");

                userAccount.setMailVerified(true);
                userService.updateUserAccount(userAccount);
                userService.setAsUsedToken(token);
                String header3 = "Mail verification success ";
                String header5 = "Acknowledgement!";
                String paragraph = "You have successfully verified mail! Please login to start using the service.";
                model.addAttribute("status", "success");
                model.addAttribute("header3", header3);
                model.addAttribute("header5", header5);
                model.addAttribute("paragraph", paragraph);
                return "MailVerify.html";
            }

        } catch (Exception e) {
            System.out.println(e.toString());
        }
        System.out.println("Invalid token");
        String header3 = "Invalid token";
        String header5 = "";
        String paragraph = "The verification mail is invalid! Please check the mail again.";
        model.addAttribute("status", "invalid");
        model.addAttribute("header3", header3);
        model.addAttribute("header5", header5);
        model.addAttribute("paragraph", paragraph);
        return "MailVerify.html";

        // System.out.println(token);
        // System.out.println(userAccount.toString());

    }

    //test impl
    @RequestMapping("/sendMailWithInlineImage"
    // , method = RequestMethod.POST
    )
    public String sendMailWithInline(
    // @RequestParam("recipientName") final String recipientName,
    // @RequestParam("recipientEmail") final String recipientEmail,
    // @RequestParam("image") final MultipartFile image,
    // final Locale locale
    )
    throws MessagingException, IOException {

        mailServiceImpl.sendMailWithInline(
            "kyaw", "tinhuat38@gmail.com"
            // , image.getName(),
            // image.getBytes(), image.getContentType(), locale
            );
        return "sent";

    }
}
