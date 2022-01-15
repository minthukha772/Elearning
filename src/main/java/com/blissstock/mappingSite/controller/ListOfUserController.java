package com.blissstock.mappingSite.controller;

import com.blissstock.mappingSite.entity.AddAdmin;
import com.blissstock.mappingSite.entity.CourseInfo;
import com.blissstock.mappingSite.entity.UserInfo;
import com.blissstock.mappingSite.repository.CourseInfoRepository;
import com.blissstock.mappingSite.repository.UserAccountRepository;
import com.blissstock.mappingSite.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@Controller
public class ListOfUserController {

  @Autowired
  UserRepository userRepo;

  @Autowired
  UserAccountRepository userAccountRepo;

        model.addAttribute("aAllAdminList", aAllRecord);
        model.addAttribute("adminRegister",newAdmin);
        model.addAttribute("adminRole", adminRole);
        // System.out.println(servletContext.getContextPath());
        // System.out.println("Previous Path Info "+req.getRequestURL());
        return "AT0003_ListofAdminsScreen";
    }


  
    
    // @RequestMapping(value = "/teacher/course-list/{courseId}/student-list",method = RequestMethod.GET)
    @RequestMapping(value = "/teacher/student-list/{courseId}",method = RequestMethod.GET)
    public String ListOfStudentByTeacher(@PathVariable Long courseId,Model model)
    {
        // Long courseId = (long) 50004;
        
        CourseInfo course = courseInfoRepo.findById(courseId).get();
  @Autowired
  CourseInfoRepository courseInfoRepo;

  long courseID = 50004;

  @RequestMapping("/TeacherList")
  public String ListOfTeacher(Model model) {
    List<UserInfo> tAllRecord = userRepo.findByUserRoleI("Teacher");
    // System.out.println(tAllRecord);
    model.addAttribute("tAllTeacherList", tAllRecord);
    return "AT0003_ListofTeachersScreen";
  }

  @RequestMapping(value = "/StudentList", method = RequestMethod.GET)
  public String ListOfStudent(Model model) {
    System.out.println("Student Console");
    // List<UserInfo> sAllRecord = userRepo.findAll();
    List<UserInfo> sAllRecord = userRepo.findAll();
    // System.out.println("Student List Console"+sAllRecord);

    model.addAttribute("sAllStudentList", sAllRecord);
    // System.out.println(sAllRecord);
    // List<UserAccount> uAllRecord = userAccountRepo.findByUserRoleU("Student");
    // model.addAttribute("UAllStudentList", uAllRecord);
    return "AT0003_ListofStudentsScreen";
  }

  @RequestMapping("/AdminList")
  public String ListOfAdmin(Model model) {
    String adminRole = "Admin";
    System.out.println(adminRole);
    AddAdmin newAdmin = new AddAdmin();

    List<UserInfo> aAllRecord = userRepo.findByUserRoleI("Admin");
    model.addAttribute("aAllAdminList", aAllRecord);
    model.addAttribute("adminRegister", newAdmin);
    model.addAttribute("adminRole", adminRole);
    // System.out.println(servletContext.getContextPath());
    // System.out.println("Previous Path Info "+req.getRequestURL());
    return "AT0003_ListofAdminsScreen";
  }

  @RequestMapping(value = "/StudentListByT", method = RequestMethod.GET)
  public String ListOfStudentByTeacher(Model model) {
    Long courseID = (long) 50004;
    CourseInfo course = courseInfoRepo.findById(courseID).get();
    System.out.println("Course Joined" + course.getUserInfo());
    UserInfo userList = course.getUserInfo();
    System.out.println("User List" + userList);
    // List<UserInfo> stAllRecord = userRepo.findByCourseI();
    // System.out.println(stAllRecord);
    model.addAttribute("allStudentList", userList);

    List<UserInfo> tAllRecord = userRepo.findByUserRoleI("Teacher");
    System.out.println("Teacher List" + tAllRecord);
    // model.addAttribute("tAllTeacherList", tAllRecord);
    return "AT0003_ListofStudentsByT";
  }
}
