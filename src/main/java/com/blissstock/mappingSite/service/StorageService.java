package com.blissstock.mappingSite.service;

import com.blissstock.mappingSite.exceptions.UnauthorizedFileAccessException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Path;
import java.util.stream.Stream;
import java.util.stream.Stream;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface StorageService {
  void init();

  void storeProfile(MultipartFile file, String fileName);

  Path loadProfile(String filename);

  Resource loadAsResource(String filename);

  public Resource loadCertificate(Long uid, String filename)
    throws UnauthorizedFileAccessException;

  void storeCertificates(Long uid, MultipartFile[] files)
    throws UnauthorizedFileAccessException;

  Stream<Path> loadAllCertificates(Long uid)
    throws UnauthorizedFileAccessException;

  public void deleteCertificate(Long uid, String filename)
    throws IOException, UnauthorizedFileAccessException;

  public boolean checkAuthForTeacher(Long uid);
}
