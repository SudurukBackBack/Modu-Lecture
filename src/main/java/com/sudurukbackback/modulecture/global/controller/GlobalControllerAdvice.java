package com.sudurukbackback.modulecture.global.controller;

import com.sudurukbackback.modulecture.domain.user.entity.User;
import com.sudurukbackback.modulecture.global.security.JwtTokenProvider;
import com.sudurukbackback.modulecture.global.security.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@RequiredArgsConstructor
@ControllerAdvice
public class GlobalControllerAdvice {

    private final JwtTokenProvider jwtTokenProvider;

    @ModelAttribute
    public void addAuthInfoToModel(HttpServletRequest request, Model model) {
        String token = JwtUtil.resolveToken(request);

        if (token != null && JwtUtil.validateToken(token)) {
            // JWT에서 사용자 인증 정보 추출
            Authentication authentication = jwtTokenProvider.getAuthentication(token);
            User user = (User) authentication.getPrincipal();

            // Thymeleaf에서 사용할 로그인 정보 전달
            model.addAttribute("isAuthenticated", true);
            model.addAttribute("username", user.getNickname());
        } else {
            // 로그인되지 않은 경우
            model.addAttribute("isAuthenticated", false);
            model.addAttribute("username", null);
        }
    }
}
