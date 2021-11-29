package com.blissstock.mappingSite.controller;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import com.blissstock.mappingSite.entity.Token;
import com.blissstock.mappingSite.entity.UserAccount;
import com.blissstock.mappingSite.enums.TokenType;
import com.blissstock.mappingSite.exceptions.UserNotFoundException;
import com.blissstock.mappingSite.service.MailServiceImpl;
import com.blissstock.mappingSite.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PasswordController {

  @Autowired
  UserService userService;

  @Autowired
  MailServiceImpl mailService;

  @GetMapping("/token")
  public String createToken(Model model) {
    String token = UUID.randomUUID().toString();
    UserAccount userAccount = userService.getUserAccountByEmail(
      "lycuzmarki@gmail.com"
    );
    System.out.println(userAccount);
    userService.createToken(userAccount, token, TokenType.VERIFICATION);
    return "redirect:/";
  }

  @GetMapping("/verify_password")
  public String verifyPassword(Model model, String token) {
    Token savedToken = userService.getToken(token, TokenType.VERIFICATION);
    if (savedToken == null) {
      System.out.println("invalid token");
      return "redirect:/home?tokenError";
    } else {
      UserAccount savedUserAccount = savedToken.getUserAccount();
      savedUserAccount.setMailVerified(true);
      userService.updateUserAccount(savedUserAccount);
      savedToken.setUsed(true);
      userService.updateToken(savedToken);
    }

    System.out.println(token);
    return "redirect:/home";
  }

  
  @RequestMapping("/reset_password")
  public String resetPassword(
    HttpServletRequest request,
    @RequestParam("email") String userEmail
  ) throws UserNotFoundException {
    UserAccount user = userService.getUserAccountByEmail(userEmail);
    if (user == null) {
      throw new UserNotFoundException();
    }
    String token = UUID.randomUUID().toString();
    String appUrl =
            request.getServerName() + // "localhost"
            ":" +
            request.getServerPort();
    userService.createToken(user, token,TokenType.PASSWORD_RESET);
    mailService.sendResetPasswordMail(user, appUrl);
    return "redirect:/login?resetSuccess";
  } 

  @GetMapping("/send")
  public String sendMail(Model model) {
    mailService.sendMail(
      "Test Subject",
      "lycuzmarki@gmail.com",
      "",
      "",
      "body"
    );
    return "redirect:/";
  }
}
