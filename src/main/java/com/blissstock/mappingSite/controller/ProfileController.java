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
import com.blissstock.mappingSite.repository.UserRepository;
import com.blissstock.mappingSite.service.StorageService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.UrlResource;
import org.springframework.mail.MailSendException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;


@Controller
public class ProfileController {
    @Autowired
    UserRepository userRepo;

    @Autowired
    StorageService storageService;
    private final Path root = Paths.get("uploads");
    private final Path certificatePath = Paths.get(root+File.separator+"certificates");
    
    @GetMapping(value="/profile/{userId}")
    private String getReviewList( @PathVariable Long userId, Model model) {  
        UserInfo userInfo=userRepo.findById(userId).orElse(null);
        UserAccount userAcc = userInfo.getUserAccount();
        String basePath = "/images/profiles/";
        model.addAttribute("imagePath",basePath+userAcc.getPhoto());
        if(userAcc.getRole().equals("student")){
            model.addAttribute("stuInfo", userInfo.toMapStudent());        
            return "CM0004_StudentProfile";
        }
        model.addAttribute("trInfo", userInfo.toMapTeacher());
        List<FileInfo> fileInfos = loadImages();
        model.addAttribute("files", fileInfos);
            return "CM0004_TeacherProfile";
    }

    
  private List<FileInfo> loadImages() {
    try {
      return storageService
        .loadAllCertificates()
        .map(
          path -> {
            String name = path.getFileName().toString();
            String url = MvcUriComponentsBuilder
              .fromMethodName(
                FileController.class,
                "getCertificates",
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
