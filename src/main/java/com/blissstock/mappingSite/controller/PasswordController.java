package com.blissstock.mappingSite.controller;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import com.blissstock.mappingSite.dto.PasswordDTO;
import com.blissstock.mappingSite.entity.Token;
import com.blissstock.mappingSite.entity.UserAccount;
import com.blissstock.mappingSite.enums.PasswordResetType;
import com.blissstock.mappingSite.enums.TokenType;
import com.blissstock.mappingSite.exceptions.UserNotFoundException;
import com.blissstock.mappingSite.service.MailServiceImpl;
import com.blissstock.mappingSite.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

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
  )
    throws UserNotFoundException {
    System.out.println(userEmail);
    UserAccount user = userService.getUserAccountByEmail(userEmail);
    if (user == null) {
      throw new UserNotFoundException();
    }
    String token = UUID.randomUUID().toString();
    String appUrl =
      request.getServerName() + // "localhost"
      ":" +
      request.getServerPort();
    userService.createToken(user, token, TokenType.PASSWORD_RESET);
    mailService.sendResetPasswordMail(user, appUrl);
    return "redirect:/login?resetSuccess";
  }

  @GetMapping(path = { "{role}/change_password", "/change_password" })
  public String changePasswordView(
    Model model,
    @PathVariable(name = "role", required = false) String role,
    String token
  ) {
    System.out.println(role);
    //Role being null meaning user is trying to reset password
    if (
      role != null &&
      !(
        role.equals("student") || role.equals("teacher") || role.equals("admin")
      )
    ) {
      throw new ResponseStatusException(
        HttpStatus.NOT_FOUND,
        "entity not found"
      );
    }

    PasswordDTO passwordDTO = new PasswordDTO();
    String title = "";
   

    if (  role == null) {
      //This is the password reset case by token
      passwordDTO.setType(PasswordResetType.TOKEN.name());
      title = "Reset Password";
      boolean isTokenValid = true;

      if(token == null){
        isTokenValid = false;
      }else{
        Token savedToken = userService.getToken(token, TokenType.PASSWORD_RESET);
        if(savedToken == null){
          isTokenValid = false;
        }else{
          //If token is valid, set it as old password for futher use.
          passwordDTO.setOldPassword(token);
        }
      }


      if(!isTokenValid){
        model.addAttribute("error", "Invalid Token");
      }
    } else {
      //This is the password reset case by old password
      passwordDTO.setType(PasswordResetType.OLD_PASSWORD.name());
      title = "Change Password";
    }

    model.addAttribute("title", title);
    model.addAttribute("passwordDTO", passwordDTO);

    return "CM0006_change_password_screen";
  }

  @PostMapping("/change_password")
  public String changePasswordPost(Model model, String token) {
    System.out.println(token);

    PasswordDTO passwordDTO = new PasswordDTO();
    String title = "";

    if (token != null) {
      //This is the password reset case by token
      passwordDTO.setType(PasswordResetType.TOKEN.name());
      title = "Reset Password";
    } else {
      //This is the password reset case by old password
      passwordDTO.setType(PasswordResetType.OLD_PASSWORD.name());
      title = "Change Password";
    }

    model.addAttribute("title", title);
    model.addAttribute("passwordDTO", passwordDTO);

    return "CM0006_change_password_screen";
  }
}
