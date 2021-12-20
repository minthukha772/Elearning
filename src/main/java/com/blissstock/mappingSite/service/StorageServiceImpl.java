package com.blissstock.mappingSite.service;

import com.blissstock.mappingSite.entity.UserAccount;
import com.blissstock.mappingSite.enums.UserRole;
import com.blissstock.mappingSite.exceptions.FileStorageException;
import com.blissstock.mappingSite.exceptions.NotImageFileException;
import com.blissstock.mappingSite.exceptions.UnauthorizedFileAccessException;
import com.blissstock.mappingSite.validation.validators.ImageFileValidator;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.stream.Stream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class StorageServiceImpl implements StorageService {

  private final Path root = Paths.get("uploads");
  private final Path certificatePath = Paths.get(
    root + File.separator + "certificates"
  );
  private final Path profilepath = Paths.get(
    root + File.separator + "profiles"
  );

  @Autowired
  UserSessionService userSessionService;

  @Override
  public void init() {
    try {
      if (!Files.exists(root)) {
        Files.createDirectory(root);
      }
      if (!Files.exists(certificatePath)) {
        Files.createDirectory(certificatePath);
      }
      if (!Files.exists(profilepath)) {
        Files.createDirectory(profilepath);
      }
    } catch (IOException e) {
      throw new RuntimeException("Could not initialize folder for upload!");
    }
  }

  @Override
	public void storeProfile(MultipartFile file,String fileName) {
		try {
			if (file.isEmpty()) {
				throw new StorageException("Failed to store empty file " + fileName);
			}
			if (fileName.contains("..")) {
				// This is a security check
				throw new StorageException(
						"Cannot store file with relative path outside current directory "
								+ fileName);
			}
			try (InputStream inputStream = file.getInputStream()) {
				Files.copy(inputStream, this.profilepath.resolve(fileName),
					StandardCopyOption.REPLACE_EXISTING);
			}
		}
		catch (IOException e) {
			throw new StorageException("Failed to store file " + fileName, e);
		}
	}

  @Override 
  public Path loadProfile(String filename) { 
    
    return profilepath.resolve(filename); 
  }
	  
	  @Override public Resource loadAsResource(String filename) { 
      try { 
        Path file = loadProfile(filename); Resource resource = new UrlResource(file.toUri()); 
        if(resource.exists() || resource.isReadable()) { 
          return resource; 
        } else {
	      
          throw new StorageFileNotFoundException( "Could not read file: " + filename);
	      } 
      } catch (MalformedURLException e) { 
        throw new StorageFileNotFoundException("Could not read file: " + filename, e); 
      } 
    }

    @Override
    public Resource loadCertificate(Long uid, String filename)
    
      throws UnauthorizedFileAccessException {
      // if (!checkAuthForTeacher(uid)) {
      //   throw new UnauthorizedFileAccessException();
      // }
      Path storeLocation = Paths.get(certificatePath + File.separator + uid);
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
  
   
  @Override
  public void storeCertificates(Long uid, MultipartFile[] files)
    throws UnauthorizedFileAccessException {
    //Checking Content Type
    if (!checkAuthForTeacher(uid)) {
      System.out.println("User "+uid+" is uploding certificates");
      throw new UnauthorizedFileAccessException();
    }
    ImageFileValidator fileValidator = new ImageFileValidator();
    for (MultipartFile file : files) {
      if (!fileValidator.isSupportedContentType(file.getContentType())) {
        throw new NotImageFileException();
      }
    }
    Path storeLocation = Paths.get(certificatePath + File.separator + uid);
    
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
  public Stream<Path> loadAllCertificates(Long uid)
    throws UnauthorizedFileAccessException {
    // if (!checkAuthForTeacher(uid)) {
    //   throw new UnauthorizedFileAccessException();
    // }
    Path storeLocation = Paths.get(certificatePath + File.separator + uid);
    try {
      return Files
        .walk(storeLocation, 1)
        .filter(path -> !path.equals(storeLocation))
        .map(storeLocation::relativize);
    } catch (IOException e) {
      throw new RuntimeException("Could not load the files!");
    }
  }


    
  public void deleteCertificate(Long uid, String filename)
    throws IOException, UnauthorizedFileAccessException {

    if (!checkAuthForTeacher(uid)) {
      throw new UnauthorizedFileAccessException();
    }
    Path storeLocation = Paths.get(certificatePath + File.separator + uid);
    Path file = storeLocation.resolve(filename);
    Files.delete(file);
    System.out.println(filename+" is deleted");
  }

  @Override
  public boolean checkAuthForTeacher(Long uid) {
    UserAccount userAccount = userSessionService.getUserAccount();
    UserRole userRole = UserRole.strToUserRole(userAccount.getRole());
    if (
      userRole != UserRole.ADMIN &&
      userRole != UserRole.SUPER_ADMIN &&
      userRole != UserRole.TEACHER
    ) {
      //  if user is not one of the following, it will return false;
      //  Teacher, Admin, Super Admin
      //
      return false;
    }
    if (
      userRole == UserRole.TEACHER &&
      userAccount.getId() != uid
    ) {
      // This condition is to make sure, teacher is not accessing other teacher resources.
      return false;
    }
    return true;
  }
}
