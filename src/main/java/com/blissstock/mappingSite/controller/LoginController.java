package com.blissstock.mappingSite.controller;

import com.blissstock.mappingSite.dto.LoginDTO;
import javax.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

  @GetMapping("/login")
  public String loginView(Model model) {
    model.addAttribute("userInfo", new LoginDTO());
    return "CM0005_login.html";
  }

  @PostMapping("/login")
  public String login(
    Model model,
    @Valid @ModelAttribute("userInfo") LoginDTO userInfo,
    BindingResult bindingResult,
    @RequestParam(value = "action", required = true) String action
  ) {
    if (bindingResult.hasErrors()) {
      System.out.println("has error");
    }
    if (action.equals("forget")) {
      //TODO add logic
    } else if (action.equals("login")) {
      //TODO add logic
    }
    model.addAttribute("userInfo", userInfo);
    return "CM0005_login.html";
  }
}
