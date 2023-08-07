package com.blissstock.mappingSite.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import com.blissstock.mappingSite.entity.Syllabus;
import com.blissstock.mappingSite.enums.UserRole;
import com.blissstock.mappingSite.exceptions.CourseNotFoundException;
import com.blissstock.mappingSite.exceptions.InsuffientPermssionException;
import com.blissstock.mappingSite.service.SyllabusService;
import com.blissstock.mappingSite.service.UserSessionService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
public class CourseSyllabusController {

  @Autowired
  SyllabusService syllabusService;

  @Autowired
  UserSessionService userSessionService;

  private static Logger logger = LoggerFactory.getLogger(
      CourseSyllabusController.class);

  @ExceptionHandler(CourseNotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public ResponseEntity<Object> handleResourceNotFoundException() {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("");
  }

  @GetMapping(path = {
      "/teacher/course_syllabus/course_id/{id}",
      "/admin/course_syllabus/course_id/{id}",
  })
  public String contextView(
      Model model,
      @PathVariable("id") long id,
      HttpServletRequest httpServletRequest)
      throws CourseNotFoundException {

    try {
      Long userID = getUid();
      UserRole userRole = getRole();
      // For the purpose of display
      logger.info("Called course_context with parameter(course_id={})", id);
      logger.info("user_id: {}, role: {}", userID, userRole);
      logger.info("Initiate to Operation Retrieve Table syllabus by Query: None");
      // Sort by title
      List<Syllabus> syllabusList = syllabusService.getAllSyllabus(id).stream()
          .sorted((p1, p2) -> p1.getTitle().compareTo(p2.getTitle())).collect(Collectors.toList());

      if (syllabusList != null)
        logger.info("Operation Retrieve Table syllabus by Query: None Result syllabus count={} | Success",
            syllabusList.size());
      else
        logger.warn("No syllabus in course_id={}", id);

      model.addAttribute(
          "deleteAction",
          httpServletRequest.getRequestURL().toString() + "/delete");
      model.addAttribute("syllabusList", syllabusList);

      model.addAttribute("postAction", httpServletRequest.getRequestURL());
      logger.info("Called course_context with parameter(course_id={}) | Success", id);
      return "course_context";
    } catch (Exception e) {
      logger.error(e.getLocalizedMessage());
      return "500";
    }
  }

  // syllabus update
  @GetMapping(path = {
      "/syllabus_details/course_id/{id}"
  })
  public String syllabusDetails(
      Model model,
      @PathVariable("id") long id,
      HttpServletRequest httpServletRequest)
      throws CourseNotFoundException {
    try {
      Long userID = getUid();
      UserRole userRole = getRole();
      // For the purpose of display
      logger.info("Called syllabus_details with parameter(course_id={})", id);
      logger.info("user_id: {}, role: {}", userID, userRole);
      logger.info("Initiate to Operation Retrieve Table syllabus by Query: None");

      // Sort by title
      List<Syllabus> syllabusList = syllabusService.getAllSyllabus(id).stream()
          .sorted((p1, p2) -> p1.getTitle().compareTo(p2.getTitle())).collect(Collectors.toList());

      if (syllabusList != null)
        logger.info("Operation Retrieve Table syllabus by Query: None Result syllabus count={} | Success",
            syllabusList.size());
      else
        logger.warn("No syllabus in course_id={}", id);

      // model.addAttribute(
      // "deleteAction",
      // httpServletRequest.getRequestURL().toString() + "/delete");
      model.addAttribute("syllabusList", syllabusList);

      // model.addAttribute("postAction", httpServletRequest.getRequestURL());
      logger.info("Called syllabus_details with parameter(course_id={}) | Success", id);
      return "syllabus_details";
    } catch (Exception e) {
      logger.error(e.getLocalizedMessage());
      return "500";
    }
  }

  @PostMapping(path = {
      "/teacher/course_syllabus/course_id/{id}",
      "/admin/course_syllabus/course_id/{id}",
  })
  public String addSyllabus(
      Model model,
      @ModelAttribute Syllabus syllabus,
      @PathVariable(name = "id") Long id,
      HttpServletRequest httpServletRequest) {
    try {
      Long userID = getUid();
      UserRole userRole = getRole();

      logger.info("Called {} with parameter(course_id={})", httpServletRequest.getRequestURI(), id);
      logger.info("user_id: {}, role: {}", userID, userRole);
      logger.info("Initiate to Operation Insert/Update Table syllabus Data course_id={}, syllabus={}", id, syllabus);

      // syllabusService.deleteSyllabus(id);
      syllabusService.addSyllabus(syllabus, id);
      logger.info("Operation Insert/Update Table syllabus Data course_id={}, syllabus={} | Success", id, syllabus);
    } catch (InsuffientPermssionException e) {
      logger.error(e.getLocalizedMessage());
      //
    } catch (Exception e) {
      logger.error(e.getLocalizedMessage());
      //
    }

    /*
     * if(userRole==UserRole.ADMIN){
     * 
     * return "redirect:/teacher/course_syllabus/"+id+"/";
     * }else if(userRole == UserRole.TEACHER){
     * return "redirect:/teacher/course_syllabus/"+id+"/";
     * }
     */

    return "redirect:" + httpServletRequest.getRequestURI();
  }

  @DeleteMapping(path = {
      "/teacher/course_syllabus/course_id/{id}",
      "/admin/course_syllabus/course_id/{id}",
  })
  public ResponseEntity<Object> deleteSyllabus(
      Model model,
      Long id,
      HttpServletRequest httpServletRequest) {

    try {
      Long userID = getUid();
      UserRole userRole = getRole();

      logger.info("Called {} with parameter(course_id={})", httpServletRequest.getRequestURI(), id);
      logger.info("user_id: {}, role: {}", userID, userRole);
      logger.info("Initiate to Operation Delete Table syllabus by syllabus_id={}", id);

      syllabusService.deleteSyllabus(id);
      logger.info("Operation Delete Table syllabus by syllabus_id={} | Success", id);
    } catch (Exception e) {
      e.printStackTrace();
      logger.error(e.getLocalizedMessage());
      return ResponseEntity.status(HttpStatus.OK).body("operation success");
    }

    /*
     * if(userRole==UserRole.ADMIN){
     * 
     * return "redirect:/teacher/course_syllabus/"+id+"/";
     * }else if(userRole == UserRole.TEACHER){
     * return "redirect:/teacher/course_syllabus/"+id+"/";
     * }
     */

    logger.info("Called {} with parameter(course_id={}) | Success", httpServletRequest.getRequestURI(), id);
    return ResponseEntity.status(HttpStatus.OK).body("operation success");
  }

  private Long getUid() {
    return userSessionService.getUserAccount().getAccountId();
  }

  private UserRole getRole() {
    return userSessionService.getRole();
  }

}
