package com.blissstock.mappingSite.controller;

import com.blissstock.mappingSite.dto.TeacherRegisterDTO;
import com.blissstock.mappingSite.dto.UserRegisterDTO;
import com.blissstock.mappingSite.entity.UserInfo;
import com.blissstock.mappingSite.enums.UserRole;
import com.blissstock.mappingSite.exceptions.UserNotFoundException;
import com.blissstock.mappingSite.repository.PaymentRepository;
import com.blissstock.mappingSite.service.PaymentInfoServiceImpl;
import com.blissstock.mappingSite.service.UserService;
import com.blissstock.mappingSite.service.UserSessionService;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
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
  PaymentInfoServiceImpl paymentInfoServiceImpl;
  @Autowired
  UserService userService;

  private static Logger logger = LoggerFactory.getLogger(ProfileEditController.class);

  @GetMapping(path = {
      "/student/profile_edit",
      "/teacher/profile_edit",
      "admin/profile_edit/student/{id}",
      "admin/profile_edit/teacher/{id}"
  })
  public String editProfileView(
      Model model,
      @PathVariable(name = "id", required = false) Long id) {
    logger.info("Before Operation Retrieve On Table{}", id);
    String role;
    System.out.println("id is " + id);

    if (id == null) {
      id = userSessionService.getUserAccount().getAccountId();
      logger.info("Operation Retrieve Got ID {}", id);
    }

    UserInfo userInfo = userService.getUserInfoByID(id);
    logger.info("Operation Retrieve On Table UserInfo {}", userInfo);
    role = userInfo.getUserAccount().getRole().equals(UserRole.TEACHER.getValue())
        ? "teacher"
        : "student";

    UserRegisterDTO userRegisterDTO = UserRegisterDTO.fromUserInfo(userInfo);
    userRegisterDTO.setAcceptTerm(true);
    logger.info("Operation Save On DTO UserRegisterDTO", userRegisterDTO);
    model.addAttribute("userInfo", userRegisterDTO);
    model.addAttribute("task", "profile_edit");
    model.addAttribute("role", role);

    logger.info("Edit Profile is success", userRegisterDTO, role);

    return "ST0001_register";
  }

  @PostMapping(path = { "/student/profile_edit", "/admin/profile_edit/student/{id}" })
  public String editStudentProfile(
      Model model,
      @Valid @ModelAttribute("userInfo") UserRegisterDTO userInfo,
      BindingResult bindingResult,
      @RequestParam(value = "action", required = true) String action,
      HttpServletRequest httpServletRequest,
      @PathVariable(name = "id", required = false) Long id) {
    logger.info("Before Operation Save On Table {}", userInfo, bindingResult, action, httpServletRequest, id);
    String role = "student";
    Long uid = getUid(id);
    logger.info("Get User ID {}", uid);
    model.addAttribute("task", "profile_edit");
    model.addAttribute("role", role);
    model.addAttribute(
        "postAction",
        httpServletRequest.getRequestURL().toString());
    logger.info("Student Profile is editing", role, httpServletRequest);
    if (bindingResult.hasErrors()) {
      System.out.println(bindingResult.getFieldError());
      return "ST0001_register";
    }

    model.addAttribute("userInfo", userInfo);
    if (action.equals("Back")) {
      model.addAttribute("userInfo", userInfo);
      model.addAttribute("task", "profile_edit");
      model.addAttribute("role", "student");
      logger.info("Student Profile back to screen", userInfo);

      return "ST0001_register";
    }
    try {

      if (action.equals("submit")) {
        logger.info("update userinfo {}", uid);
        userService.updateUser(userInfo, uid);
        logger.info("student or admin profile is edited successfully", userService);
        if (userSessionService.getRole() == UserRole.STUDENT) {
          return "redirect:/student/profile/?message=profileEdit";
        } else {
          return "redirect:/admin/browse/profile/" + uid + "?message=profileEdit";
        }
      }

    } catch (Exception e) {
      e.getLocalizedMessage();
      if (userSessionService.getRole() == UserRole.STUDENT) {
        return "redirect:/student/profile/?message=profileEdit";
      } else {
        return "redirect:/admin/browse/profile/" + uid + "?message=profileEdit";
      }
    }

    // Information For Randering Confirm
    model.addAttribute("infoMap", userInfo.toMap());
    logger.info("Information For Randering Confirm", userInfo.toMap());
    System.out.println(userInfo.toMap());
    return "ST0001_register";
  }

  @PostMapping(path = { "/teacher/profile_edit", "/admin/profile_edit/teacher/{id}" })
  public String editTeacherProfile(
      Model model,
      @Valid @ModelAttribute("userInfo") TeacherRegisterDTO userInfo,
      BindingResult bindingResult,
      @RequestParam(value = "action", required = true) String action,
      HttpServletRequest httpServletRequest,
      @PathVariable(name = "id", required = false) Long id) {
    logger.info("Before Operation Save On Table{} ", userInfo, bindingResult, action, httpServletRequest, id);
    String role = "teacher";
    Long uid = getUid(id);
    logger.info("Get User ID {}", uid);

    model.addAttribute("task", "profile_edit");
    model.addAttribute("role", role);
    model.addAttribute(
        "postAction",
        httpServletRequest.getRequestURL().toString());
    logger.info("Teacher Profile is Editing {}", role, httpServletRequest);

    if (bindingResult.hasErrors()) {
      System.out.println(bindingResult.getFieldError());
      return "ST0001_register";
    }

    model.addAttribute("userInfo", userInfo);
    logger.warn("button value is :{}", action);
    if (action.equals("Back")) {
      model.addAttribute("userInfo", userInfo);
      model.addAttribute("task", "profile_edit");
      model.addAttribute("role", "teacher");
      logger.info("Student Profile back to screen", userInfo);

      return "ST0001_register";
    }
    try {
      if (action.equals("submit")) {
        logger.info("update userinfo {}", uid);
        userService.updateUser(userInfo, uid);
        logger.info("student or admin profile is edited successfully", userService);
        if (userSessionService.getRole() == UserRole.TEACHER) {
          return "redirect:/teacher/profile/?message=profileEdit";
        } else {
          return "redirect:/admin/browse/profile/" + uid + "?message=profileEdit";
        }

      }

    } catch (Exception e) {
      e.getLocalizedMessage();
      if (userSessionService.getRole() == UserRole.TEACHER) {
        return "redirect:/teacher/profile/?error=profileEdit";
      } else {
        return "redirect:/admin/browse/profile/" + uid + "?error=profileEdit";
      }

    }

    // Information For Randering Confirm
    model.addAttribute("infoMap", userInfo.toMap());
    // System.out.println(userInfo.toMap());
    logger.info("Information For Randering Confirm", userInfo.toMap());

    return "ST0001_register";
  }

  private Long getUid(Long id) {
    Long uid = 0L;
    UserRole role = userSessionService.getRole();
    if (role == UserRole.ADMIN || role == UserRole.SUPER_ADMIN) {
      uid = id;
    } else if (role == UserRole.TEACHER || role == UserRole.STUDENT) {
      uid = userSessionService.getUserAccount().getAccountId();
    } else {
      throw new RuntimeException("user authetication fail");
    }
    return uid;
  }

  @PostMapping(path = "/delete/paymentMethod/{paymentInfoId}")
  private String DeleteTeacherPaymentInfo(@PathVariable(name = "paymentInfoId", required = false) Long paymentInfoId,
      @ModelAttribute("id") Long Uid) {
    logger.info("Before Operation Save On Table {}", paymentInfoId, Uid);
    try {
      paymentInfoServiceImpl.deletePaymentInfo(paymentInfoId);
      logger.info("Operation Delete On Table PaymentInfo Table{}", paymentInfoServiceImpl);
    } catch (UserNotFoundException e) {
      logger.info("payment information not found");
      e.getLocalizedMessage();
    }
    System.out.println(Uid);
    if (userSessionService.getUserAccount().getRole().equals(UserRole.ADMIN.getValue())
        ||
        userSessionService.getUserAccount().getRole().equals(UserRole.SUPER_ADMIN.getValue())) {
      return "redirect:/admin/browse/profile/" + Uid;
    }
    return "redirect:/teacher/profile/";
  }
}
