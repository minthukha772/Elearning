package com.blissstock.mappingSite.controller;

import com.blissstock.mappingSite.entity.UserAccount;
import com.blissstock.mappingSite.entity.UserInfo;
import com.blissstock.mappingSite.exceptions.UnauthorizedFileAccessException;
import com.blissstock.mappingSite.model.FileInfo;
import com.blissstock.mappingSite.repository.UserAccountRepository;
import com.blissstock.mappingSite.repository.UserRepository;
import com.blissstock.mappingSite.service.StorageService;
import com.blissstock.mappingSite.service.StorageServiceImpl;
import com.blissstock.mappingSite.service.UserSessionService;
import com.blissstock.mappingSite.utils.CheckUploadFileType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;


@Controller
public class AdminTopController {
  @Autowired
  StorageService storageService;

	@Autowired
	public AdminTopController(StorageService storageService) {
		this.storageService = storageService;
	}
    @Autowired
    UserSessionService userSessionService;
    
    @Autowired
    UserRepository userRepo;

    @Autowired
    UserAccountRepository userAccRepo;

    @GetMapping(value="/admin/top")
    private String getAdminTopScreen( Model model) {  
        Long userId = userSessionService.getUserAccount().getId();
        UserInfo userInfo=userRepo.findById(userId).orElse(null);
        UserAccount userAcc = userInfo.getUserAccount();
         //load profile picture
         if(userInfo.getPhoto()==null){
          String photoString=null;
          model.addAttribute("pic64", photoString);
        }
        FileInfo profile = storageService.loadProfileAsFileInfo(userInfo);
        model.addAttribute("profile", profile);
        
        model.addAttribute("userInfo", userInfo);
        model.addAttribute("userAcc", userAcc);

        // model.addAttribute("trInfo", userInfo.toMapTeacher());
        // List<FileInfo> fileInfos = loadImages();
        // model.addAttribute("files", fileInfos);
        model.addAttribute("postAction","/admin/top/update/"+userId);;
            return "AD0001_AdminTop";
    }

    


  @PostMapping(value= "/admin/top/update/{userId}")
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
        //upload photo 
        try {
          storageService.store(userId, photo, StorageServiceImpl.PROFILE_PATH, true);
          userInfo.setPhoto(originalFileName);
          userRepo.save(userInfo);
        } catch (UnauthorizedFileAccessException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }

        //insert photo 
       
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
