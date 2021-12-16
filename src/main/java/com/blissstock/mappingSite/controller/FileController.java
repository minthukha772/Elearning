package com.blissstock.mappingSite.controller;

import com.blissstock.mappingSite.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import com.blissstock.mappingSite.exceptions.UnauthorizedFileAccessException;
import com.blissstock.mappingSite.service.UserSessionService;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class FileController {

  @Autowired
  StorageService storageService;
  
  @GetMapping("/files/profiles/{filename:.+}")
  @ResponseBody
  public ResponseEntity<Resource> getProfile(@PathVariable String filename) {
    Resource file = storageService.loadAsResource(filename);
    return ResponseEntity
      .ok()
      .header(
        HttpHeaders.CONTENT_DISPOSITION,
        "attachment; filename=\"" + file.getFilename() + "\""
      )
      .body(file);
  }


  @Autowired
  UserSessionService userSessionService;

  @GetMapping("/files/certificates/{uid}/{filename:.+}")
  @ResponseBody
  public ResponseEntity<Resource> getCertificates(
    @PathVariable Long uid,
    @PathVariable String filename
  ) {
    System.out.println(filename);
    Resource file;
    try {
      file = storageService.loadCertificate(uid, filename);
    } catch (UnauthorizedFileAccessException e) {
      e.printStackTrace();
      return ResponseEntity.badRequest().body(null);
    }
    return ResponseEntity
      .ok()
      /*       .header(
        HttpHeaders.CONTENT_DISPOSITION,
        "attachment; filename=\"" + file.getFilename() + "\""
      ) */
      .contentType(MediaType.IMAGE_JPEG)
      .body(file);
  }
}
