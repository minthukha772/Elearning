package com.blissstock.mappingSite.controller;

import com.blissstock.mappingSite.dto.LoginDTO;
import com.blissstock.mappingSite.entity.UserAccount;
import com.blissstock.mappingSite.enums.UserRole;
import com.blissstock.mappingSite.exceptions.UserNotFoundException;
import com.blissstock.mappingSite.service.MailService;
import com.blissstock.mappingSite.service.UserService;
import com.blissstock.mappingSite.service.UserSessionService;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class LoginController {

  @Autowired
  UserSessionService userSessionService;

  @Autowired
  UserService userService;

  @Autowired
  MailService mailService;

  private static Logger logger= LoggerFactory.getLogger(LoginController.class);

  @GetMapping("/login")
  public String loginView(
    Model model,
    String error,
    String logout,
    String message,
    String resetSuccess,
    String tokenError
  ) {
    UserRole userRole = userSessionService.getRole();
    if (
      userRole != UserRole.GUEST_USER &&
      userRole != UserRole.ADMIN &&
      userRole != UserRole.SUPER_ADMIN
    ) {
      logger.info("redirect to home");
      return "redirect:/home";
    }
    if (resetSuccess != null) {
      message =
        "A password reset link has been sent to your email. Please check your email to continue.";
    }

    //log
    logger.info("resetSuccess is {}", resetSuccess);

    if(tokenError !=null){
      error = "invalid token";
    }

    //log
    logger.info("tokenError is {}", tokenError);

    if (error != null) {
      if (error.isBlank()) {
        model.addAttribute("error", "Your email and password is invalid.");
      } else {
        model.addAttribute("error", error);
      }
    }

    //log
    logger.info("error is {}", error);

    if (logout != null) {
      model.addAttribute("message", "You have been logged out successfully");
    }
    if (message != null) {
      model.addAttribute("message", message);
    }
    model.addAttribute("userInfo", new LoginDTO());

    //log
    logger.info("message is {}", message);
    logger.info("another attempt");

    return "CM0005_login.html";
  }
  /*   @PostMapping(value = "/login/reset_password")
  public ResponseEntity<Object> resetPassword(
    String email,
    HttpServletRequest request
  ) {
    System.out.println("Login called");
    System.out.println(email);

    UserAccount userAccount = userService.getUserAccountByEmail(email);
    if (userAccount == null) {
      return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body("The email address is not registered in the system.");
    }

    String appUrl =
      request.getServerName() + // "localhost"
      ":" +
      request.getServerPort(); // 8080

    try {
      mailService.sendResetPasswordMail(userAccount, appUrl);
    } catch (Exception e) {
      e.printStackTrace();
      return ResponseEntity
        .status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body("Something went wrong");
    }

    return ResponseEntity
      .status(HttpStatus.OK)
      .body(
        "A password reset link has been sent to your email. Please check your email to continue."
      );
  } */
}
