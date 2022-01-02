package com.blissstock.mappingSite.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.blissstock.mappingSite.dto.PasswordDTO;
import com.blissstock.mappingSite.entity.Token;
import com.blissstock.mappingSite.entity.UserAccount;
import com.blissstock.mappingSite.entity.VerificationToken;
import com.blissstock.mappingSite.enums.PasswordResetType;
import com.blissstock.mappingSite.enums.TokenType;
import com.blissstock.mappingSite.service.MailServiceImpl;
import com.blissstock.mappingSite.service.UserService;
import com.blissstock.mappingSite.service.UserSessionService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
// import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

@Controller
public class PasswordController {

  private static final Logger logger = LoggerFactory.getLogger(
      PasswordController.class);

  @Autowired
  UserService userService;
  @Autowired
  UserSessionService userSessionService;
  @Autowired
  MailServiceImpl mailService;

  @Autowired
  private PasswordEncoder passwordEncoder;

  
    @GetMapping("/password/encrypt")
    public String encrpty(Model model, String pass) {
    String password = passwordEncoder.encode(pass);
    System.out.println(password);
     return "redirect:/";
    }
  /*
   * @GetMapping("/token")
   * public String createToken(Model model) {
   * String token = UUID.randomUUID().toString();
   * UserAccount userAccount = userService.getUserAccountByEmail(
   * "lycuzmarki@gmail.com"
   * );
   * ////system.out.println(userAccount);
   * userService.createToken(userAccount, token, TokenType.VERIFICATION);
   * return "redirect:/";
   * }
   */
  @GetMapping("password/verify_password")
  public String verifyPassword(Model model, @RequestParam(value = "token", required = true) String token) {
    logger.info("GET requested");
    logger.info("Reset password by token called");
    model.addAttribute("title", "Change");
    if (token == null) {
      model.addAttribute("error", "The password reset mail is invalid! Please check the mail again.");
      return "CM006_reset_password_screen";

    }
    UserAccount userAccount = userService.getUserAccountByToken(token,
        "PASSWORD_RESET");
    if (userAccount == null) {
      System.out.println("Invalid token");
      String header3 = "Invalid token";
      String header5 = "";
      String paragraph = "The password reset mail is invalid! Please check the mail again.";
      model.addAttribute("status", "invalid");
      model.addAttribute("header3", header3);
      model.addAttribute("header5", header5);
      model.addAttribute("paragraph", paragraph);
      return "MailVerify.html";
    } else {
      PasswordDTO passwordDTO = new PasswordDTO();
      model.addAttribute("passwordDTO", passwordDTO);
      passwordDTO.setOldPassword(token);
      model.addAttribute("token", token);
      return "CM006_reset_password_screen";

    }

    // Token savedToken = userService.getToken(token, TokenType.VERIFICATION);
    // if (savedToken == null) {
    // // //System.out.println("invalid token");
    // return "redirect:/login?tokenError";
    // } else {
    // UserAccount savedUserAccount = savedToken.getUserAccount();
    // savedUserAccount.setMailVerified(true);
    // userService.updateUserAccount(savedUserAccount);
    // savedToken.setUsed(true);
    // userService.updateToken(savedToken);
    // }

    // //System.out.println(token);
    // return "redirect:/home";
  }

  @PostMapping("password/verify_password")
  public String changePassoword(Model model, @Valid @ModelAttribute("passwordDTO") PasswordDTO passwordDTO,
      @ModelAttribute("token") String token,
      BindingResult bindingResult) {
    logger.info("Post Method");
    model.addAttribute("title", "Change");
    System.out.println(passwordDTO.toString());
    System.out.println(token);
    model.addAttribute("passwordDTO", passwordDTO);
    // System.out.println(passwordDTO.toString());
    if (bindingResult.hasErrors()) {
      logger.warn("validation error, {}", bindingResult.getFieldError());

      model.addAttribute("error", bindingResult.getFieldError().getDefaultMessage());
      return "CM006_reset_password_screen";
    }
    logger.info("passwrod binding is success");

    if (!passwordDTO.getPassword().equals(passwordDTO.getConfirmPassword())) {
      logger.info("Password and coniform password do not match");

      model.addAttribute("error", "password and confirm password should match");
      return "CM006_reset_password_screen";
    } else {
      UserAccount userAccount = userService.getUserAccountByToken(token, "PASSWORD_RESET");
      if (userAccount == null) {

        model.addAttribute("error", "The password reset mail is invalid! Please check the mail again.");
        return "CM006_reset_password_screen";

      }
      userAccount.setPassword(passwordEncoder.encode(passwordDTO.getPassword()));
      userService.updateUserAccount(userAccount);
      userService.setAsUsedToken(token);
      model.addAttribute("success", "Password Change Success");
      model.addAttribute("message", "Plesase login with new passowrd to continue");
      return "CM006_reset_password_screen";
    }

  }

