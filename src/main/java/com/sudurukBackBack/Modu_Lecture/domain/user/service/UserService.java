package com.sudurukBackBack.Modu_Lecture.domain.user.service;

import com.sudurukBackBack.Modu_Lecture.domain.user.dto.request.UserDeleteRequestDto;
import com.sudurukBackBack.Modu_Lecture.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UserService {

    private final AuthService authService;

    @Transactional
    public void deleteUser(Authentication auth, UserDeleteRequestDto request) {

        // 본인 인증
        User user = authService.authenticationUser(
                auth.getName(), request.getCurrentPassword());

        user.deleteUser();
    }
}
