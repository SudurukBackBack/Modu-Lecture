package com.sudurukBackBack.Modu_Lecture.domain.user.service;

import com.sudurukBackBack.Modu_Lecture.domain.user.dto.request.UserLoginRequestDto;
import com.sudurukBackBack.Modu_Lecture.domain.user.dto.request.UserRegistrationRequestDto;
import com.sudurukBackBack.Modu_Lecture.domain.user.entity.User;
import com.sudurukBackBack.Modu_Lecture.domain.user.entity.enums.UserGrade;
import com.sudurukBackBack.Modu_Lecture.domain.user.entity.enums.UserStatus;
import com.sudurukBackBack.Modu_Lecture.domain.user.exception.WrongAuthenticationException;
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

        String email = request.getEmail().toLowerCase();
        String password = request.getPassword();
        String username = request.getUsername();

        // email 가입 가능 여부 확인
        userValidator.validateEmailUniqueness(email);

        return userRepository.save(User.builder()
                .email(email)
                .password(passwordEncoder.encode(password))
                .username(username)
                .grade(UserGrade.ROLE_BRONZE)
                .userStatus(UserStatus.ACTIVE)
                .createdAt(LocalDateTime.now())
                .build());
    }

    public User authenticate(UserLoginRequestDto request) {

        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(WrongAuthenticationException::new);

        // 계정이 활성화 상태인지 확인
        userValidator.validateUserIsActive(user);

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new WrongAuthenticationException();
        }

        return user;
    }

}
