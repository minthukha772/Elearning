package com.blissstock.mappingSite.controller;

import javax.validation.Valid;

import com.blissstock.mappingSite.dto.EmailCheckRegisterDTO;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class EmailCheckController {

  /// A Get Method For Email Check Before Register
  @GetMapping(
    path = { "/email_check/register/{role}", "/email_check/register/" }
  )
  public String registerForm(
    @PathVariable(name = "role", required = false) String role,
    Model model
  ) {
    // Tell Thymeleaf to render as Reister
    model.addAttribute("action", "register");

    // For Post Method Action
    model.addAttribute("postAction", "/email_check/register/");

    // Initialize Form
    if (role == null || !role.equals("teacher")) {
      role = "student";
    }
    EmailCheckRegisterDTO emailCheckRegisterDTO = new EmailCheckRegisterDTO();
    emailCheckRegisterDTO.setRole(role);
    model.addAttribute("emailCheck", emailCheckRegisterDTO);

    // render
    return "ST0000_email_check.html";
  }

  /// A Post Method for email check before Register
  @PostMapping("/email_check/register/")
  public String register(
    Model model,
    @Valid @ModelAttribute("emailCheck") EmailCheckRegisterDTO emailRegister,
    BindingResult bindingResult
  ) {
    if (bindingResult.hasErrors()) {
      model.addAttribute("action", "register");

      // For Rendering the title
      model.addAttribute("role", emailRegister.getRole());

      System.out.println(emailRegister);

      // render
      return "ST0000_email_check.html";
    }
    // Redirect to Register
    // Two Valid Address:
    // 1. /register/student/email@gmail.com
    // 2. /register/teacher/email@gmail.com
    return (
      "redirect:/register/" +
      emailRegister.getRole() +
      "/" +
      emailRegister.getEmail() +
      "/"
    );
  }

  @GetMapping("/email_check/password_reset")
  public String resetPassword(
    @PathVariable(required = false) String role,
    Model model
  ) {
    if (role == null || role != "reset_password") {
      role = "register";
    }
    model.addAttribute("role", role);
    model.addAttribute("action", "register");
    model.addAttribute("email", new EmailCheckRegisterDTO());
    return "ST0000_email_check.html";
  }
}
