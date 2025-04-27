package com.sudurukbackback.modulecture.domain.user.service;

import com.sudurukbackback.modulecture.domain.user.component.AuthComponent;
import com.sudurukbackback.modulecture.domain.user.component.UserValidator;
import com.sudurukbackback.modulecture.domain.user.dto.request.UserLoginRequestDto;
import com.sudurukbackback.modulecture.domain.user.dto.request.UserRegistrationRequestDto;
import com.sudurukbackback.modulecture.domain.user.entity.User;
import com.sudurukbackback.modulecture.domain.user.entity.enums.UserGrade;
import com.sudurukbackback.modulecture.domain.user.entity.enums.UserStatus;
import com.sudurukbackback.modulecture.domain.user.exception.WrongAuthenticationException;
import com.sudurukbackback.modulecture.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AuthService implements UserDetailsService {

    private final UserRepository userRepository;
    private final AuthComponent authComponent;
    private final UserValidator userValidator;
    private final LoginAttemptService loginAttemptService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(email));
    }

    @Transactional
    public User signUp(UserRegistrationRequestDto request) {

        String email = request.getEmail().toLowerCase();
        String password = request.getPassword();

        // 닉네임 미설정 시 임의의 닉네임 부여
        String nickname = Optional.ofNullable(request.getNickname())
                .filter(n -> !n.isEmpty())
                .orElse("User" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));

        // email 가입 가능 여부 확인
        authComponent.validateEmailUniqueness(email);
        // 닉네임 중복 확인
        userValidator.validateNicknameUniqueness(nickname);

        return userRepository.save(User.builder()
                .email(email)
                .password(authComponent.encodePassword(password))
                .nickname(nickname)
                .grade(UserGrade.ROLE_BRONZE)
                .userStatus(UserStatus.ACTIVE)
                .createdAt(LocalDateTime.now())
                .build());
    }

    @Transactional
    public User signIn(UserLoginRequestDto request) {

        String email = request.getEmail().toLowerCase();

        if (!loginAttemptService.checkAndIncrementLoginAttempts(email)) {
            long remainingTime = loginAttemptService.getRemainingLockoutTime(email);
            throw new WrongAuthenticationException(remainingTime);
        }

        var user = authComponent.verifyEmailAndPasswordMatch(email, request.getPassword());
        userValidator.validateUserStatus(user);

        // 로그인 성공 시 시도 횟수 초기화
        loginAttemptService.resetLoginAttempts(email);

        return user;
    }
}
