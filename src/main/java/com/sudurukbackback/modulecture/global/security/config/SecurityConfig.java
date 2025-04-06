package com.sudurukbackback.modulecture.global.security.config;

import com.sudurukbackback.modulecture.global.security.JwtAuthenticationFilter;
import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity(prePostEnabled = true)
@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtAuthenticationFilter jwtAuthenticationFilter) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/sign-up", "/api/auth/sign-in", "api/auth/refresh", "/web/auth/**").permitAll()
                        .requestMatchers("/css/**", "/js/**", "/images/**", "/images/logo.svg", "/fragments/**").permitAll()
                        .requestMatchers("/main", "/community/**", "/lecture/**", "/api/enroll/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/posts/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/comments/**").permitAll()
                        .requestMatchers("/test/**").permitAll()
                        .anyRequest().authenticated()
                )
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint((request, response, authException) -> {
                            boolean hasAccessToken = false;
                            boolean hasRefreshToken = false;

                            if (request.getCookies() != null) {
                                for (Cookie cookie : request.getCookies()) {
                                    if ("access".equals(cookie.getName())) {
                                        hasAccessToken = true;
                                    }
                                    if ("refresh".equals(cookie.getName())) {
                                        hasRefreshToken = true;
                                    }
                                }
                            }

                            if (hasAccessToken && hasRefreshToken) {
                                String originalUri = request.getRequestURI();
                                response.sendRedirect("/web/auth/refresh?redirect=" + originalUri);
                            } else {
                                response.sendRedirect("/web/auth/login?error=unauthorized");
                            }
                        })
                )
                .logout(logout -> logout
                        .logoutUrl("/logoutUrl")
                        .logoutSuccessUrl("/main")
                        .invalidateHttpSession(true)
                        .deleteCookies("access")
                        .deleteCookies("refresh")
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .httpBasic(AbstractHttpConfigurer::disable);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
