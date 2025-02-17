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

        var user = verifyEmailAndPassword(request.getEmail(), request.getPassword());

        // 계정 상태 확인 (Pending이면 Active로 복구)
        if (user.getUserStatus() == UserStatus.PENDING) {
            user.reactiveAccount();
        }

        // 탈퇴 완료된 계정으로 로그인 시
        if (user.getUserStatus() == UserStatus.DELETED) {
            throw new WrongAuthenticationException();
        }

        return user;
    }

    /**
     * 이메일 비밀번호 매칭 인증
     *
     * @param email
     * @param password
     * @return User
     */
    User verifyEmailAndPassword(String email, String password) {
        var user = findUserByEmail(email);
        validatePassword(password, user.getPassword());
        return user;
    }

    // 이메일로 UserEntity 불러오기
    private User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(WrongAuthenticationException::new);
    }

    // 비밀번호 검증
    private void validatePassword(String password1, String password2) {
        if (!passwordEncoder.matches(password1, password2)) {
            throw new WrongAuthenticationException();
        }
    }
}
