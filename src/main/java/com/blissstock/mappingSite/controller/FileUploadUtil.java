package com.blissstock.mappingSite.controller;
import java.io.*;
import java.nio.file.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;
 
public class FileUploadUtil {

    private static Logger logger = LoggerFactory.getLogger(
    FileUploadUtil.class);
     
    public static void saveFile(String uploadDir, String fileName,
            MultipartFile multipartFile) throws IOException {
        Path uploadPath = Paths.get(uploadDir);
         
        if (!Files.exists(uploadPath)) {
            logger.info("File not exists on the path: {}", uploadPath);
            logger.info("Creating a new file/directory");
            Files.createDirectories(uploadPath);
        }
         
        try (InputStream inputStream = multipartFile.getInputStream()) {
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
            logger.info("File saved successfully: {}", filePath);
        } catch (IOException ioe) { 
            throw new IOException("Could not save image file: " + fileName, ioe);
        }      
    }
}