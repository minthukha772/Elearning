package com.blissstock.mappingSite.service;

import java.nio.file.Path;
import java.util.stream.Stream;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface StorageService {

	void init();

	public Resource loadCertificate(String filename);

	void storeCertificates(MultipartFile[] files) ;

	Stream<Path> loadAllCertificates();



}