package com.blissstock.mappingSite.controller;

import com.blissstock.mappingSite.exceptions.UnauthorizedFileAccessException;
import com.blissstock.mappingSite.service.StorageService;
import com.blissstock.mappingSite.service.StorageServiceImpl;
import com.blissstock.mappingSite.service.UserSessionService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
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

  @Autowired
  UserSessionService userSessionService;

  private static Logger logger = LoggerFactory.getLogger(
      FileController.class);

  @GetMapping("/files/{type}/{uid}/{filename:.+}")
  @ResponseBody
  public ResponseEntity<Resource> getResource(
      @PathVariable String type,
      @PathVariable Long uid,
      @PathVariable String filename) {

    Resource file = null;
    try {

      logger.info("The file name: {}, type: {}, of user_id: {} is being loaded", filename, type, uid);

      if (type.equals("profiles")) {
        file = storageService.load(uid, filename, StorageServiceImpl.PROFILE_PATH);
      } else if (type.equals("certificates")) {
        file = storageService.load(uid, filename, StorageServiceImpl.CERTIFICATE_PATH);
      } else if (type.equals("slip")) {
        file = storageService.load(uid, filename, StorageServiceImpl.SLIP_PATH);
      } else if (type.equals("cphotos")) {
        file = storageService.load(uid, filename, StorageServiceImpl.COURSE_PATH);
      } else if (type.equals("question_materials")) {
        file = storageService.load(uid, filename, StorageServiceImpl.QUESTION_MATERIAL_PATH);
      } else if (type.equals("answer_materials")) {
        file = storageService.load(uid, filename, StorageServiceImpl.ANSWER_MATERIAL_PATH);
      }
      logger.info("Resource file: {} loaded, Success", file);

    } catch (UnauthorizedFileAccessException e) {
      e.printStackTrace();
      logger.error(e.getLocalizedMessage());
      return ResponseEntity.badRequest().body(null);
    }
    return ResponseEntity
        .ok()
        /*
         * .header(
         * HttpHeaders.CONTENT_DISPOSITION,
         * "attachment; filename=\"" + file.getFilename() + "\""
         * )
         */
        .contentType(MediaType.IMAGE_JPEG)
        .body(file);
  }
}
