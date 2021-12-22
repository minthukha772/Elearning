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
                return "index.html";
            } else {
                System.out.println("mail verified");
                userAccount.setMailVerified(true);
                userService.updateUserAccount(userAccount);
                return "mail_Verify.html";
            }

        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return "index.html";

        // System.out.println(token);
        // System.out.println(userAccount.toString());

    }
}
