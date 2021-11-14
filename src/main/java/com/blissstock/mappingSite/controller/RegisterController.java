package com.blissstock.mappingSite.controller;

import com.blissstock.mappingSite.dto.TeacherRegisterDTO;
import com.blissstock.mappingSite.dto.UserRegisterDTO;
import com.blissstock.mappingSite.validation.validators.EmailValidator;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class RegisterController {

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

    model.addAttribute("task", "register");
    model.addAttribute("role", role);
    model.addAttribute("postAction", "/register/" + role);

    return "ST0001_register.html";
  }

  @PostMapping(path = "/register/student")
  public String toSutdentRegisterConfirm(
    Model model,
    @Valid @ModelAttribute("userInfo") UserRegisterDTO userInfo,
    BindingResult bindingResult,
    @RequestParam(value="action", required=true) String action
  ) {
    String role = "student";
    model.addAttribute("task", "register");
    model.addAttribute("role", role);
    model.addAttribute("postAction", "/register/" + role);

    if (bindingResult.hasErrors()) {
        return "ST0001_register.html";
    }

    if(action.equals("submit")){
      System.out.println("Submitting");
    }

    //Information For Randering Confirm
    model.addAttribute("infoMap", userInfo.toMap());
    return "ST0001_register.html";
  }

  @PostMapping(path = "/register/teacher", params = "action=next")
  public String toTeacherRegisterConfirm(
    Model model,
    @Valid @ModelAttribute("userInfo") UserRegisterDTO userInfo,
    BindingResult bindingResult
  ) {
    //System.out.println(userInfo);
    model.addAttribute("role", "student");

    if (bindingResult.hasErrors()) {
        return "ST0001_register.html";
    }

    //Information For Randering Confirm
    model.addAttribute("infoMap", userInfo.toMap());
    return "ST0001_register.html";
  }

  
}
