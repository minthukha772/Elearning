package com.blissstock.mappingSite.controller;

import com.blissstock.mappingSite.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class FileController {

  @Autowired
  StorageService storageService;

  @GetMapping("/files/certificates/{filename:.+}")
  @ResponseBody
  public ResponseEntity<Resource> getCertificates(@PathVariable String filename) {
    Resource file = storageService.loadCertificate(filename);
    return ResponseEntity
      .ok()
      .header(
        HttpHeaders.CONTENT_DISPOSITION,
        "attachment; filename=\"" + file.getFilename() + "\""
      )
      .body(file);
  }
}
