# 1. 빌드 단계 (Gradle 사용)
FROM eclipse-temurin:21-jdk-alpine AS build

# 2. 작업 디렉터리 설정
WORKDIR /app

# 3. Gradle Wrapper 및 설정 파일 복사
COPY gradlew ./
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .

# 4. 의존성 사전 다운로드 (빌드 속도 최적화)
RUN chmod +x gradlew && ./gradlew dependencies --no-daemon

# 5. 애플리케이션 소스 코드 복사
COPY src ./src

# 6. JAR 파일 빌드
RUN ./gradlew clean build -x test --no-daemon

# --- 배포 단계 ---
FROM eclipse-temurin:21-jdk-alpine

# 7. 작업 디렉터리 설정
WORKDIR /app

# 8. 빌드된 JAR 파일 복사
COPY --from=build /app/build/libs/*.jar app.jar

# 9. .env 파일 복사
COPY .env /app/.env

# 10. 컨테이너 시작 시 실행할 명령어
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
