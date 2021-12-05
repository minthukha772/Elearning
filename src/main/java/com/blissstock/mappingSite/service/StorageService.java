package com.blissstock.mappingSite.service;

import java.io.IOException;
import java.nio.file.Path;
import java.util.stream.Stream;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface StorageService {

	void init();

	public Resource loadCertificate(Long uid, String filename);

	void storeCertificates(Long uid, MultipartFile[] files) ;

	Stream<Path> loadAllCertificates(Long uid);

	public void deleteCertificate(Long uid, String filename) throws IOException;



}