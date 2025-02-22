package com.sudurukbackback.modulecture;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@EnableScheduling
@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class ModuLectureApplication {

	public static void main(String[] args) {
		SpringApplication.run(ModuLectureApplication.class, args);
	}

}
