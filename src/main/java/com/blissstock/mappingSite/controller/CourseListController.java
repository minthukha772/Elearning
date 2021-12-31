package com.blissstock.mappingSite.controller;

import org.checkerframework.checker.guieffect.qual.UI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.blissstock.mappingSite.entity.CourseInfo;
import com.blissstock.mappingSite.entity.CourseTime;
import com.blissstock.mappingSite.entity.JoinCourseUser;
import com.blissstock.mappingSite.entity.UserAccount;
import com.blissstock.mappingSite.entity.UserInfo;
import com.blissstock.mappingSite.repository.CourseRepository;
import com.blissstock.mappingSite.repository.JoinCourseUserRepository;
import com.blissstock.mappingSite.repository.UserAccountRepository;
import com.blissstock.mappingSite.repository.UserInfoRepositoryTwo;




@Controller

public class CourseListController {

    @Autowired
    private CourseRepository courseRepo;

    @Autowired
    private UserAccountRepository userAccountRepo;

    @Autowired
    private UserInfoRepositoryTwo userInfoRepo;

    @Autowired
    private JoinCourseUserRepository joinCourseUserRepo;
    
    @RequestMapping("/guest-course-list")
    private String getCourseListGuest(Model model){

        List<CourseInfo> allList = courseRepo.findAll();
        System.out.print("Here is : " + allList.get(0).getAboutCourse());
        model.addAttribute("courseList", allList);
        return "CM0002_CourseListGuest";
    }

    // @GetMapping("/course-list-guest/{id}")
    // private String courseDetails(@PathVariable("id") Long courseId){
    //     Optional<CourseInfo> courseInfo = courseRepo.findById(courseId);
    //     System.out.println("testing" + courseInfo.get().getAboutCourse());
    //     return "helloworld";
    // }

    @RequestMapping("/course-list")
    private String getCourseList(){

        // List<CourseInfo> allList = courseRepo.findAll();
        return "CM0002_CourseListGuest";
        // model.addAttribute("courseList", allList);
    //     return "CM0002_CourseListGuest";
    }



    @RequestMapping("/student-course-list")
    private String getCourseListStudent(){
        return "CM0002_CourseListStudent";
    }

    @RequestMapping("/teacher-course-list")
    private String getCourseListTeacher(Model model, Principal principal){
        UserAccount userAccount = userAccountRepo.findByMail(principal.getName());

        System.out.println("Thiha sout yuu" + principal.getName());
        UserInfo userInfo = userInfoRepo.findByUserAccount(userAccount);
        List<JoinCourseUser> joinCourseList = joinCourseUserRepo.findByUserInfo(userInfo);
        List<CourseInfo> courseList = new ArrayList<>();
        for(JoinCourseUser joinCourseUser : joinCourseList){
            courseList.add(joinCourseUser.getCourseInfo());
        }
        if(courseList.size() == 0){
            model.addAttribute("something", false);
        }else {
            model.addAttribute("something", true);
        }
        model.addAttribute("userInfo", userInfo);
        model.addAttribute("courseList", courseList);
        return "CM0002_CourseListTeacher";
    }

