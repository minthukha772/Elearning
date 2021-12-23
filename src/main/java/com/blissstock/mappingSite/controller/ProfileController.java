package com.blissstock.mappingSite.controller;

import com.blissstock.mappingSite.entity.BankInfo;
import com.blissstock.mappingSite.entity.PaymentAccount;
import com.blissstock.mappingSite.entity.UserAccount;
import com.blissstock.mappingSite.entity.UserInfo;
import com.blissstock.mappingSite.enums.UserRole;
import com.blissstock.mappingSite.model.FileInfo;
import com.blissstock.mappingSite.model.Message;
import com.blissstock.mappingSite.repository.BankInfoRepository;
import com.blissstock.mappingSite.repository.PaymentAccountRepository;
import com.blissstock.mappingSite.repository.UserAccountRepository;
import com.blissstock.mappingSite.repository.UserInfoRepository;
import com.blissstock.mappingSite.repository.UserRepository;
import com.blissstock.mappingSite.service.StorageService;
import com.blissstock.mappingSite.service.UserService;
import com.blissstock.mappingSite.service.UserSessionService;
import com.blissstock.mappingSite.utils.CheckUploadFileType;
import com.blissstock.mappingSite.utils.FileNameGenerator;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.validation.Valid;
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
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

@Controller
public class ProfileController {

  private static Logger logger = LoggerFactory.getLogger(
    ProfileController.class
  );

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

  @GetMapping(
    value = { "/student/browse/profile/{id}", "/teacher/browse/profile/{id}" }
  )
  private String browseProfile(Model model, @PathVariable Long id) {
    logger.info("GET Requested");
    logger.info("Getting userinfo: {} from database", id);

    //##################################################//
    //Load User Information//
    Optional<UserInfo> optionalUserInfo = userRepo.findById(id);
    //If request id does not exist
    if (!optionalUserInfo.isPresent()) {
      logger.info("User {} does not exist", id);
      return "redirect:/404";
    }
    UserInfo userInfo = optionalUserInfo.get();
    //##################################################//

    //##################################################//
    // Check Role

    UserRole role = UserRole.strToUserRole(userInfo.getUserAccount().getRole());
    logger.debug("requested user role is {}", role.getValue());

    if (role == UserRole.ADMIN || role == UserRole.SUPER_ADMIN) {
      logger.info("User does not have sufficient auth to access uid:{}", id);
      return "redirect:/404";
    }

    //##################################################//

    //##################################################//
    //Load Profile
    try {
      FileInfo profilePic = loadProfile(id);
      model.addAttribute("profilePic", profilePic);
    } catch (Exception e) {
      e.printStackTrace();
      logger.info("unable to get profile {}", id);
    }
    //##################################################//

    //##################################################//
    //Load Certificate
    if (role == UserRole.TEACHER) {
      //load certificates
      List<FileInfo> certificateFiles = loadImages(id);
      logger.info("User have certificates: {}", certificateFiles.size());
      model.addAttribute("certificateFiles", certificateFiles);
    }
    //##################################################//

    model.addAttribute("isEditable", false);
    model.addAttribute("role", role.getValue());
    model.addAttribute("infoMap", userInfo.toMap(true));
    model.addAttribute("id", id);

    return "CM0004_Profile";
  }

  @GetMapping(
    value = {
      "/student/profile/", "/teacher/profile", "/admin/browse/profile/{id}",
    }
  )
  private String viewProfile(
    Model model,
    @PathVariable(required = false) Long id,
    String message,
    String error
  ) {
    logger.info("GET Requested");

    long uid = getUid(id);

    //##################################################//
    //Display Message//
    if (error != null) {
      logger.debug("Error Message: {}", error);
      Message messageInfo = new Message();
      messageInfo.setError(true);
      messageInfo.setText(Message.ERROR);
      model.addAttribute("message", messageInfo);
    } else if (message != null) {
      logger.debug("Message: {}", message);
      Message messageInfo = new Message();
      messageInfo.setError(false);
      if (message.equals("profileEdit")) {
        messageInfo.setText(Message.PROFILE_EDIT_SUCCESSFULLY);
      }
      model.addAttribute("message", messageInfo);
    }

    //############################################

    //##################################################//
    //Load User Information//
    Optional<UserInfo> optionalUserInfo = userRepo.findById(uid);
    //If request uid does not exist
    if (!optionalUserInfo.isPresent()) {
      logger.info("User {} does not exist", uid);
      return "redirect:/404";
    }
    UserInfo userInfo = optionalUserInfo.get();
    //##################################################//

    //##################################################//
    // Check Role

    UserRole role = UserRole.strToUserRole(userInfo.getUserAccount().getRole());
    logger.debug("requested user role is {}", role.getValue());

    if (role == UserRole.ADMIN || role == UserRole.SUPER_ADMIN) {
      logger.info("User does not have sufficient auth to access uuid:{}", uid);
      return "redirect:/404";
    }

    //##################################################//

    //##################################################//
    //Load Profile
    try {
      FileInfo profilePic = loadProfile(uid);
      model.addAttribute("profilePic", profilePic);
    } catch (Exception e) {
      e.printStackTrace();
      logger.info("unable to get profile {}", uid);
    }

    //##################################################//

    //##################################################//
    //Load Certificate
    if (role == UserRole.TEACHER) {
      //load certificates
      List<FileInfo> certificateFiles = loadImages(uid);
      logger.info("User have certificates: {}", certificateFiles.size());
      model.addAttribute("certificateFiles", certificateFiles);
    }
    //##################################################//
    model.addAttribute("isEditable", true);
    model.addAttribute("role", role.getValue());
    model.addAttribute("infoMap", userInfo.toMap(false));
    model.addAttribute("id", uid);

    return "CM0004_Profile";
  }

