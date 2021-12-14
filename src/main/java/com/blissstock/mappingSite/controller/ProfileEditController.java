package com.blissstock.mappingSite.controller;

import com.blissstock.mappingSite.dto.TeacherRegisterDTO;
import com.blissstock.mappingSite.dto.UserRegisterDTO;
import com.blissstock.mappingSite.entity.UserInfo;
import com.blissstock.mappingSite.enums.UserRole;
import com.blissstock.mappingSite.service.UserService;
import com.blissstock.mappingSite.service.UserSessionService;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ch.qos.logback.classic.Logger;

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
      "admin/profile_edit/student/{id}",
      "admin/profile_edit/teacher/{id}",
    }
  )
  public String editProfileView(
    Model model,
    @PathVariable(name = "id", required = false) Long id
  ) {
    String role;
    System.out.println("id is " + id);

    if (id != null) {
      //Id only present on admin side
      UserInfo userInfo = userService.getUserInfoByID(id);
      id = userInfo.getUserAccount().getId();
      role = "admin";
    } else {
      id = userSessionService.getUserAccount().getId();
      role =
        userSessionService.getRole() == UserRole.TEACHER
          ? "teacher"
          : "student";
    }

    UserInfo userInfo = userService.getUserInfoByID(id);
    UserRegisterDTO userRegisterDTO = UserRegisterDTO.fromUserInfo(userInfo);
    userRegisterDTO.setAcceptTerm(true);
    model.addAttribute("userInfo", userRegisterDTO);
    model.addAttribute("task", "profile_edit");
    model.addAttribute("role", role);

    return "ST0001_register";
  }

  @PostMapping(
    path = { "/student/profile_edit", "/admin/profile_edit/student/{id}" }
  )
  public String editStudentProfile(
    Model model,
    @Valid @ModelAttribute("userInfo") UserRegisterDTO userInfo,
    BindingResult bindingResult,
    @RequestParam(value = "action", required = true) String action,
    HttpServletRequest httpServletRequest,
    @PathVariable(name = "id", required = false) Long id
  ) {
    String role = "student";
    Long uid = getUid(id);
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
        userService.updateUser(userInfo, uid);
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
    path = { "/teacher/profile_edit", "/admin/profile_edit/teacher" }
  )
  public String editTeacherProfile(
    Model model,
    @Valid @ModelAttribute("userInfo") TeacherRegisterDTO userInfo,
    BindingResult bindingResult,
    @RequestParam(value = "action", required = true) String action,
    HttpServletRequest httpServletRequest,
    @PathVariable(name = "id", required = false) Long id
  ) {
    String role = "teacher";
    Long uid = getUid(id);
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
        userService.updateUser(userInfo, uid);
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

  private Long getUid(Long id) {
    Long uid = 0L;
    UserRole role = userSessionService.getRole();
    if (role == UserRole.ADMIN || role == UserRole.SUPER_ADMIN) {
      uid = id;
    } else if (role == UserRole.TEACHER || role == UserRole.STUDENT) {
      uid = userSessionService.getUserAccount().getId();
    } else {
      throw new RuntimeException("user authetication fail");
    }
    return uid;
  }
}
