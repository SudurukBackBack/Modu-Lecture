package com.sudurukBackBack.Modu_Lecture.domain.user.service;

import com.sudurukBackBack.Modu_Lecture.domain.user.dto.request.PasswordUpdateRequestDto;
import com.sudurukBackBack.Modu_Lecture.domain.user.dto.request.UserDeleteRequestDto;
import com.sudurukBackBack.Modu_Lecture.domain.user.entity.User;
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

    @Transactional
    public void updatePassword(Authentication auth, PasswordUpdateRequestDto request) {

        // 로그인을 통한 본인 인증
        var user = authService.authenticationUser(
                auth.getName(), request.getCurrentPassword());

        // 비밀번호 재설정
        user.changePassword(request.getNewPassword(), passwordEncoder);

        // TODO: 로그아웃 처리 -> 토큰 무효화
    }


    @Transactional
    public void deleteUser(Authentication auth, UserDeleteRequestDto request) {

        // 본인 인증
        User user = authService.authenticationUser(
                auth.getName(), request.getCurrentPassword());

        user.deleteUser();
    }
}
