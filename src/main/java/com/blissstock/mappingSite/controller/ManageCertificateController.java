package com.blissstock.mappingSite.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.blissstock.mappingSite.enums.UserRole;
import com.blissstock.mappingSite.exceptions.NotImageFileException;
import com.blissstock.mappingSite.exceptions.UnauthorizedFileAccessException;
import com.blissstock.mappingSite.model.FileInfo;
import com.blissstock.mappingSite.service.StorageService;
import com.blissstock.mappingSite.service.UserSessionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

@Controller
public class ManageCertificateController {

  @Autowired
  StorageService storageService;

  @Autowired
  UserSessionService userSessionService;

  @GetMapping(
    value = { "/teacher/manage_certificate", "/admin/manage_certificate/{id}" }
  )
  public String manageCertificate(
    Model model,
    @PathVariable(name = "id", required = false) Long id
  ) {
    Long uid = getUid(id);
    List<FileInfo> fileInfos = loadImages(uid);
    model.addAttribute("files", fileInfos);

    return "AT0007_manage_certificate";
  }

  @PostMapping(
    value = { "/teacher/manage_certificate", "/admin/manage_certificate/{id}" }
  )
  public String uploadFiles(
    @RequestParam("files") MultipartFile[] files,
    Model model,
    @PathVariable(name = "id", required = false) Long id
  ) {
    System.out.println(files.length);
    Long uid = getUid(id);
    try {
      if (files.length > 0) {
        storageService.storeCertificates(uid, files);
      } else {
        model.addAttribute(
          "fileUploadError",
          "Please Select at least one file"
        );
      }
    } catch (NotImageFileException e) {
      model.addAttribute(
        "fileUploadError",
        "Only Jpg, Jpeg and Png are allowed"
      );
    } catch (UnauthorizedFileAccessException e) {
      e.printStackTrace();
      model.addAttribute(
        "fileUploadError",
        "Unauthorized File Access"
      );
    } catch (Exception e) {
      e.printStackTrace();
    }

    List<FileInfo> fileInfos = loadImages(uid);
    model.addAttribute("files", fileInfos);

    return "AT0007_manage_certificate";
  }

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

  @DeleteMapping(
    value = { "/teacher/manage_certificate", "/admin/manage_certificate/{id}" }
  )
  public ResponseEntity<Object> deleteCertificate(
    String name,
    @PathVariable(name = "id", required = false) Long id
  ) {
    System.out.println("Delete requested for file name: " + name);
    Long uid = getUid(id);
    //return ResponseEntity.badRequest().body("something went wrong");
    try {
      storageService.deleteCertificate(uid, name);
    } catch (IOException e) {
      e.printStackTrace();
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("something went wrong");
    } catch (UnauthorizedFileAccessException e) {
      e.printStackTrace();
      return ResponseEntity.status(HttpStatus.FORBIDDEN).body("forbidden");
    }

    return ResponseEntity.ok().body("delete successfully");
  }

  //To decide with user id to use
  //If user is teacher, use id from session
  //If user is admin, use id provide by the path value
  private Long getUid(Long id) {
    Long uid = 0L;
    UserRole role = userSessionService.getRole();
    if (role == UserRole.ADMIN || role == UserRole.SUPER_ADMIN) {
      uid = id;
    } else if (role == UserRole.TEACHER) {
      uid = userSessionService.getUserAccount().getAccountId();
    } else {
      throw new RuntimeException("user authetication fail");
    }
    return uid;
  }
}
