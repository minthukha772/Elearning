package com.blissstock.mappingSite.controller;

import com.blissstock.mappingSite.dto.TeacherRegisterDTO;
import com.blissstock.mappingSite.dto.UserRegisterDTO;
import com.blissstock.mappingSite.entity.UserInfo;
import com.blissstock.mappingSite.exceptions.UserAlreadyExistException;
import com.blissstock.mappingSite.service.MailService;
import com.blissstock.mappingSite.service.UserService;
import com.blissstock.mappingSite.validation.validators.EmailValidator;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class RegisterController {

  @Autowired
  UserService userService;

  @Autowired
  MailService mailService;

  @ExceptionHandler(value = ConstraintViolationException.class)
  public String exception(ConstraintViolationException exception) {
    System.out.println("excption occur");
    System.out.println(exception.getMessage());
    return "redirect:/email_check/register/";
  }

  //Redirect to email_check
  
  @GetMapping("/register")
  public String registerForm() {
    return "redirect:/email_check/register/";
  }

  //Handle User GET Request
  @Valid
  @PreAuthorize("isAnonymous()")
  @GetMapping("/register/{role}/{email}")
  public String registerForm(
    @PathVariable(name = "role", required = false) String role,
    @PathVariable(name = "email") String email,
    Model model
  ) {
    //if email is not validate throw ConstraintViolationException exception
    if (!new EmailValidator().validateEmail(email)) {
      throw new ConstraintViolationException("Invalid Email", null);
    }

    //set role to student unless it is equal to teacher
    if (role == null || !role.equals("teacher")) {
      role = "student";
    }

    //Initialize UserInfo
    UserRegisterDTO userInfo;

    userInfo =
      role.equals("student") ? new UserRegisterDTO() : new TeacherRegisterDTO();
    userInfo.setEmail(email);
    model.addAttribute("userInfo", userInfo);
    //

    model.addAttribute("task", "Register");
    model.addAttribute("role", role);
    model.addAttribute("postAction", "/register/" + role);

    return "ST0001_register.html";
  }

  @PostMapping(path = "/register/student/{email}")
  public String studentRegisterConfirm(
    Model model,
    @Valid @ModelAttribute("userInfo") UserRegisterDTO userInfo,
    BindingResult bindingResult,
    @RequestParam(value = "action", required = true) String action,
    HttpServletRequest request,
    Errors errors
  ) {
    String role = "student";
    model.addAttribute("task", "Register");
    model.addAttribute("role", role);
    model.addAttribute("postAction", "/register/" + role);

    if (bindingResult.hasErrors()) {
      return "ST0001_register.html";
    }

    try {
      if (action.equals("submit")) {
        try {
          UserInfo savedUserInfo = userService.addUser(userInfo);

          String appUrl =
            request.getServerName() + // "localhost"
            ":" +
            request.getServerPort(); //"8080"
          mailService.sendVerificationMail(savedUserInfo.getUserAccount(), appUrl);
        } catch (UserAlreadyExistException e) {
          e.printStackTrace();
          model.addAttribute("userExistError", true);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    } catch (Exception e) {
      System.out.println(e);
    }

    //Information For Randering Confirm
    model.addAttribute("infoMap", userInfo.toMap());
    return "ST0001_register.html";
  }

  @PostMapping(path = "/register/teacher/{email}")
  public String teacherRegisterConfirm(
    Model model,
    @Valid @ModelAttribute("userInfo") TeacherRegisterDTO userInfo,
    BindingResult bindingResult,
    @RequestParam(value = "action", required = true) String action,
    HttpServletRequest request,
    Errors errors
  ) {
    String role = "teacher";
    model.addAttribute("task", "Register");
    model.addAttribute("role", role);
    model.addAttribute("postAction", "/register/" + role);

    if (bindingResult.hasErrors()) {
      return "ST0001_register.html";
    }

    try {
      if (action.equals("submit")) {
        try {
          UserInfo savedUserInfo = userService.addUser(userInfo);

          String appUrl =
            request.getServerName() + // "localhost"
            ":" +
            request.getServerPort(); //"8080"
          mailService.sendVerificationMail(savedUserInfo.getUserAccount(), appUrl);
        } catch (UserAlreadyExistException e) {
          e.printStackTrace();
          model.addAttribute("userExistError", true);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    } catch (Exception e) {
      System.out.println(e);
    }

    //Information For Randering Confirm
    model.addAttribute("infoMap", userInfo.toMap());
    return "ST0001_register.html";
  }
}