    @RequestMapping(value = "/courseSearch", method = RequestMethod.POST)
    private String searchCourse(@RequestParam("course-name") String courseName, 
                                @RequestParam("teacher-name") String teacherName,
                                @RequestParam("from-date") String fromDate,
                                @RequestParam("to-date") String toDate, Model model )
    {
        DateFormat outputFormatter = new SimpleDateFormat("yyyy-MM-dd");
        String searchResultError = "Your search result exceed limit. We will show 1400 results";

        //find by course name
        if(courseName!="" && teacherName=="" && fromDate=="" && toDate==""){
            List<CourseInfo> course = courseRepo.findByCourseName(courseName);
            if(course==null){
                model.addAttribute("noCourse","There is no course with the given name");
                model.addAttribute("resultSize", course.size());
                return "CM0002_CourseListGuest";
            }else{
                model.addAttribute("courseList", course);
                model.addAttribute("resultSize", course.size());
                
            }

            if(course.size() > 1400){
                model.addAttribute("searchResultError", searchResultError);
            }
            
        }

        //Find by teacher name
        else if(courseName=="" && teacherName!="" && fromDate=="" && toDate==""){

            List<UserAccount> userAccountList = userAccountRepo.findByRole("teacher");
            List<UserInfo> userInfoList = new ArrayList<UserInfo>();
            

            
            List<CourseInfo> courseList = new ArrayList<CourseInfo>();
     

            for(UserAccount userAccount : userAccountList){
                UserInfo uInfo = userInfoRepo.findByNameAndAccount(teacherName, userAccount.getAccountId());
                userInfoList.add(uInfo);
            }

          
            for(UserInfo userInfo : userInfoList){
                List<JoinCourseUser> joinCourseUserList = joinCourseUserRepo.findByUserInfo(userInfo);

                for(JoinCourseUser joinCourseUser : joinCourseUserList){
                    courseList.add(joinCourseUser.getCourseInfo());
                }
               
            }

            if(courseList.size()==0){
                model.addAttribute("noCourse","There is no course with the given name");
                model.addAttribute("resultSize", courseList.size());
                return "CM0002_CourseListGuest";
            }else{
                model.addAttribute("courseList", courseList);
                model.addAttribute("resultSize", courseList.size());
            }
            
        }


        //Find by course name and teacher name
        else if(courseName!="" && teacherName!="" && fromDate=="" && toDate==""){
            List<UserAccount> userAccountList = userAccountRepo.findByRole("teacher");
            List<UserInfo> userInfoList = new ArrayList<UserInfo>();
              
            List<CourseInfo> courseList = new ArrayList<CourseInfo>();
     

            for(UserAccount userAccount : userAccountList){
                UserInfo uInfo = userInfoRepo.findByNameAndAccount(teacherName, userAccount.getAccountId());
                userInfoList.add(uInfo);
            }

          
            for(UserInfo userInfo : userInfoList){
                // List<CourseInfo> cInfo = courseRepo.findByUserInfo(userInfo);
                List<JoinCourseUser> joinCourseUserList = joinCourseUserRepo.findByUserInfo(userInfo);
               
                for(JoinCourseUser joinCourseUser : joinCourseUserList){
                    if(joinCourseUser.getCourseInfo().getCourseName().equals(courseName)){
                        courseList.add(joinCourseUser.getCourseInfo());
                    }
                }

            }
            
            if(courseList.size()==0){
                model.addAttribute("noCourse","There is no course with the given name");
                model.addAttribute("resultSize", courseList.size());
                return "CM0002_CourseListGuest";
            }else{
                model.addAttribute("courseList", courseList);
                model.addAttribute("resultSize", courseList.size());
            }
            
        }

        //Find by course name, teacher name and start date
        else if(courseName!="" && teacherName!="" && fromDate!="" && toDate==""){
            List<UserAccount> userAccountList = userAccountRepo.findByRole("teacher");
            List<UserInfo> userInfoList = new ArrayList<UserInfo>();
              
            List<CourseInfo> courseList = new ArrayList<CourseInfo>();
     

            for(UserAccount userAccount : userAccountList){
                UserInfo uInfo = userInfoRepo.findByNameAndAccount(teacherName, userAccount.getAccountId());
                userInfoList.add(uInfo);
            }

            
            for(UserInfo userInfo : userInfoList){
                
                List<CourseInfo> cInfo = new ArrayList<>();
                List<JoinCourseUser> joinCourseUserList = joinCourseUserRepo.findByUserInfo(userInfo);
               
                for(JoinCourseUser joinCourseUser : joinCourseUserList){
                    cInfo.add(joinCourseUser.getCourseInfo());
                }
                
                for(CourseInfo courseInfo1 : cInfo){
                    
                    
                    String output = outputFormatter.format(courseInfo1.getStartDate());
                    if(courseInfo1.getCourseName().equals(courseName) && output.equals(fromDate)){
                        courseList.add(courseInfo1);
                    }

                }
            }
            
            if(courseList.size()==0){
                model.addAttribute("noCourse","There is no course with the given name");
                return "CM0002_CourseListGuest";
            }else{
                model.addAttribute("courseList", courseList);
            }
            
        }

        //Find by Course name, Teacher name, Start Date and End Date
        else if(courseName!="" && teacherName!="" && fromDate!="" && toDate!=""){
            List<UserAccount> userAccountList = userAccountRepo.findByRole("teacher");
            List<UserInfo> userInfoList = new ArrayList<UserInfo>();
              
            List<CourseInfo> courseList = new ArrayList<CourseInfo>();
     

            for(UserAccount userAccount : userAccountList){
                UserInfo uInfo = userInfoRepo.findByNameAndAccount(teacherName, userAccount.getAccountId());
                userInfoList.add(uInfo);
            }

            
            for(UserInfo userInfo : userInfoList){
                
                List<CourseInfo> cInfo = new ArrayList<>();
                List<JoinCourseUser> joinCourseUserList = joinCourseUserRepo.findByUserInfo(userInfo);
               
                for(JoinCourseUser joinCourseUser : joinCourseUserList){
                    cInfo.add(joinCourseUser.getCourseInfo());
                }
               
                
                for(CourseInfo courseInfo1 : cInfo){
                    
                    
                    String startDate = outputFormatter.format(courseInfo1.getStartDate());
                    String endDate = outputFormatter.format(courseInfo1.getEndDate());
                    if(courseInfo1.getCourseName().equals(courseName) && startDate.equals(fromDate) && endDate.equals(toDate)){
                        courseList.add(courseInfo1);
                    }

                }
            }
            
            if(courseList.size()==0){
                model.addAttribute("noCourse","There is no course with the given name");
                return "CM0002_CourseListGuest";
            }else{
                model.addAttribute("courseList", courseList);
            }
            
        }

       
        
       
        return "CM0002_CourseListGuest";
    }

