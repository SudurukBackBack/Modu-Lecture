package com.sudurukbackback.modulecture.global.security;

import com.sudurukbackback.modulecture.domain.auth.service.AuthService;
import com.sudurukbackback.modulecture.global.security.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final StringRedisTemplate redisTemplate;
    private final AuthService authService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        String token = JwtUtil.resolveToken(request, "access");

        // 블랙리스트 토큰인지 확인
        if (token != null && redisTemplate.hasKey("BL:" + token)) {
            log.info("로그아웃된 토큰으로 접근 시도: {}", token);
            SecurityContextHolder.clearContext();

            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"Invalid token.\"}");
            return;
        }

        // 유효한 토큰인지 확인 후 SecurityContext에 등록
        if (token != null && JwtUtil.validateToken(token)) {
            Authentication auth = authService.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(auth);
            log.debug("인증 성공: {}", auth.getName());
        }

        filterChain.doFilter(request, response);
    }
}