  @GetMapping(
    value = {
      "/teacher/profile/{userId}",
      "/student/profile/{userId}",
      "/admin/profile/{userId}",
    }
  )
  private String getProfile(@PathVariable Long userId, Model model) {
    String role = userSessionService.getRole() == UserRole.TEACHER
      ? "teacher"
      : userSessionService.getRole() == UserRole.STUDENT ? "student" : "admin";
    UserInfo userInfo = userRepo.findById(userId).orElse(null);
    model.addAttribute("userInfo", userInfo);
    model.addAttribute("role", role);
    UserAccount userAcc = userInfo.getUserAccount();

    List<BankInfo> bankList = bankRepo.findAll();
    model.addAttribute("bankList", bankList);

    if (userAcc.getRole().equals("ROLE_STUDENT")) {
      FileInfo profile = loadProfile(userId);
      model.addAttribute("profile", profile);
      model.addAttribute("stuInfo", userInfo.toMap(false));
      model.addAttribute(
        "profilePost",
        "/" + role + "/profile-upload/" + userId
      );
      return "CM0004_StudentProfile";
    }
    model.addAttribute("trInfo", userInfo.toMap(false));
    //load profile picture
    FileInfo profile = loadProfile(userId);
    model.addAttribute("profile", profile);

    //load certificates
    List<FileInfo> fileInfos = loadImages(userId);
    //System.out.println(fileInfos);
    model.addAttribute("files", fileInfos);

    //post action

    model.addAttribute("profilePost", "/" + role + "/profile-upload/" + userId);
    return "CM0004_TeacherProfile";
  }

  /* model.addAttribute("trInfo", userInfo.toMapTeacher());
    //load profile picture
    if (userAcc.getPhoto() == null) {
      String photoString = null;
      model.addAttribute("pic64", photoString);
    }
    FileInfo profile = loadProfile(userId);
    model.addAttribute("profile", profile);

    //load certificates
    List<FileInfo> fileInfos = loadImages(userId);
    model.addAttribute("files", fileInfos);

    //post action
    model.addAttribute("profilePost", role + "/profile-upload/" + userId);
    return "CM0004_TeacherProfile";
  } */

  //upload profile and payment
  @PostMapping(
    value = {
      "/student/profile-upload/{userId}",
      "/teacher/profile-upload/{userId}",
      "/admin/profile-upload/{userId}",
    }
  )
  private String postProfile(
    Model model,
    @RequestParam("profile_pic") MultipartFile photo,
    @RequestParam(value = "action", required = true) String action,
    @Valid @ModelAttribute("userInfo") UserInfo userPayment,
    @Valid @ModelAttribute("profile") FileInfo profile,
    @PathVariable Long userId
  ) {
    String role = userSessionService.getRole() == UserRole.TEACHER
      ? "teacher"
      : userSessionService.getRole() == UserRole.STUDENT ? "student" : "admin";
    if (action.equals("profile-upload")) {
      if (!photo.isEmpty()) {
        if (CheckUploadFileType.checkType(photo)) {
          //get original photo name and generate a new file name
          String originalFileName = StringUtils.cleanPath(
            photo.getOriginalFilename()
          );
          String saveFileName = FileNameGenerator.getRandomFileName(
            originalFileName
          );

          //upload photo
          storageService.storeProfile(photo, saveFileName);

          //insert photo

          userRepo.save(userPayment);
        } else {
          model.addAttribute(
            "photoTypeErr",
            "Files other than image file cannot be uploaded."
          );
          return "CM0004_TeacherProfile";
        }
      }
    }
    //upload payment info
    else if (action.equals("add_payment")) {
      List<PaymentAccount> payAccs = userPayment.getPaymentAccount();

      for (PaymentAccount payAcc : payAccs) {
        payAcc.setUserInfo(userPayment);

        BankInfo bankInfo = bankRepo
          .findById(payAcc.getCheckedBank())
          .orElse(null);
        payAcc.setBankInfo(bankInfo);
        payAccRepo.save(payAcc);
        System.out.println(payAcc);
      }
    }

    return "redirect:/" + role + "/profile/" + userId;
  }

  private Long getUid(Long id) {
    Long uid = 0L;
    UserRole role = userSessionService.getRole();
    if (role == UserRole.ADMIN || role == UserRole.SUPER_ADMIN) {
      uid = id;
    } else if (role == UserRole.TEACHER || role == UserRole.STUDENT) {
      uid = userSessionService.getUserAccount().getAccountId();
    } else {
      throw new RuntimeException("user authetication fail");
    }
    return uid;
  }

  //Get profile photo
  private FileInfo loadProfile(long userId) {
    try {
      UserAccount userAcc = userAccRepo.findById(userId).orElse(null);
      Path path = storageService.loadProfile(userAcc.getPhoto());
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
