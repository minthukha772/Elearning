package com.blissstock.mappingSite.controller;

import javax.validation.Valid;

import com.blissstock.mappingSite.dto.UserRegisterDTO;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RegisterConfirm {

  @Valid
  @PostMapping("/register_confirm/student")
  public String registerForm(
    @Valid @ModelAttribute("userInfo") UserRegisterDTO userInfo,
    @PathVariable(name = "role", required = false) String role,
    Model model
  ) {
    model.addAttribute("userInfo", userInfo);
    return "index";
  }

  
}
