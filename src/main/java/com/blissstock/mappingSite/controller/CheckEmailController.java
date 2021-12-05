package com.blissstock.mappingSite.controller;

import com.blissstock.mappingSite.dto.EmailCheckRegisterDTO;
import com.blissstock.mappingSite.service.UserService;
import com.blissstock.mappingSite.validation.validators.EmailValidator;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class CheckEmailController {

  @Autowired
  UserService userService;

  /// A Get Method For Email Check Before Register
  @GetMapping(path = { "/check_email/{role}", "/check_email" })
  public String registerForm(
    @PathVariable(name = "role", required = false) String role,
    Model model,
    String email
  ) {
    // Tell Thymeleaf to render as Reister
    model.addAttribute("action", "register");

    // For Post Method Action
    model.addAttribute("postAction", "/check_email/register/");

/*     if (email != null && !email.isBlank()) {
      EmailValidator emailValidator = new EmailValidator();
      boolean isValidEmail = emailValidator.validateEmail(email);
      if (isValidEmail) {
        model.addAttribute("email", email);
      }
    } */

    // Initialize Form
    if (role == null || !role.equals("teacher")) {
      role = "student";
    }
    EmailCheckRegisterDTO emailCheckRegisterDTO = new EmailCheckRegisterDTO();
    emailCheckRegisterDTO.setEmail(email);
    emailCheckRegisterDTO.setRole(role);
    model.addAttribute("emailCheck", emailCheckRegisterDTO);

    // render
    return "ST0000_check_email";
  }

  /// A Post Method for email check before Register
  @PostMapping("/check_email/register")
  public String register(
    Model model,
    @Valid @ModelAttribute("emailCheck") EmailCheckRegisterDTO emailRegister,
    BindingResult bindingResult
  ) {
    if (bindingResult.hasErrors()) {
      model.addAttribute("action", "register");
      /* // For Rendering the title
      model.addAttribute("role", emailRegister.getRole());
 */
      // render
      return "ST0000_check_email.html";
    }
    if (userService.getUserAccountByEmail(emailRegister.getEmail()) != null) {
      //User already exists
      model.addAttribute("action", "register");
      /*  // For Rendering the title
      model.addAttribute("role", emailRegister.getRole()); */
      //For displaying error message
      model.addAttribute("error", true);

      // render
      return "ST0000_check_email.html";
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

  @GetMapping("/check_email/password_reset")
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
    return "ST0000_check_email.html";
  }
}
