package com.sudurukBackBack.Modu_Lecture.global.util;

import com.sudurukBackBack.Modu_Lecture.domain.user.entity.User;
import com.sudurukBackBack.Modu_Lecture.domain.user.entity.enums.UserStatus;
import com.sudurukBackBack.Modu_Lecture.domain.user.exception.AccountNotActiveException;
import com.sudurukBackBack.Modu_Lecture.domain.user.exception.EmailAlreadyExistsException;
import com.sudurukBackBack.Modu_Lecture.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class UserValidator {

    private final UserRepository userRepository;

    public void validateEmailUniqueness(String email) {
        boolean emailExists = userRepository.existsByEmail(email);

        if (emailExists) {
            throw new EmailAlreadyExistsException();
        }
    }

    // 계좌 활성화 여부 확인
    public void validateUserIsActive(User user) {
        if (user.getUserStatus() != UserStatus.ACTIVE) {
            throw new AccountNotActiveException();
        }
    }
}