    @PostMapping("/courseFilter")
    private String courseFilter(
        Model model,@ModelAttribute("level") String levelList,
                                @ModelAttribute("category") String categoryList,
                                @ModelAttribute("method") String methodList
    ){
        System.out.println(levelList);  
        System.out.println(categoryList);
        System.out.println(methodList);
        if(!levelList.equals("") && categoryList.equals("") && methodList.equals("")){
            List<CourseInfo> courseList = courseRepo.findByLevel(levelList);
            model.addAttribute("courseList", courseList); 
            model.addAttribute("level", levelList);
           
        }

        else if(levelList.equals("") && !categoryList.equals("") && methodList.equals("")){
            List<CourseInfo> courseList = courseRepo.findByCategory(categoryList);
            model.addAttribute("courseList", courseList); 
            model.addAttribute("category", categoryList);
            
        }

        else if(levelList.equals("") && categoryList.equals("") && !methodList.equals("")){
            List<CourseInfo> courseList = courseRepo.findByClassType(methodList);
            model.addAttribute("courseList", courseList); 
            model.addAttribute("classType", methodList);
            
        }

        else if(!levelList.equals("") && !categoryList.equals("") && methodList.equals("")){
            List<CourseInfo> courseList = courseRepo.findByLevelAndCategory(levelList, categoryList);
            model.addAttribute("courseList", courseList); 
            model.addAttribute("category", categoryList);
            model.addAttribute("level", levelList);
            
        }

        else if(!levelList.equals("") && categoryList.equals("") && !methodList.equals("")){
            List<CourseInfo> courseList = courseRepo.findByLevelAndClassType(levelList, methodList);
            model.addAttribute("courseList", courseList); 
            model.addAttribute("classType", methodList);
            model.addAttribute("level", levelList);
            
        }

        else if(levelList.equals("") && !categoryList.equals("") && !methodList.equals("")){
            List<CourseInfo> courseList = courseRepo.findByCategoryAndClassType(categoryList, methodList);
            model.addAttribute("courseList", courseList); 
            model.addAttribute("classType", methodList);
            model.addAttribute("category", categoryList);
            
        }

        else if(!levelList.equals("") && !categoryList.equals("") && !methodList.equals("")){
            List<CourseInfo> courseList = courseRepo.findByLevelAndCategoryAndClassType(levelList, categoryList,methodList);
            model.addAttribute("courseList", courseList); 
            model.addAttribute("classType", methodList);
            model.addAttribute("category", categoryList);
            model.addAttribute("level", levelList);
            
        }

        else{
            List<CourseInfo> courseList = courseRepo.findAll();
            model.addAttribute("courseList", courseList);
        }



  
        return "CM0002_CourseListGuest";
    }






    @RequestMapping("/courseInfo")
    private String courseRegister(Model model){
       
        CourseInfo courseInfo = new CourseInfo();
        model.addAttribute("course", courseInfo);
        return "test";
    } 

    @RequestMapping("/userInfo")
    private String userInfoRegister(Model model){
        UserInfo userInfo = new UserInfo();
        model.addAttribute("userInfo", userInfo);
        return "testUserInfo";
    }