  @RequestMapping("password/reset_password")
  public String resetPassword(
      HttpServletRequest request,
      @RequestParam("email") String userEmail) {
    logger.info("POST requested, email {}", userEmail);
    UserAccount user = userService.getUserAccountByEmail(userEmail);
    if (user == null) {
      return ("redirect:/check_email/reset_password?email=" +
          userEmail +
          "&error=true");
    }
    /* String token = UUID.randomUUID().toString(); */
    String appUrl = request.getServerName() + // "localhost"
        ":" +
        request.getServerPort();
    /* userService.createToken(user, token, TokenType.PASSWORD_RESET); */
    mailService.sendResetPasswordMail(user, appUrl);
    return "redirect:/login?resetSuccess=true";
  }

  @GetMapping(path = { "{role}/change_password" })
  public String changePasswordView(
      Model model,
      @PathVariable(name = "role", required = true) String role,
      String token) {
    // //System.out.println("change password called");
    // Role being null meaning user is trying to reset password
    logger.info("GET requested, role {}", role);
    if (role != null &&
        !(role.equals("student") || role.equals("teacher") || role.equals("admin"))) {
      throw new ResponseStatusException(
          HttpStatus.NOT_FOUND,
          "entity not found");
    }

    PasswordDTO passwordDTO = new PasswordDTO();
    String title = "";

    if (role == null) {
      // This is the password reset case by token
      passwordDTO.setType(PasswordResetType.TOKEN.name());
      title = "Reset Password";
      boolean isTokenValid = true;

      if (token == null) {
        isTokenValid = false;
      } else {
        Token savedToken = userService.getToken(
            token,
            TokenType.PASSWORD_RESET);
        if (savedToken == null) {
          isTokenValid = false;
        } else {
          // If token is valid, set it as old password for futher use.
          passwordDTO.setOldPassword(token);
        }
      }

      if (!isTokenValid) {
        model.addAttribute("error", "Invalid Token");
      }
    } else {
      // This is the password reset case by old password
      passwordDTO.setType(PasswordResetType.OLD_PASSWORD.name());
      title = "Change Password";
    }

    model.addAttribute("title", title);
    model.addAttribute("passwordDTO", passwordDTO);
    logger.trace("Title {}", model.getAttribute("title"));

    return "CM0006_change_password_screen";
  }

  @PostMapping("{role}/change_password")
  public String changePasswordPost(
      Model model,
      @Valid @ModelAttribute("passwordDTO") PasswordDTO passwordDTO,
      BindingResult bindingResult) {
    logger.info("POST request, {}", passwordDTO);

    String title = "Change Password";
    model.addAttribute("type", "OLD_PASSWORD");
    model.addAttribute("title", title);
    model.addAttribute("passwordDTO", passwordDTO);

    if (bindingResult.hasErrors()) {
      logger.warn("validation error, {}", bindingResult.getFieldError());

      model.addAttribute("error", bindingResult.getFieldError().getDefaultMessage());
      return "CM0006_change_password_screen";
    }
    logger.info("passwrod binding is success");

    System.out.println("Password DTO is" + passwordDTO.toString());
    // //System.out.println(passwordDTO.getPassword());
    // //System.out.println(passwordDTO.getConfirmPassword());
    // //System.out.println(passwordDTO.getOldPassword());

    if (!passwordDTO.getPassword().equals(passwordDTO.getConfirmPassword())) {
      logger.info("Password and coniform password do not match");

      model.addAttribute("error", "password and confirm password should match");
      return "CM0006_change_password_screen";
    }
    // get user email

    UserAccount userAccount = userSessionService.getUserAccount();
    // System.out.println("stored psw is " + userAccount.getPassword());
    // System.out.println(userAccount.getPassword());
    // System.out.println(passwordEncoder.encode(passwordDTO.getOldPassword()));
    // System.out.println(passwordEncoder.encode("a1111111"));

    if (passwordEncoder.matches(passwordDTO.getOldPassword(), userAccount.getPassword())) {

      userAccount.setPassword(passwordEncoder.encode(passwordDTO.getPassword()));
      userService.updateUserAccount(userAccount);

      model.addAttribute("success", "Password Change Success");
      model.addAttribute("message", "Plesase login with new passowrd to continue");
      // todo navigate to complete screen;
      // model.addAttribute("title", "Login");
      return "CM0006_change_password_screen";
      // return "CM0005_login.html";
    } else {
      logger.info(" Password and stored password do not match");

      model.addAttribute("error", "Please enter the correct old password");
      return "CM0006_change_password_screen";

    }

  }
}
