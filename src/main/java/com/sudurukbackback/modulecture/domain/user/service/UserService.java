package com.sudurukbackback.modulecture.domain.user.service;

import com.sudurukbackback.modulecture.domain.user.component.AuthComponent;
import com.sudurukbackback.modulecture.domain.user.component.UserValidator;
import com.sudurukbackback.modulecture.domain.user.dto.request.PasswordUpdateRequestDto;
import com.sudurukbackback.modulecture.domain.user.dto.request.UserDeleteRequestDto;
import com.sudurukbackback.modulecture.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final AuthComponent authComponent;
    private final UserValidator userValidator;

    @Transactional
    public void updatePassword(Authentication auth, PasswordUpdateRequestDto request) {
        // 본인 인증
        User user = authenticateActiveUser(auth.getName(), request.getCurrentPassword());

        // 비밀번호 재설정
        user.changePassword(request.getNewPassword(), passwordEncoder);

        // TODO: 로그아웃 처리 -> 토큰 무효화
    }

    @Transactional
    public void deactivateAccount(Authentication auth, UserDeleteRequestDto request) {
        // 본인 인증
        User user = authenticateActiveUser(auth.getName(), request.getCurrentPassword());

        user.requestDeactivateAccount();
    }

    private User authenticateActiveUser(String email, String password) {
        // 이메일 비밀번호 인증
        var user = authComponent.verifyEmailAndPasswordMatch(email, password);
        userValidator.validateUserIsActive(user);

        return user;
    }
}
