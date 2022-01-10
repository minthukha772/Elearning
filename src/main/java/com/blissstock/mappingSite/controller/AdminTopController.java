package com.blissstock.mappingSite.controller;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

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
import com.blissstock.mappingSite.utils.FileNameGenerator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class AdminTopController {

  private static Logger logger = LoggerFactory.getLogger(
      ProfileController.class);

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

  @GetMapping(value = "/admin/top/")
  private String getAdminTopScreen(Model model) {
    Long userId = userSessionService.getUserAccount().getAccountId();
    UserInfo userInfo = userRepo.findById(userId).orElse(null);
    UserAccount userAcc = userInfo.getUserAccount();

    logger.info("GET Requested");
    logger.info("Getting admininfo: {} from database", userId);
    Optional<UserInfo> optionalUserInfo = userRepo.findById(userId);
    // If request id does not exist
    if (!optionalUserInfo.isPresent()) {
      logger.info("User {} does not exist", userId);
      return "redirect:/404";
    }
    // load profile picture
    if (userInfo.getPhoto() == null) {
      String photoString = null;
      model.addAttribute("pic64", photoString);
    }
    FileInfo profile = storageService.loadProfileAsFileInfo(userInfo);
    model.addAttribute("profile", profile);

    model.addAttribute("userInfo", userInfo);
    model.addAttribute("userAcc", userAcc);

    // model.addAttribute("trInfo", userInfo.toMapTeacher());
    // List<FileInfo> fileInfos = loadImages();
    // model.addAttribute("files", fileInfos);
    // model.addAttribute("postAction","/admin/top/update");
    return "AD0001_AdminTop";

  }
  // //Get profile photo
  // private FileInfo loadProfile(long userId) {
  // try {
  // UserInfo userInfo = userRepo.findById(userId).orElse(null);
  // Path path = storageService.loadProfile(userInfo.getPhoto());
  // String name = path.getFileName().toString();
  // String url = MvcUriComponentsBuilder
  // .fromMethodName(
  // FileController.class,
  // "getProfile",
  // path.getFileName().toString()
  // )
  // .build()
  // .toString();

  // return new FileInfo(name, url);
  // } catch (Exception e) {
  // return null;
  // }

  // // @PostMapping(value= "/admin/top/update")
  // // private String postProfile(Model model,
  // // @RequestParam("profile_pic") MultipartFile photo,
  // // @RequestParam(value="action", required=true) String action
  // // ) {
  // // UserInfo userInfo=userRepo.findById(userId).orElse(null);
  // // UserAccount acc=userInfo.getUserAccount();

  // if(!photo.isEmpty()) {
  // if(CheckUploadFileType.checkType(photo)) {
  // //get original photo name and generate a new file name
  // String originalFileName =StringUtils.cleanPath(photo.getOriginalFilename());
  // //upload photo
  // try {
  // storageService.store(userId, photo, StorageServiceImpl.PROFILE_PATH, true);
  // userInfo.setPhoto(originalFileName);
  // userRepo.save(userInfo);
  // } catch (UnauthorizedFileAccessException e) {
  // // TODO Auto-generated catch block
  // e.printStackTrace();
  // }

  // //insert photo

  // }else {
  // model.addAttribute("photoTypeErr", "Files other than image file cannot be
  // uploaded.");
  // return "CM0004_TeacherProfile";
  // }
  // }
  // userInfo.setUserName(userInfo.getUserName());
  // userRepo.save(userInfo);
  // // else if(action.equals("add_payment")){
  // // List<PaymentAccount> payAccs=userInfo.getPaymentAccount();
  // // System.out.println(payAccs);
  // // for(PaymentAccount payAcc : payAccs){
  // // payAccRepo.save(payAcc);
  // // System.out.println(payAcc);
  // // }
  // // }

  // // return "AD0001_AdminTop";
  // // }

  @PostMapping(value = "/admin/top/edit/")
  public String editProfilePicture(
      Model model,
      @RequestParam("profile_pic") MultipartFile photo,
      @Valid @ModelAttribute("userAcc") UserAccount mailEdit,
      @PathVariable(required = false) Long id,
      @RequestParam(value = "action", required = true) String action,
      HttpServletRequest httpServletRequest) {
    logger.info("Post Requested");
    logger.info("Payment Edit Info {}", photo);

    Long uid = userSessionService.getUserAccount().getAccountId();
    UserInfo userInfo = userRepo.findById(uid).orElse(null);
    UserAccount userAcc = userAccRepo.findById(uid).orElse(null);

    String redirectAddress = "redirect:" +
        httpServletRequest.getRequestURI().replace("/edit/", "");
    logger.debug("Redirect Addresss {}", redirectAddress);

    if (action.equals("pic-edit")) {
      if (!photo.isEmpty() && CheckUploadFileType.checkType(photo)) {
        // get original photo name and generate a new file name
        String originalFileName = StringUtils.cleanPath(
            photo.getOriginalFilename());
        String saveFileName = FileNameGenerator.renameFileName(
            originalFileName,
            uid.toString());

        // upload photo
        try {
          storageService.store(uid, photo, StorageServiceImpl.PROFILE_PATH, true);
        } catch (UnauthorizedFileAccessException e) {
          e.printStackTrace();
        }
        // insert photo
        userInfo.setPhoto(saveFileName);
        userRepo.save(userInfo);

        logger.info("profile photo {} stored", saveFileName);
        return redirectAddress + "/";
      }
    } else if (action.equals("edit")) {

      UserInfo nameEdit = mailEdit.getUserInfo();
      // System.out.println(nameEdit.getPhoto());
      userInfo.setUserName(nameEdit.getUserName());
      userAcc.setMail(mailEdit.getMail());
      System.out.println(mailEdit.getMail());
      userRepo.save(userInfo);
      userAccRepo.save(userAcc);

      // logger.info("profile photo {} stored", saveFileName);
      return redirectAddress + "/";
    }

    return redirectAddress + "?error";
  }

}