package com.blissstock.mappingSite.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;
import antlr.Token;

import com.blissstock.mappingSite.service.UserService;
import com.blissstock.mappingSite.service.UserServiceImpl;

import java.util.Optional;

import com.blissstock.mappingSite.entity.UserAccount;
import com.blissstock.mappingSite.repository.TokenRepository;
import com.blissstock.mappingSite.repository.UserRepository;

@Controller
@RequestMapping("/")
public class MailCheckController {
    @Autowired
    UserService userService;

    @GetMapping(path = { "/verify_password/{token}" })
    public String mailVerify(
            @PathVariable(name = "token", required = false) String token,
            Model model) {
        System.out.println("mail verify called" + token);

        try {
            System.out.println("get token callsed");
            UserAccount userAccount = userService.getUserAccountByToken(token);
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
}
