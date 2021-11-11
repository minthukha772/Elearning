package com.blissstock.mappingSite;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan("com.blissstock.mappingSite.entity")
@EnableJpaRepositories("com.blissstock.mappingSite.repository")
public class MappingSiteApplication {

	public static void main(String[] args) {
		SpringApplication.run(MappingSiteApplication.class, args);
	}

}
