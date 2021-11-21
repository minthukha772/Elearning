package com.blissstock.mappingSite;

import javax.annotation.Resource;

import com.blissstock.mappingSite.service.StorageService;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan("com.blissstock.mappingSite.entity")
@EnableJpaRepositories("com.blissstock.mappingSite.repository")
public class MappingSiteApplication implements CommandLineRunner{

  @Resource
  StorageService storageService;

  public static void main(String[] args) {
    SpringApplication.run(MappingSiteApplication.class, args);
  }

@Override
public void run(String... args) throws Exception {
	storageService.init();
}

 

}
