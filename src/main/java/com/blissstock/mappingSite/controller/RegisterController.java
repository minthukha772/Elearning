package com.blissstock.mappingSite.controller;

import com.blissstock.mappingSite.dto.TeacherRegisterDTO;
import java.util.List;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import org.hibernate.validator.HibernateValidator;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Validated
public class RegisterController {

  @ExceptionHandler(value = ConstraintViolationException.class)
  public String exception(ConstraintViolationException exception) {
    System.out.println("excption occur");
    System.out.println(exception.getMessage());
    return "redirect:/";
  }

  @GetMapping("/register/{role}/{email}")
  public String registerForm(
    @PathVariable(required = false) String role,
    @Email @PathVariable String email,
    Model model
  ) {
    if (role == null || role != "teacher") {
      role = "student";
    }

    System.out.println(role);
    System.out.println(email);
    TeacherRegisterDTO userInfo = new TeacherRegisterDTO();
    userInfo.setEmail(email);
    model.addAttribute("action", "register");
    model.addAttribute("role", role);
    model.addAttribute("userInfo", userInfo);
    return "register.html";
  }

  @PostMapping("/register")
  public String register(
    Model model,
    @Valid @ModelAttribute("userInfo") TeacherRegisterDTO userInfo,
    BindingResult bindingResult
  ) {
    //model.addAttribute("userInfo", userInfo);
    //System.out.println(bindingResult.getFieldError("passwordMatch"));
    //System.out.println(bindingResult.toString());
    return "register.html";
  }
}
