package com.sudurukBackBack.Modu_Lecture.domain.user.service;

import com.sudurukBackBack.Modu_Lecture.domain.user.dto.request.UserDeleteRequestDto;
import com.sudurukBackBack.Modu_Lecture.domain.user.entity.User;
import com.sudurukBackBack.Modu_Lecture.domain.user.exception.UserNotExistException;
import com.sudurukBackBack.Modu_Lecture.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    public void deleteUser(Authentication auth, UserDeleteRequestDto request) {

        User user = findUserByEmail(auth.getName());
        user.deleteUser();
    }

    private User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(UserNotExistException::new);
    }
}
