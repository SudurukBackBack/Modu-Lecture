package com.sudurukBackBack.Modu_Lecture.domain.user.service;

import com.sudurukBackBack.Modu_Lecture.domain.user.dto.request.UserRegistrationRequestDto;
import com.sudurukBackBack.Modu_Lecture.domain.user.entity.User;
import com.sudurukBackBack.Modu_Lecture.domain.user.repository.UserRepository;
import com.sudurukBackBack.Modu_Lecture.global.util.UserValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class AuthService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserValidator userValidator;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(email));
    }

    @Transactional
    public User signUp(UserRegistrationRequestDto request) {

        String email = request.getEmail();
        String password = request.getPassword();
        String username = request.getUsername();

        // email 가입 가능 여부 확인
        userValidator.emailAlreadyExist(email);

        return userRepository.save(User.builder()
                .email(email)
                .password(passwordEncoder.encode(password))
                .username(username)
                .grade(0)
                .userStatus(1)
                .createdAt(LocalDateTime.now())
                .build());
    }

}
