package com.blissstock.mappingSite.service;

import com.blissstock.mappingSite.exceptions.UnauthorizedFileAccessException;
import java.io.IOException;
import java.nio.file.Path;
import java.util.stream.Stream;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface StorageService {
  void init();

  Resource loadAsResource(String filename);

  public Resource loadCertificate(Long uid, String filename)
    throws UnauthorizedFileAccessException;

    public void storeProfile(MultipartFile file, String fileName);

  void store(Long uid, MultipartFile files, Path path)
    throws UnauthorizedFileAccessException;

  Stream<Path> loadAllCertificates(Long uid)
    throws UnauthorizedFileAccessException;

  public void deleteCertificate(Long uid, String filename)
    throws IOException, UnauthorizedFileAccessException;

  public boolean checkAuthForTeacher(Long uid);

  public Path loadProfile(String filename);
}
