package com.blissstock.mappingSite.service;

import java.io.IOException;
import java.nio.file.Path;
import java.util.stream.Stream;

import com.blissstock.mappingSite.exceptions.UnauthorizedFileAccessException;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface StorageService {

	void init();

	public Resource loadCertificate(Long uid, String filename) throws UnauthorizedFileAccessException;

	void storeCertificates(Long uid, MultipartFile[] files) throws UnauthorizedFileAccessException;

	Stream<Path> loadAllCertificates(Long uid) throws UnauthorizedFileAccessException;

	public void deleteCertificate(Long uid, String filename) throws IOException, UnauthorizedFileAccessException;

	public boolean checkAuthForTeacher(Long uid);

}