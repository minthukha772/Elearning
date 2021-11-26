package com.blissstock.mappingSite.controller;

import com.blissstock.mappingSite.enums.UserRole;
import com.blissstock.mappingSite.service.UserSessionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeRedirectController {

  @Autowired
  UserSessionService userSessionService;

  @RequestMapping("/home")
  public String redirectHome(Model model) {
    UserRole userRole= userSessionService.getRole();
    if(userRole == null){
        //redirect to 
        return "redirect:/";
    }
    if(userRole == UserRole.ADMIN || userRole == UserRole.SUPER_ADMIN){
        //TODO redirect to Admin Top Page
        return "redirect:/";
    }
    if(userRole == UserRole.STUDENT || userRole == UserRole.TEACHER){
       //TODO redirect to Student/Teacher Top Page
       return "redirect:/";
    }
    return "redirect:/";
  }
}
