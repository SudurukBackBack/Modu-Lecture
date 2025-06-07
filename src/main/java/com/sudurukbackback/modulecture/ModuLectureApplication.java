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

		System.setProperty("naver.client-id", Objects.requireNonNull(dotenv.get("NAVER_CLIENT_ID")));
		System.setProperty("naver.client-secret", Objects.requireNonNull(dotenv.get("NAVER_CLIENT_SECRET")));
		System.setProperty("naver.redirect-uri", Objects.requireNonNull(dotenv.get("NAVER_REDIRECT_URI")));
		System.setProperty("naver.auth-grant-type", Objects.requireNonNull(dotenv.get("NAVER_AUTH_GRANT_TYPE")));
		System.setProperty("naver.scope", Objects.requireNonNull(dotenv.get("NAVER_SCOPE")));
		System.setProperty("naver.auth-uri", Objects.requireNonNull(dotenv.get("NAVER_AUTH_URI")));
		System.setProperty("naver.token-uri", Objects.requireNonNull(dotenv.get("NAVER_TOKEN_URI")));
		System.setProperty("naver.user-info-uri", Objects.requireNonNull(dotenv.get("NAVER_USER_INFO_URI")));
		System.setProperty("naver.user-name-attribute", Objects.requireNonNull(dotenv.get("NAVER_USER_NAME_ATTRIBUTE")));

		SpringApplication.run(ModuLectureApplication.class, args);

	}

}