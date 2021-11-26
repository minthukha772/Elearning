package com.blissstock.mappingSite.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.blissstock.mappingSite.exceptions.NotImageFileException;
import com.blissstock.mappingSite.model.FileInfo;
import com.blissstock.mappingSite.service.StorageService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

@Controller
public class ManageCertificateController {

  @Autowired
  StorageService storageService;

  @GetMapping("/manage_certificate")
  public String manageCertificate(Model model) {
    List<FileInfo> fileInfos = loadImages();
    model.addAttribute("files", fileInfos);

    return "AT0007_manage_certificate";
  }

  @PostMapping(value = "/manage_certificate")
  public String uploadFiles(
    @RequestParam("files") MultipartFile[] files,
    Model model
  ) {
    System.out.println(files.length);
    try {
      if (files.length > 0) {
        storageService.storeCertificates(files);
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
    } catch (Exception e) {
      e.printStackTrace();
    }

    List<FileInfo> fileInfos = loadImages();
    model.addAttribute("files", fileInfos);

    return "AT0007_manage_certificate";
  }

  //TODO Implement DELETE REQUEST

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
