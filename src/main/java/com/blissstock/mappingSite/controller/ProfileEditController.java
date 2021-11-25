package com.blissstock.mappingSite.controller;

import com.blissstock.mappingSite.dto.TeacherRegisterDTO;
import com.blissstock.mappingSite.dto.UserRegisterDTO;
import com.blissstock.mappingSite.entity.UserAccount;
import com.blissstock.mappingSite.entity.UserInfo;
import com.blissstock.mappingSite.enums.UserRole;
import com.blissstock.mappingSite.service.UserService;
import com.blissstock.mappingSite.service.UserSessionService;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
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
  public String editProfileView(
    Model model,
    @PathVariable(name = "id", required = false) Long id
  ) {
    String email, role, postAction;
    System.out.println("id is " + id);

    if (id != null) {
      //Id only present on admin side
      UserInfo userInfo = userService.getUserInfoByID(id);
      email = userInfo.getUserAccount().getMail();
      role = userInfo.getUserAccount().getRole().getValue();
      postAction = "/admin/profile_edit/" + role;
    } else {
      email = userSessionService.getEmail();
      role = userSessionService.getRole().getValue();
      postAction = "/" + role + "/profile_edit/";
    }
    System.out.println(postAction);

    UserRegisterDTO userInfo;
    userInfo = userService.getUserByEmail(email);
    userInfo.setAcceptTerm(true);
    model.addAttribute("userInfo", userInfo);
    model.addAttribute("task", "profile_edit");
    model.addAttribute("role", role);
    model.addAttribute("postAction", postAction);

    return "ST0001_register";
  }

  @PostMapping(
    path = { "/student/profile_edit", "/admin/profile_edit/student" }
  )
  public String editStudentProfile(
    Model model,
    @Valid @ModelAttribute("userInfo") UserRegisterDTO userInfo,
    BindingResult bindingResult,
    @RequestParam(value = "action", required = true) String action,
    HttpServletRequest httpServletRequest
  ) {
    String role = "student";
    model.addAttribute("task", "profile_edit");
    model.addAttribute("role", role);
    model.addAttribute(
      "postAction",
      httpServletRequest.getRequestURL().toString()
    );

    if (bindingResult.hasErrors()) {
      return "ST0001_register";
    }

    model.addAttribute("userInfo", userInfo);
    try {
      if (action.equals("submit")) {
        userService.updateUser(userInfo);
      }
      //TODO redirect to complete page
    } catch (Exception e) {
      System.out.println(e);
    }

    //Information For Randering Confirm
    model.addAttribute("infoMap", userInfo.toMap());
    System.out.println(userInfo.toMap());
    return "ST0001_register";
  }

  @PostMapping(
    path = { "/student/profile_edit", "/admin/profile_edit/teacher" }
  )
  public String editTeacherProfile(
    Model model,
    @Valid @ModelAttribute("userInfo") TeacherRegisterDTO userInfo,
    BindingResult bindingResult,
    @RequestParam(value = "action", required = true) String action,
    HttpServletRequest httpServletRequest
  ) {
    String role = "teacher";
    model.addAttribute("task", "profile_edit");
    model.addAttribute("role", role);
    model.addAttribute(
      "postAction",
      httpServletRequest.getRequestURL().toString()
    );

    if (bindingResult.hasErrors()) {
      return "ST0001_register";
    }

    model.addAttribute("userInfo", userInfo);
    try {
      if (action.equals("submit")) {
        userService.updateUser(userInfo);
      }
      //TODO redirect to complete page
    } catch (Exception e) {
      System.out.println(e);
    }

    //Information For Randering Confirm
    model.addAttribute("infoMap", userInfo.toMap());
    System.out.println(userInfo.toMap());
    return "ST0001_register";
  }
}