    @RequestMapping("/userAccount")
    private String userAccountRegister(Model model){
        UserAccount userAccount = new UserAccount();
        userAccount.setMailVerified(true);
        model.addAttribute("userAccount", userAccount);
        return "userAccount";
    }

    @PostMapping("/coursePost")
    private String coursePost(@ModelAttribute("course") CourseInfo cInfo){
        
        courseRepo.save(cInfo);
        return "helloworld";
    }

    @PostMapping("/userInfoPost")
    private String userInfoPost(@ModelAttribute("userInfo") UserInfo uInfo){
        
        userInfoRepo.save(uInfo);
        return "helloworld";
    }

    @PostMapping("/userAccountPost")
    private String userAccountPost(@ModelAttribute("userAccount") UserAccount userAccount){
        userAccountRepo.save(userAccount);
        return "helloworld";
    }

    @RequestMapping("/testUserInfo")
    private String userInfoUpload(){
        return "testUserInfo";
    }

    @RequestMapping("/accToUserInfoAdd")
    private String accToUserInfoAdd(){
        return "accToUserInfo";
    }

    @RequestMapping("/userAndCourseAdd")
    private String userAndCourseAdd(){
        return "userInfoAndCourseInfo";
    }

    @PostMapping("/accToUserInfo")
    private String accToUserInfo(@ModelAttribute("uid") Long uid, @ModelAttribute("accountId") Long accId){
        UserInfo userInfo = userInfoRepo.findById(uid).get();
        UserAccount userAccount = userAccountRepo.findById(accId).get();
        userInfo.setUserAccount(userAccount);
        userInfoRepo.save(userInfo);
        return "helloworld";
    }

    // @PostMapping("/userAndCourse")
    // private String userAndCourse(@ModelAttribute("uid") Long uid, @ModelAttribute("courseId") Long courseId){
        
    //     CourseInfo courseInfo = courseRepo.findById(courseId).get();
    //     UserInfo userInfo = userInfoRepo.findById(uid).get();
    //     List<CourseInfo> courseInfoList = userInfo.getCourseInfo();
    //     List<UserInfo> userInfoList = courseInfo.getUserInfo();
    //     courseInfoList.add(courseInfo);
    //     userInfoList.add(userInfo);
    //     userInfo.setCourseInfo(courseInfoList);
    //     userInfoRepo.save(userInfo);
    //     courseInfo.setUserInfo(userInfoList);
    //     courseRepo.save(courseInfo);
    //     return "helloworld";
    // }

    // @RequestMapping("/hello")
    // private String helloTest(){
    //     Date date = new Date(); 
	
        
    //     UserInfo helloUser = userInfoRepo.findById((long) 13).get();
    //     for(int i=0; i<helloUser.getCourseInfo().size(); i++){
    //         System.out.println("YayYay" + helloUser.getCourseInfo().get(i).getCourseName());
    //     }

    //     CourseInfo cInfo = courseRepo.findById((long) 10).get();
    //     for(int i=0; i<cInfo.getUserInfo().size(); i++){
    //         System.out.println("YayYay" + cInfo.getUserInfo().get(i).getUserName());
    //     }

        
    //     return "helloworld";
    // }

    @GetMapping("/guest-course-details")
    private String courseDetails(@RequestParam("cid") Long cid, Model model){
        CourseInfo courseInfo = courseRepo.findById(cid).get();
        List<CourseTime> courseTimeList = courseInfo.getCourseTime();
        List<UserAccount> userAccountList = userAccountRepo.findByRole("teacher");
        List<UserInfo> userInfoList = new ArrayList<>();
        for(UserAccount userAccount : userAccountList){
            UserInfo userInfo = userInfoRepo.findByUserAccount(userAccount);
            userInfoList.add(userInfo);
        }
        model.addAttribute("courseInfo", courseInfo);
        model.addAttribute("courseTimeList", courseTimeList);
        model.addAttribute("userInfoList", userInfoList);
        return "CM0003_CourseDetails";
    }

    @GetMapping("/teacher-course-details")
    private String teacherCourseDetails(){
        return "CM0003_CourseDetailsTeacher";
    }

    @GetMapping("/admin-course-details")
    private String adminCourseDetails(){
        return "CM0003_CourseDetailsAdmin";
    }

}
 