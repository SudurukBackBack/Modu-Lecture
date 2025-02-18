package com.sudurukBackBack.Modu_Lecture.domain.user.service;

import com.sudurukBackBack.Modu_Lecture.domain.user.dto.request.PasswordUpdateRequestDto;
import com.sudurukBackBack.Modu_Lecture.domain.user.dto.request.UserDeleteRequestDto;
import com.sudurukBackBack.Modu_Lecture.domain.user.entity.User;
import com.sudurukBackBack.Modu_Lecture.global.util.UserValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UserService {

    private final AuthService authService;
    private final PasswordEncoder passwordEncoder;
    private final UserValidator userValidator;

    @Transactional
    public void updatePassword(Authentication auth, PasswordUpdateRequestDto request) {
        // 본인 인증
        User user = authicateActiveUser(auth.getName(), request.getCurrentPassword());

        // 비밀번호 재설정
        user.changePassword(request.getNewPassword(), passwordEncoder);

        // TODO: 로그아웃 처리 -> 토큰 무효화
    }

    @Transactional
    public void deactivateAccount(Authentication auth, UserDeleteRequestDto request) {
        // 본인 인증
        User user = authicateActiveUser(auth.getName(), request.getCurrentPassword());

        user.deactivateAccount();
    }

    private User authicateActiveUser(String email, String password) {
        // 이메일 비밀번호 인증
        var user = authService.verifyEmailAndPassword(email, password);
        userValidator.validateUserIsActive(user);

        return user;
    }
}
