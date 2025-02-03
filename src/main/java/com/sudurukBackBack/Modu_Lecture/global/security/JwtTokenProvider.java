package com.sudurukBackBack.Modu_Lecture.global.security;

import com.sudurukBackBack.Modu_Lecture.domain.user.entity.User;
import com.sudurukBackBack.Modu_Lecture.domain.user.service.AuthService;
import io.github.cdimascio.dotenv.Dotenv;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtTokenProvider {

    private final AuthService authService;

    private static final String KEY_ROLE = "role";
    private static final long EXPIRATION_TIME = 60 * 60 * 24 * 7; // 7 hours

    private Key key;

    @PostConstruct
    public void init() {
        String secretKey = getSecretKey();
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));

        log.info("JWT Secret Key initialized successfully.");
    }

    // `.env`에서 secretKey 추출
    private String getSecretKey() {

        Dotenv dotenv = Dotenv.load();
        String secretKey = dotenv.get("JWT_SECRET");

        if (secretKey == null || secretKey.isEmpty()) {
            log.error("JWT_SECRET is not defined in environment variables.");
            throw new IllegalStateException("JWT_SECRET is not defined in the environment variables.");
        }

        if (secretKey.getBytes(StandardCharsets.UTF_8).length < 32) {
            log.error("JWT_SECRET must be at least 32 bytes long.");
            throw new IllegalArgumentException("JWT_SECRET must be at least 32 bytes long.");
        }

        return secretKey;
    }

    /**
     * JWT 토큰 생성.
     *
     * @param user 사용자 이름.
     * @return 생성된 JWT 토큰 문자열.
     */
    public String generateToken(User user) {

        List<String> roles = user.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        String email = user.getEmail();

        // 사용자 정보 추가
        var claims = Jwts.claims().setSubject(email);
        claims.put(KEY_ROLE, roles);

        // 토큰 만료 시간 설정
        var now = new Date();
        var expirationDate = new Date(now.getTime() + EXPIRATION_TIME);

        // 토큰 생성
        String token = Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expirationDate)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        log.info("JWT Token generated for user: {} (Expires: {})", email, expirationDate);
        return token;
    }

    /**
     * JWT 토큰으로부터 인증 객체 생성.
     *
     * @param token JWT 토큰.
     * @return 인증(Authentication) 객체.
     */
    public Authentication getAuthentication(String token) {

        String username = getUsername(token);
        UserDetails userDetails = authService.loadUserByUsername(username);

        List<GrantedAuthority> authorities = getAuthorities(token);
        log.info("Authentication created for user: {}", username);

        return new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
    }

    /**
     * 토큰에서 사용자 이름(식별자) 추출.
     *
     * @param token JWT 토큰.
     * @return 사용자 이름(식별자).
     */
    public String getUsername(String token) {

        String username = parseClaims(token).getSubject();
        log.debug("Extracted username from JWT: {}", username);

        return username;
    }

    /**
     * JWT 토큰에서 권한 정보 추출.
     *
     * @param token JWT 토큰.
     * @return 권한 리스트.
     */
    public List<GrantedAuthority> getAuthorities(String token) {

        // Claims에서 roles 추출
        Claims claims = parseClaims(token);
        Object rolesObject = claims.get(KEY_ROLE);

        List<String> roles;

        if (rolesObject instanceof List) {
            roles = ((List<?>) rolesObject).stream()
                    .filter(item -> item instanceof String)
                    .map(item -> (String) item)
                    .toList();
        } else if (rolesObject instanceof String) {
            roles = List.of((String) rolesObject);
        } else {
            roles = new ArrayList<>();
        }
        log.debug("Extracted roles from JWT: {}", roles);

        return roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    /**
     * JWT 토큰에서 Claims 추출.
     *
     * @param token JWT 토큰.
     * @return Claims 객체.
     */
    private Claims parseClaims(String token) {

        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

        } catch (ExpiredJwtException e) {
            log.warn("JWT Token expired: {}", e.getClaims().getSubject());
            return e.getClaims();

        } catch (JwtException e) {
            log.error("Invalid JWT Token: {}", token);
            throw new IllegalArgumentException("Invalid JWT token");
        }
    }

    /**
     * JWT 토큰 유효성 검증.
     *
     * @param token JWT 토큰.
     * @return 유효한 토큰이면 true, 그렇지 않으면 false.
     */
    public boolean validateToken(String token) {

        try {
            // Claims를 파싱하여 만료 시간 확인
            Claims claims = parseClaims(token);
            boolean isValid = !claims.getExpiration().before(new Date());

            if (isValid) {
                log.info("JWT Token is valid for user: {}", claims.getSubject());
            } else {
                log.warn("JWT Token has expired for user: {}", claims.getSubject());
            }

            return isValid;

        } catch (JwtException | IllegalArgumentException e) {
            log.error("JWT Token validation failed: {}", token);
            return false;
        }
    }
}
