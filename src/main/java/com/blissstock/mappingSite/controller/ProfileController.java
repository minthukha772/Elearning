package com.blissstock.mappingSite.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import java.util.List;

import javax.annotation.Resource;
import javax.validation.Valid;
import com.blissstock.mappingSite.model.FileInfo;
import com.blissstock.mappingSite.entity.BankInfo;
import com.blissstock.mappingSite.entity.CourseInfo;
import com.blissstock.mappingSite.entity.PaymentAccount;
import com.blissstock.mappingSite.entity.UserAccount;
import com.blissstock.mappingSite.entity.UserInfo;
import com.blissstock.mappingSite.enums.UserRole;
import com.blissstock.mappingSite.repository.BankInfoRepository;
import com.blissstock.mappingSite.repository.CourseInfoRepository;
import com.blissstock.mappingSite.repository.PaymentAccountRepository;
import com.blissstock.mappingSite.repository.UserAccountRepository;
import com.blissstock.mappingSite.repository.UserRepository;
import com.blissstock.mappingSite.service.StorageService;
import com.blissstock.mappingSite.service.UserService;
import com.blissstock.mappingSite.service.UserSessionService;
import com.blissstock.mappingSite.utils.CheckUploadFileType;
import com.blissstock.mappingSite.utils.FileNameGenerator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.UrlResource;
import org.springframework.mail.MailSendException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import ch.qos.logback.core.net.SyslogOutputStream;


@Controller
public class ProfileController {
  @Autowired
  StorageService storageService;

	@Autowired
	public ProfileController(StorageService storageService) {
		this.storageService = storageService;
	}
    @Autowired
    UserSessionService userSessionService;

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepo;

    @Autowired
    UserAccountRepository userAccRepo;

    @Autowired
    PaymentAccountRepository payAccRepo;

    @Autowired
    BankInfoRepository bankRepo;

    //Get profile
    @Valid
    @GetMapping(value={"/teacher/profile/{userId}",
                      "/student/profile/{userId}",
                      "/admin/profile/{userId}"
  })
    private String getProfile( @PathVariable Long userId, Model model) {  
        String role = userSessionService.getRole() == UserRole.TEACHER ? "teacher" : userSessionService.getRole() == UserRole.STUDENT ? "student" : "admin";
        UserInfo userInfo=userRepo.findById(userId).orElse(null);      
        model.addAttribute("userInfo", userInfo);
        model.addAttribute("role", role);
        UserAccount userAcc = userInfo.getUserAccount();

        List<BankInfo> bankList = bankRepo.findAll();
        model.addAttribute("bankList", bankList);

        if(userAcc.getRole().equals("ROLE_STUDENT")){
          FileInfo profile = loadProfile(userId);
          model.addAttribute("profile", profile);
          model.addAttribute("stuInfo", userInfo.toMapStudent());
          model.addAttribute("profilePost", "/"+role+"/profile-upload/"+userId);        
            return "CM0004_StudentProfile";
        }
        model.addAttribute("trInfo", userInfo.toMapTeacher());
        //load profile picture
        FileInfo profile = loadProfile(userId);
        model.addAttribute("profile", profile);

        //load certificates
        List<FileInfo> fileInfos = loadImages(userId);
        //System.out.println(fileInfos);
        model.addAttribute("files", fileInfos);

        //post action
        
        model.addAttribute("profilePost", "/"+role+"/profile-upload/"+userId);
        return "CM0004_TeacherProfile";
    }

  //upload profile and payment
  @PostMapping(value= {"/student/profile-upload/{userId}",
                      "/teacher/profile-upload/{userId}",
                      "/admin/profile-upload/{userId}"
})
  private String postProfile(Model model,
    @RequestParam("profile_pic") MultipartFile photo, 
    @RequestParam(value="action", required=true) String action,
    @Valid @ModelAttribute("userInfo") UserInfo userPayment,
    @Valid @ModelAttribute("profile") FileInfo profile,
    @PathVariable Long userId
  ) {
    String role = userSessionService.getRole() == UserRole.TEACHER ? "teacher" : userSessionService.getRole() == UserRole.STUDENT ? "student" : "admin";
    if(action.equals("profile-upload")){
        
    if(!photo.isEmpty()) {
      if(CheckUploadFileType.checkType(photo)) {
        //get original photo name and generate a new file name
          String originalFileName =StringUtils.cleanPath(photo.getOriginalFilename());
          String saveFileName = FileNameGenerator.getRandomFileName(originalFileName);
 
        //upload photo 
        storageService.storeProfile(photo,saveFileName);

        //insert photo 
        
        userRepo.save(userPayment);
      }else {
        model.addAttribute("photoTypeErr", "Files other than image file cannot be uploaded.");
        return "CM0004_TeacherProfile";
      }
    }
  }
  //upload payment info
  else if(action.equals("add_payment")){
    List<PaymentAccount> payAccs=userPayment.getPaymentAccount();

    for(PaymentAccount payAcc : payAccs){
      payAcc.setUserInfo(userPayment);
      
      BankInfo bankInfo=bankRepo.findById(payAcc.getCheckedBank()).orElse(null);
      payAcc.setBankInfo(bankInfo);
        payAccRepo.save(payAcc);
        System.out.println(payAcc);
      }
   }

   
      return "redirect:/"+role+"/profile/"+userId;
  

  }

  //Get profile photo
  private FileInfo loadProfile(long userId) {
    try {
        UserInfo userInfo=userRepo.findById(userId).orElse(null);
        if(userInfo.getPhoto()==null){
          userInfo.setPhoto("profile1.jpg");
          Path path= storageService.loadProfile(userInfo.getPhoto());
          String name = path.getFileName().toString();
          String url = "/images/profiles/profile1.jpg";

        return new FileInfo(name, url);
        }
        Path path= storageService.loadProfile(userInfo.getPhoto());
        String name = path.getFileName().toString();
        String url = MvcUriComponentsBuilder
          .fromMethodName(
            FileController.class,
            "getProfile",
            path.getFileName().toString()
          )
          .build()
          .toString();

        return new FileInfo(name, url);
    } catch (Exception e) {
      return null;
    }
    
  }
    
  //Get certificate  
    private List<FileInfo> loadImages(Long uid) {
      try {
        return storageService
          .loadAllCertificates(uid)
          .map(
            path -> {
              String name = path.getFileName().toString();
              String url = MvcUriComponentsBuilder
                .fromMethodName(
                  FileController.class,
                  "getCertificates",
                  uid,
                  path.getFileName().toString()
                )
                .build()
                .toString();
              return new FileInfo(name, url);
            }
          )
          .collect(Collectors.toList());
      } catch (Exception e) {
        return new ArrayList<>();
      }
    }
    
  }