package com.blissstock.mappingSite.service;

import com.blissstock.mappingSite.exceptions.FileStorageException;
import com.blissstock.mappingSite.exceptions.NotImageFileException;
import com.blissstock.mappingSite.validation.validators.ImageFileValidator;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.stream.Stream;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class StorageServiceImpl implements StorageService {

  private final Path root = Paths.get("uploads");
  private final Long uid = 13L;

  @Override
  public void init() {
    try {
      if (!Files.exists(root)) {
        Files.createDirectory(root);
      }
    } catch (IOException e) {
      throw new RuntimeException("Could not initialize folder for upload!");
    }
  }

  @Override
  public void storeCertificates(MultipartFile[] files) {
    //Checking Content Type
    ImageFileValidator fileValidator = new ImageFileValidator();
    for (MultipartFile file : files) {
      if (!fileValidator.isSupportedContentType(file.getContentType())) {
        throw new NotImageFileException();
      }
    }

    Path storeLocation = Paths.get(root + File.separator + uid);
    if (!Files.exists(storeLocation)) {
      try {
        Files.createDirectories(storeLocation);
      } catch (IOException e) {
        e.printStackTrace();
        throw new RuntimeException("Could not initialize folder for upload!");
      }
    }
    System.out.println(storeLocation.toAbsolutePath());
    for (MultipartFile file : files) {
      try {
        try {
          Files.copy(
            file.getInputStream(),
            storeLocation.resolve(file.getOriginalFilename())
          );
        } catch (Exception e) {
          throw new RuntimeException(
            "Could not store the file. Error: " + e.getMessage()
          );
        }
      } catch (Exception e) {
        e.printStackTrace();

        throw new FileStorageException(
          "Could not store file " +
          file.getOriginalFilename() +
          ". Please try again!"
        );
      }
      System.out.println("Operation Successful");
    }
  }

  @Override
  public Stream<Path> loadAllCertificates() {
    Path storeLocation = Paths.get(root + File.separator + uid);
    try {
      return Files
        .walk(storeLocation, 1)
        .filter(path -> !path.equals(storeLocation))
        .map(storeLocation::relativize);
    } catch (IOException e) {
      throw new RuntimeException("Could not load the files!");
    }
  }

  @Override
  public Resource loadCertificate(String filename) {
    Path storeLocation = Paths.get(root + File.separator + uid);
    try {
      Path file = storeLocation.resolve(filename);
      Resource resource = new UrlResource(file.toUri());

      if (resource.exists() || resource.isReadable()) {
        return resource;
      } else {
        throw new RuntimeException("Could not read the file!");
      }
    } catch (MalformedURLException e) {
      throw new RuntimeException("Error: " + e.getMessage());
    }
  }
}
