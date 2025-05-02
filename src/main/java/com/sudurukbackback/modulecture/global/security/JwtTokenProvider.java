package com.sudurukbackback.modulecture.global.security;

import com.sudurukbackback.modulecture.domain.auth.component.AuthComponent;
import com.sudurukbackback.modulecture.domain.user.entity.User;
import com.sudurukbackback.modulecture.global.security.util.JwtUtil;
import io.github.cdimascio.dotenv.Dotenv;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtTokenProvider {

    private static final String KEY_ROLE = "role";
    private static final long EXPIRATION_TIME = 60 * 60 * 1000L;
    private static final long REFRESH_EXPIRATION_TIME = 60 * 60 * 24 * 1000L;

    private final AuthComponent authComponent;

    private Key key;

    @PostConstruct
    public void init() {

        String secretKey = getSecretKey();
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
        JwtUtil.setKey(key);
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
     * @param email 사용자 이메일.
     * @return Jwt token, Refresh token
     */
    public Map<String, String> generateToken(String email) {

        User user = authComponent.findUserByEmail(email);

        // 사용자의 권한 문자열 추출
        List<String> roles = user.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

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

        // refresh 토큰 생성
        String refreshToken = Jwts.builder()
                .setSubject(user.getEmail())
                .setIssuedAt(now)
                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_EXPIRATION_TIME))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        return Map.of("access_token", token, "refresh_token", refreshToken);
    }
}
