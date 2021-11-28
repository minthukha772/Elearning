package com.blissstock.mappingSite.controller;

import com.blissstock.mappingSite.entity.Content;
import com.blissstock.mappingSite.entity.Syllabus;
import com.blissstock.mappingSite.enums.UserRole;
import com.blissstock.mappingSite.exceptions.InsuffientPermssionException;
import com.blissstock.mappingSite.service.SyllabusService;
import com.blissstock.mappingSite.service.UserSessionService;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CourseSyllabusController {

  @Autowired
  SyllabusService syllabusService;

  @Autowired
  UserSessionService userSessionService;

  @GetMapping(
    path = { "/teacher/course_syllabus/{id}", "/admin/course_syllabus/{id}" }
  )
  public String contextView(
    Model model,
    @PathVariable("id") long id,
    HttpServletRequest httpServletRequest
  ) {
    //TODO replace data from repository
    //For the purpose of display
    List<Syllabus> syllabusList = syllabusService.getAllSyllabus(id);

    model.addAttribute(
      "deleteAction",
      httpServletRequest.getRequestURL().toString() + "/delete"
    );
    model.addAttribute("syllabusList", syllabusList);

    model.addAttribute("postAction", httpServletRequest.getRequestURL());

    return "course_context";
  }

  @PostMapping(
    path = { "/teacher/course_syllabus/{id}", "/admin/course_syllabus/{id}" }
  )
  public String addSyllabus(
    Model model,
    @ModelAttribute Syllabus syllabus,
    HttpServletRequest httpServletRequest
  ) {
    UserRole userRole = userSessionService.getRole();

    System.out.println(syllabus.getSyllabusId());
    System.out.println(syllabus.getTitle());
    for(Content content: syllabus.getContent()){
      System.out.println(content.getContentId());
      System.out.println(content.getContent());
    }

    try {
      //syllabusService.deleteSyllabus(id);
      syllabusService.addSyllabus(syllabus);
    } catch(InsuffientPermssionException e){
      //
    }
    catch (Exception e) {
      //
    }

    /* 
    if(userRole==UserRole.ADMIN){

      return "redirect:/teacher/course_syllabus/"+id+"/";
    }else if(userRole == UserRole.TEACHER){
      return "redirect:/teacher/course_syllabus/"+id+"/";
    }
     */

    return "redirect:" + httpServletRequest.getRequestURI();
  }

  @PostMapping(
    path = {
      "/teacher/course_syllabus/{id}/delete",
      "/admin/course_syllabus/{id}/delete",
    }
  )
  public String deleteSyllabus(
    Model model,
    @PathVariable("id") long id,
    HttpServletRequest httpServletRequest
  ) {
    UserRole userRole = userSessionService.getRole();
    try {
      syllabusService.deleteSyllabus(id);
    } catch (Exception e) {}

    /* 
    if(userRole==UserRole.ADMIN){

      return "redirect:/teacher/course_syllabus/"+id+"/";
    }else if(userRole == UserRole.TEACHER){
      return "redirect:/teacher/course_syllabus/"+id+"/";
    }
     */

    return "course_context";
  }
}
