package com.blissstock.mappingSite.controller;

import com.blissstock.mappingSite.entity.UserAccount;
import com.blissstock.mappingSite.exceptions.UserNotFoundException;
import com.blissstock.mappingSite.service.UserService;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PasswordController {

  @Autowired
  UserService userService;
/* 
  @PostMapping("/reset_password")
  public GenericResponse resetPassword(
    HttpServletRequest request,
    @RequestParam("email") String userEmail
  ) {
    UserAccount user = userService.getUserAccountByEmail(userEmail);
    if (user == null) {
      throw new UserNotFoundException();
    }
    String token = UUID.randomUUID().toString();
    userService.createPasswordResetTokenForUser(user, token);
    mailSender.send(
      constructResetTokenEmail(
        getAppUrl(request),
        request.getLocale(),
        token,
        user
      )
    );
    return new GenericResponse(
      messages.getMessage(
        "message.resetPasswordEmail",
        null,
        request.getLocale()
      )
    );
  } */
}
