package com.sudurukbackback.modulecture.domain.auth.service;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Map;

@Service
public class CookieService {

    private static final String ACCESS_TOKEN = "access";
    private static final String REFRESH_TOKEN = "refresh";

    /**
     * 지정된 속성을 가진 HTTP 전용(HTTP-only) 및 보안(secure) 쿠키를 생성합니다.
     *
     * @param cookieName 쿠키의 종류 (access or refresh)
     * @param token      쿠키에 저장될 token 값
     * @param hours      쿠키의 유효 시간 (시간 단위)
     * @return 생성된 쿠키를 나타내는 ResponseCookie 인스턴스
     */
    private ResponseCookie createCookie(String cookieName, String token, int hours) {

        return ResponseCookie.from(cookieName, token)
                .httpOnly(true)
                .secure(true)
                .sameSite("Strict")
                .path("/")
                .maxAge(Duration.ofHours(hours))
                .build();
    }

    /**
     * 지정된 특성을 가진 HTTP 전용(HTTP-only) 및 보안(secure) 접근 토큰과 리프레시 토큰 쿠키를 생성합니다.
     *
     * @param accessToken  접근 토큰 쿠키에 저장될 토큰 값
     * @param refreshToken 리프레시 토큰 쿠키에 저장될 토큰 값
     * @return "access_cookie"와 "refresh_cookie"를 키로 갖고, 각각 해당하는 쿠키를 값으로 갖는 맵
     */
    public Map<String, ResponseCookie> createCookies(String accessToken, String refreshToken) {

        var accessTokenCookie = createCookie(ACCESS_TOKEN, accessToken, 24);
        var refreshTokenCookie = createCookie(REFRESH_TOKEN, refreshToken, 48);

        return Map.of("access_cookie", accessTokenCookie, "refresh_cookie", refreshTokenCookie);
    }

    /**
     * 제공된 접근 및 리프레시 쿠키를 HTTP 응답 헤더에 추가합니다.
     *
     * @param response HTTP 응답 객체 (쿠키가 추가될 대상)
     * @param accessCookie 헤더에 추가될 접근 토큰 쿠키
     * @param refreshCookie 헤더에 추가될 리프레시 토큰 쿠키
     */
    public void setCookiesInHttpHeader(
            HttpServletResponse response,
            ResponseCookie accessCookie,
            ResponseCookie refreshCookie
    ) {
        // 쿠키 설정
        response.addHeader(HttpHeaders.SET_COOKIE, accessCookie.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());
    }
}
