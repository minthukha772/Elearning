package com.blissstock.mappingSite.controller;

import com.blissstock.mappingSite.dto.TeacherRegisterDTO;
import com.blissstock.mappingSite.dto.UserRegisterDTO;
import com.blissstock.mappingSite.entity.UserAccount;
import com.blissstock.mappingSite.service.UserService;
import com.blissstock.mappingSite.service.UserSessionService;
import javax.validation.Valid;
import javax.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ProfileEditController {

  @Autowired
  UserSessionService userSessionService;

  @Autowired
  UserService userService;

  @GetMapping(
    path = {
      "/student/profile_edit",
      "/teacher/profile_edit",
      "admin/profile_edit/{id}",
    }
  )
  public String editProfileView(Model model) {
    String email = userSessionService.getEmail();
    String role = userSessionService.getRole().getValue();

    UserRegisterDTO userInfo;
    userInfo = userService.getUserByEmail(email);
    userInfo.setAcceptTerm(true);
    model.addAttribute("userInfo", userInfo);

    model.addAttribute("task", "profile_edit");
    model.addAttribute("role", role);
    model.addAttribute("postAction", "/" + role + "/profile_edit/");

    return "ST0001_register";
  }

  @PostMapping("/student/profile_edit")
  public String editProfile(
    Model model,
    @Valid @ModelAttribute("userInfo") UserRegisterDTO userInfo,
    BindingResult bindingResult,
    @RequestParam(value = "action", required = true) String action
  ) {
    String role = "student";
    model.addAttribute("task", "profile_edit");
    model.addAttribute("role", role);
    model.addAttribute("postAction", "/" + role + "/profile_edit/");

    if (bindingResult.hasErrors()) {
      System.out.println("Template Having error");
      System.out.println(bindingResult.getErrorCount());
      bindingResult
        .getAllErrors()
        .stream()
        .forEach(e -> System.out.println(e.toString()));
      return "ST0001_register";
    }

    model.addAttribute("userInfo", userInfo);
    /*     try {
      if (action.equals("submit")) {
        try {
         userService.updateUser(userInfo);
        } catch (Exception e) {
          System.out.println(e);
        }
      }
    } catch (Exception e) {
      System.out.println(e);
    } */

    //Information For Randering Confirm
    model.addAttribute("infoMap", userInfo.toMap());
    System.out.println(userInfo.toMap());
    return "ST0001_register";
  }
}
