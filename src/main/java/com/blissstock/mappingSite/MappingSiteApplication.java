package com.blissstock.mappingSite;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan("com.blissstock.mappingsite")
// @EntityScan("com.blissstock.mappingsite.entity")
// @EnableJpaRepositories("com.blissstock.mappingsite.repository")
public class MappingSiteApplication {

	public static void main(String[] args) {
		SpringApplication.run(MappingSiteApplication.class, args);
	}

}
