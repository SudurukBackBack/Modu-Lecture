package com.sudurukBackBack.Modu_Lecture.global.util;

import com.sudurukBackBack.Modu_Lecture.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class UserValidator {

    private final UserRepository userRepository;

    public void emailAlreadyExist(String email) {
        boolean emailExists = userRepository.existsByEmail(email);

        if (emailExists) {
            throw new IllegalArgumentException("Email already exists");
        }
    }
}
