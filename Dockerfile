# 1. 빌드 환경 (멀티 스테이지 빌드 적용)
FROM gradle:8.5-jdk21 AS build

# 2. 작업 디렉토리 설정
WORKDIR /app

# 3. 프로젝트 파일 복사 (의존성 캐시 최적화)
COPY gradlew gradlew
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .

# 4. 의존성 미리 다운로드 (캐싱 활용)
RUN chmod +x gradlew && ./gradlew dependencies --no-daemon

# 5. 애플리케이션 소스 코드 복사 후 빌드
COPY src src
RUN ./gradlew clean build -x test --no-daemon

# --- 배포 단계 ---
FROM openjdk:21-jdk-slim

# 6. 작업 디렉토리 설정
WORKDIR /app

# 7. 빌드된 JAR 파일 복사
COPY --from=build /app/build/libs/*.jar app.jar
COPY --from=build /app/src/main/resources/templates/ /app/templates/

# 8. .env 파일 복사
COPY .env /app/.env

# 9. 포트 설정 (Spring Boot 기본 포트 8080)
EXPOSE 8080

# 10. 실행 명령어
ENTRYPOINT ["java", "-jar", "/app/app.jar"]