package com.sudurukBackBack.Modu_Lecture.global.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        log.info("Starting JWT authentication filter: [{}] {}", request.getMethod(), request.getRequestURI());

        // Extract token
        String token = resolveToken(request);

        if (token != null) {
            log.debug("Extracted JWT token");

            // Validate token
            if (jwtTokenProvider.validateToken(token)) {
                log.info("JWT Token is valid");

                // Retrieve Authentication object
                Authentication auth = jwtTokenProvider.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(auth);
                log.info("🔑 Authentication set for user: {}", auth.getName());

            } else {
                log.warn("Invalid or expired JWT token");
            }
        } else {
            log.warn("No JWT token found in the request");
        }

        // Continue with the filter chain
        filterChain.doFilter(request, response);

        log.debug("JWT authentication filter processing completed");
    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);

        if (bearerToken != null && bearerToken.startsWith(BEARER_PREFIX)) {
            String token = bearerToken.substring(BEARER_PREFIX.length());
            log.debug("Extracted Bearer Token");

            return token;
        }

        log.warn("No Bearer Token found in the Authorization header");
        return null;
    }
}
