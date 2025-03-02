package com.sudurukbackback.modulecture.global.controller;

import com.sudurukbackback.modulecture.global.security.JwtTokenProvider;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@RequiredArgsConstructor
@ControllerAdvice
public class GlobalControllerAdvice {

    private final JwtTokenProvider jwtTokenProvider;

    @ModelAttribute
    public void addAuthInfoToModel(HttpServletRequest request, Model model) {
        String token = getJwtFromCookie(request);

        if (token != null && jwtTokenProvider.validateToken(token)) {
            // JWT에서 사용자 인증 정보 추출
            Authentication authentication = jwtTokenProvider.getAuthentication(token);
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            // Thymeleaf에서 사용할 로그인 정보 전달
            model.addAttribute("isAuthenticated", true);
            model.addAttribute("username", userDetails.getUsername().substring(0, userDetails.getUsername().indexOf("@")));
        } else {
            // 로그인되지 않은 경우
            model.addAttribute("isAuthenticated", false);
            model.addAttribute("username", null);
        }
    }

    private String getJwtFromCookie(HttpServletRequest request) {
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("jwtToken".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}
