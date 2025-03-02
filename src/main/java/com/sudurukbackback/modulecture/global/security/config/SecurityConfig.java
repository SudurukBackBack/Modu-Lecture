package com.sudurukbackback.modulecture.global.security.config;

import com.sudurukbackback.modulecture.global.security.JwtAuthenticationFilter;
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
                        .requestMatchers("/auth/sign-up", "/auth/sign-in").permitAll() // 인증 없이 접근 가능 API
                        .requestMatchers("/main", "/css/**", "/js/**", "/images/**", "/images/logo.svg", "/fragments/**", "/mypage/**", "/community/**").permitAll() // 인증 없이 접근 가능
                        .requestMatchers(HttpMethod.GET, "/posts/**").permitAll() // 커뮤니티 조회 기능만
                        .requestMatchers("/gold/**").hasRole("GOLD") // GOLD 이상만 접근 가능
                        .requestMatchers("/platinum/**").hasRole("PLATINUM") // PLATINUM만 접근 가능
                        .anyRequest().permitAll() // 개발 과정 한정 인증 무효화
                )
                .logout(logout -> logout
                        .logoutUrl("/auth/sign-out")
                        .logoutSuccessUrl("/main")
                        .invalidateHttpSession(true)
                        .deleteCookies("jwtToken")
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
