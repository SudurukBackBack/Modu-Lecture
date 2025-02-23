package com.sudurukbackback.modulecture;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

import java.util.Objects;

@EnableScheduling
@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class ModuLectureApplication {

	public static void main(String[] args) {

		Dotenv dotenv = Dotenv.load();

		// RDS 인증 정보 설정
		System.setProperty("spring.datasource.url", Objects.requireNonNull(dotenv.get("RDS_URL")));
		System.setProperty("spring.datasource.username", Objects.requireNonNull(dotenv.get("RDS_USERNAME")));
		System.setProperty("spring.datasource.password", Objects.requireNonNull(dotenv.get("RDS_PASSWORD")));

		// S3 인증 정보 설정
		System.setProperty("cloud.aws.region", Objects.requireNonNull(dotenv.get("REGION")));
		System.setProperty("cloud.aws.s3.bucket-name", Objects.requireNonNull(dotenv.get("BUCKET_NAME")));
		System.setProperty("cloud.aws.credentials.access-key", Objects.requireNonNull(dotenv.get("ACCESS_KEY")));
		System.setProperty("cloud.aws.credentials.secret-key", Objects.requireNonNull(dotenv.get("SECRET_KEY")));

		SpringApplication.run(ModuLectureApplication.class, args);


	}

}
