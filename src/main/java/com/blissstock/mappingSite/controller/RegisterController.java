package com.blissstock.mappingSite.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import javax.validation.constraints.Email;

import com.blissstock.mappingSite.dto.TeacherRegisterDTO;
import com.blissstock.mappingSite.dto.UserRegisterDTO;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RegisterController {

  @ExceptionHandler(value = ConstraintViolationException.class)
  public String exception(ConstraintViolationException exception) {
    System.out.println("excption occur");
    System.out.println(exception.getMessage());
    return "redirect:/email_check/register/";
  }

  @GetMapping("/register")
  public String registerForm() {
    return "redirect:/email_check/register/";
  }

  @Valid
  @GetMapping("/register/{role}/{email}")
  public String registerForm(
    @PathVariable(required = false) String role,
    @Email @PathVariable String email,
    Model model
  ) {
    if (role == null || !role.equals("teacher")) {
      role = "student";
    }

    UserRegisterDTO userInfo;

    userInfo =
      role.equals("student") ? new UserRegisterDTO() : new TeacherRegisterDTO();
    userInfo.setEmail(email);

    model.addAttribute("action", "register");
    model.addAttribute("role", role);

    model.addAttribute("postAction", "/register/"+role);

    model.addAttribute("userInfo", userInfo);
    return "ST0001_register.html";
  }

  @PostMapping("/register/student")
  public String registerStudent(
    Model model,
    @Valid @ModelAttribute("userInfo") UserRegisterDTO userInfo,
    BindingResult bindingResult,
    HttpServletRequest request
  ) {

    model.addAttribute("userInfo", userInfo);

    model.addAttribute("role","student");

    return "ST0001_register.html";
  }

  @PostMapping("/register/teacher")
  public String registerTeacher(
    Model model,
    @Valid @ModelAttribute("userInfo") TeacherRegisterDTO userInfo,
    BindingResult bindingResult,
    HttpServletRequest request
  ) {

    model.addAttribute("userInfo", userInfo);

    model.addAttribute("role","teacher");
    

    return "ST0001_register.html";
  }
}
