package com.blissstock.mappingSite.controller;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.blissstock.mappingSite.service.ThumbnailService;

@Controller
public class ThumbnailController {

    @Autowired
    ThumbnailService thumbnailService;

    private static final Logger logger = LoggerFactory.getLogger(CourseDetailsController.class);

    @GetMapping("/admin/generate/thumbnail")
    public String listFiles() {
    
        return "GenerateThumbnail";
    }

    @PostMapping("/admin/thumbnail")
    public ResponseEntity<String> thumbnail(@RequestParam("fileInput") MultipartFile videoFile, Model model)
            throws IOException {

        try {

            // Use the name of the uploaded video file as the thumbnail file name
            String videoFileName = videoFile.getOriginalFilename();
            String thumbnailName = videoFileName.substring(0, videoFileName.lastIndexOf("."));

            thumbnailService.generateThumbnail(videoFile.getInputStream(), "thumbnails/", thumbnailName, "jpg", 1);

            return ResponseEntity.status(HttpStatus.OK).build();

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

    }

}
