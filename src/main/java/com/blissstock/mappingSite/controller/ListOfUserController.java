package com.blissstock.mappingSite.controller;

<<<<<<< HEAD
import com.blissstock.mappingSite.model.AddAdmin;
import com.blissstock.mappingSite.entity.CourseInfo;
import com.blissstock.mappingSite.entity.UserInfo;
import com.blissstock.mappingSite.entity.JoinCourseUser;
import com.blissstock.mappingSite.repository.CourseInfoRepository;
import com.blissstock.mappingSite.repository.UserAccountRepository;
import com.blissstock.mappingSite.repository.UserRepository;
import com.blissstock.mappingSite.repository.JoinCourseUserRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
=======
import com.blissstock.mappingSite.entity.AddAdmin;
import com.blissstock.mappingSite.entity.CourseInfo;
import com.blissstock.mappingSite.entity.UserInfo;
import com.blissstock.mappingSite.repository.CourseInfoRepository;
import com.blissstock.mappingSite.repository.UserAccountRepository;
import com.blissstock.mappingSite.repository.UserRepository;
>>>>>>> de9d4e27d27b5edff44f1328c1783237d3565af7

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

<<<<<<< HEAD
import java.util.ArrayList;
=======
>>>>>>> de9d4e27d27b5edff44f1328c1783237d3565af7
import java.util.List;

@Controller
public class ListOfUserController {
<<<<<<< HEAD

    private static final Logger logger = LoggerFactory.getLogger(ListOfUserController.class);
    
=======
 /*    
>>>>>>> de9d4e27d27b5edff44f1328c1783237d3565af7
    @Autowired
    UserRepository userRepo;
    
    @Autowired
    UserAccountRepository userAccountRepo;
    
    @Autowired
    CourseInfoRepository courseInfoRepo;
<<<<<<< HEAD

    @Autowired
    JoinCourseUserRepository joinCourseUserRepo;
=======
>>>>>>> de9d4e27d27b5edff44f1328c1783237d3565af7
    
    long courseID = 50004;
    
    @RequestMapping("/TeacherList")
    public String ListOfTeacher(Model model)
    {
<<<<<<< HEAD
        List<UserInfo> tAllRecord = userRepo.findByUserRoleI("ROLE_TEACHER");
        // System.out.println(tAllRecord);
        logger.info("Teacher List of Mapping Site {}",tAllRecord);
=======
        List<UserInfo> tAllRecord = userRepo.findByUserRoleI("Teacher");
        // System.out.println(tAllRecord);
>>>>>>> de9d4e27d27b5edff44f1328c1783237d3565af7
        model.addAttribute("tAllTeacherList", tAllRecord);
        return "AT0003_ListofTeachersScreen";
    }
    @RequestMapping(value = "/StudentList",method = RequestMethod.GET)
    public String ListOfStudent(Model model)
    {
        System.out.println("Student Console");
        // List<UserInfo> sAllRecord = userRepo.findAll();
<<<<<<< HEAD
        List<UserInfo> sAllRecord = userRepo.findByUserRoleI("ROLE_STUDENT");
        // System.out.println("Student List Console"+sAllRecord);
        
        logger.info("Student List of Mapping Site {}",sAllRecord);
=======
        List<UserInfo> sAllRecord = userRepo.findByUserRoleI("Student");
        // System.out.println("Student List Console"+sAllRecord);
        
>>>>>>> de9d4e27d27b5edff44f1328c1783237d3565af7
        model.addAttribute("sAllStudentList", sAllRecord);
        // System.out.println(sAllRecord);
        // List<UserAccount> uAllRecord = userAccountRepo.findByUserRoleU("Student");
        // model.addAttribute("UAllStudentList", uAllRecord);
        return "AT0003_ListofStudentsScreen";
    }
    
    @RequestMapping("/AdminList")
    public String ListOfAdmin(Model model)
    {
        String adminRole = "Admin";
        System.out.println(adminRole);
        AddAdmin newAdmin = new AddAdmin();
        
<<<<<<< HEAD
        List<UserInfo> aAllRecord = userRepo.findByUserRoleI("ROLE_ADMIN");

        logger.info("Admin List of Mapping Site {}",aAllRecord);

=======
        List<UserInfo> aAllRecord = userRepo.findByUserRoleI("Admin");
>>>>>>> de9d4e27d27b5edff44f1328c1783237d3565af7
        model.addAttribute("aAllAdminList", aAllRecord);
        model.addAttribute("adminRegister",newAdmin);
        model.addAttribute("adminRole", adminRole);
        // System.out.println(servletContext.getContextPath());
        // System.out.println("Previous Path Info "+req.getRequestURL());
        return "AT0003_ListofAdminsScreen";
    }
    
    @RequestMapping(value = "/StudentListByT",method = RequestMethod.GET)
    public String ListOfStudentByTeacher(Model model)
    {
        Long courseID = (long) 50004;
<<<<<<< HEAD
        
        CourseInfo course = courseInfoRepo.findById(courseID).get();

        logger.info("Course ID {}","Course Info {}",courseID,course);

        List<JoinCourseUser> joinCourseUser = course.getJoin();
        // List<UserInfo> userList = new ArrayList<>();
        List<UserInfo> userList = new ArrayList<UserInfo>();
        // List<UserInfo> userList = joinCourseUser.getUserInfo();
        for(JoinCourseUser joinCU:joinCourseUser)
        {
            UserInfo userinfo = joinCU.getUserInfo();
            userList.add(userinfo);
        }
        // List<JoinCourseUser> joinCourseUser = joinCourseUserRepo.findByCourseID(courseID);
        
        // System.out.println("Course Joined"+course.getUserInfo());
        // List<UserInfo> userList=course.getUserInfo();
        logger.info("Course ID {}","Student List {}",courseID,userList);

        System.out.println("User List"+userList);
        // List<UserInfo> stAllRecord = userRepo.findByCourseI();
        // System.out.println(stAllRecord);
        model.addAttribute("courseInfo", course);
        model.addAttribute("allStudentList", userList);
        
        List<UserInfo> tAllRecord = userRepo.findByUserRoleI("ROLE_TEACHER");
        System.out.println("Teacher List"+tAllRecord);
        // model.addAttribute("tAllTeacherList", tAllRecord);
        return "AT0003_ListofStudentsByT";
    }
=======
        CourseInfo course = courseInfoRepo.findById(courseID).get();
        System.out.println("Course Joined"+course.getUserInfo());
        List<UserInfo> userList=course.getUserInfo();
        System.out.println("User List"+userList);
        // List<UserInfo> stAllRecord = userRepo.findByCourseI();
        // System.out.println(stAllRecord);
        model.addAttribute("allStudentList", userList);
        
        List<UserInfo> tAllRecord = userRepo.findByUserRoleI("Teacher");
        System.out.println("Teacher List"+tAllRecord);
        // model.addAttribute("tAllTeacherList", tAllRecord);
        return "AT0003_ListofStudentsByT";
    } */
>>>>>>> de9d4e27d27b5edff44f1328c1783237d3565af7
}
