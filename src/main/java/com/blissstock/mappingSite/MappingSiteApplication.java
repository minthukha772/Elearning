package com.blissstock.mappingSite;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan("com.blissstock.entity")
//@EnableJpaRepositories("com.blissstock.repository")
public class MappingSiteApplication {

	public static void main(String[] args) {
		SpringApplication.run(MappingSiteApplication.class, args);
	}

}
