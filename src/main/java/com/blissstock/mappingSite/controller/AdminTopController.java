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
import com.blissstock.mappingSite.entity.UserAccount;
import com.blissstock.mappingSite.entity.UserInfo;
import com.blissstock.mappingSite.repository.UserAccountRepository;
import com.blissstock.mappingSite.repository.UserRepository;
import com.blissstock.mappingSite.service.StorageService;
import com.blissstock.mappingSite.utils.CheckUploadFileType;
import com.blissstock.mappingSite.utils.FileNameGenerator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.UrlResource;
import org.springframework.mail.MailSendException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;


@Controller
public class AdminTopController {
  @Autowired
  StorageService storageService;

	@Autowired
	public AdminTopController(StorageService storageService) {
		this.storageService = storageService;
	}

    @Autowired
    UserRepository userRepo;

    @Autowired
    UserAccountRepository userAccRepo;

    @GetMapping(value="{role}/top/{userId}")
    private String getAdminTopScreen( @PathVariable String role, @PathVariable Long userId, Model model) {  
        UserInfo userInfo=userRepo.findById(userId).orElse(null);
        UserAccount userAcc = userInfo.getUserAccount();
         //load profile picture
         if(userInfo.getPhoto()==null){
          String photoString=null;
          model.addAttribute("pic64", photoString);
        }
        FileInfo profile = loadProfile(userId);
        model.addAttribute("profile", profile);
        
        model.addAttribute("userInfo", userInfo);
        model.addAttribute("userAcc", userAcc);

        // model.addAttribute("trInfo", userInfo.toMapTeacher());
        // List<FileInfo> fileInfos = loadImages();
        // model.addAttribute("files", fileInfos);
        model.addAttribute("postAction","/"+role+"/top/update/"+userId);;
            return "AD0001_AdminTop";
    }
    private FileInfo loadProfile(long userId) {
      try {
          UserInfo userInfo=userRepo.findById(userId).orElse(null);
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

  @PostMapping(value= "/{role}/top/update/{userId}")
  private String postProfile(Model model,
    @RequestParam("profile_pic") MultipartFile photo, 
    @RequestParam(value="action", required=true) String action,
    @PathVariable String role,
    @PathVariable Long userId
  ) {
    UserInfo userInfo=userRepo.findById(userId).orElse(null);
    UserAccount acc=userInfo.getUserAccount();
        
    if(!photo.isEmpty()) {
      if(CheckUploadFileType.checkType(photo)) {
        //get original photo name and generate a new file name
          String originalFileName =StringUtils.cleanPath(photo.getOriginalFilename());
          String saveFileName = FileNameGenerator.getRandomFileName(originalFileName);
 
        //upload photo 
        storageService.storeProfile(photo,saveFileName);

        //insert photo 
        userInfo.setPhoto(saveFileName);
        userRepo.save(userInfo);
      }else {
        model.addAttribute("photoTypeErr", "Files other than image file cannot be uploaded.");
        return "CM0004_TeacherProfile";
      }
    }
    userInfo.setUserName(userInfo.getUserName());
    userRepo.save(userInfo);
  // else if(action.equals("add_payment")){
  //   List<PaymentAccount> payAccs=userInfo.getPaymentAccount();
  //   System.out.println(payAccs);
  //   for(PaymentAccount payAcc : payAccs){
  //       payAccRepo.save(payAcc);
  //       System.out.println(payAcc);
  //     }
  //   }
      
      return "redirect:/"+role+"/top/"+userId;
  

  }

 
}
