package com.sudurukBackBack.Modu_Lecture.global.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

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

        logger.info("Starting JWT authentication filter");

        // Extract token
        String token = resolveToken(request);

        if (token != null) {
            logger.info("JWT Token extracted");

            // Validate token
            if (jwtTokenProvider.validateToken(token)) {
                logger.info("JWT Token is valid");

                // Retrieve Authentication object
                Authentication auth = jwtTokenProvider.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(auth);
                logger.info("Authentication set for user");
            } else {
                logger.warn("Invalid or expired JWT token");
            }
        } else {
            logger.warn("No JWT token found in the request");
        }

        // Continue with the filter chain
        filterChain.doFilter(request, response);

        logger.info("JWT authentication filter processing completed");
    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        logger.info("Authorization Header");

        if (bearerToken != null && bearerToken.startsWith(BEARER_PREFIX)) {
            String token = bearerToken.substring(BEARER_PREFIX.length());
            logger.info("Extracted Bearer Token");
            return token;
        }

        logger.info("No Bearer Token found in the Authorization header");
        return null;
    }
}
