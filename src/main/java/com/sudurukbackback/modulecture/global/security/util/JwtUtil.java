package com.sudurukbackback.modulecture.global.security.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Setter;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.security.Key;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@UtilityClass
public class JwtUtil {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";
    private static final String KEY_ROLE = "role";

    @Setter
    private static Key key;

    /**
     * JWT 토큰을 Authorization 헤더 또는 Cookie에서 가져오는 메서드
     */
    public String resolveToken(HttpServletRequest request, String cookieName) {
        // 1. Authorization 헤더에서 토큰 가져오기
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (bearerToken != null && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(BEARER_PREFIX.length());
        }

        // 2. Cookie에서 토큰 가져오기
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if (cookieName.equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }

        return null;
    }

    /**
     * 토큰에서 사용자 이름(식별자) 추출.
     *
     * @param token JWT 토큰.
     * @return 사용자 이름(식별자).
     */
    public String getUsername(String token) {

        String uuid = parseClaims(token).getSubject();
        log.debug("Extracted username from JWT: {}", uuid);

        return uuid;
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

    public long getExpiration(String token) {
        Claims claims = parseClaims(token);
        return claims.getExpiration().getTime() - System.currentTimeMillis();
    }

    /**
     * JWT 토큰에서 Claims 추출.
     *
     * @param token JWT 토큰.
     * @return Claims 객체.
     */
    public Claims parseClaims(String token) {

        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

        } catch (ExpiredJwtException e) {
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
            return !claims.getExpiration().before(new Date());

        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}
