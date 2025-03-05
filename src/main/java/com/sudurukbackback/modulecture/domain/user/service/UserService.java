package com.sudurukbackback.modulecture.domain.user.service;

import com.sudurukbackback.modulecture.domain.user.component.AuthComponent;
import com.sudurukbackback.modulecture.domain.user.component.UserValidator;
import com.sudurukbackback.modulecture.domain.user.dto.request.PasswordUpdateRequestDto;
import com.sudurukbackback.modulecture.domain.user.dto.request.UserDeleteRequestDto;
import com.sudurukbackback.modulecture.domain.user.dto.response.UserProfileResponseDto;
import com.sudurukbackback.modulecture.domain.user.entity.User;
import com.sudurukbackback.modulecture.domain.user.entity.enums.UserStatus;
import com.sudurukbackback.modulecture.domain.user.exception.SameNicknameException;
import com.sudurukbackback.modulecture.domain.user.repository.UserRepository;
import com.sudurukbackback.modulecture.global.exception.BasicServerException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
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

    public UserProfileResponseDto getUserProfile(String email) {
        // 사용자 정보 가져오기
        User user = getUserByEmail(email);

        return UserProfileResponseDto.of(user.getEmail(), user.getNickname());
    }

    @Transactional
    public UserProfileResponseDto updateUserProfile(String email, String newNickname) {
        // 닉네임 중복 여부 확인
        if (userRepository.existsByNickname(newNickname)) {
            throw new SameNicknameException();
        }

        // 사용자 정보 가져오기
        User user = getUserByEmail(email);
        user.changeNickname(newNickname);

        return UserProfileResponseDto.of(user.getEmail(), user.getNickname());
    }

    // Batch (PENDING 상태인 계정 최종 탈퇴 처리)
    @Transactional
    public void deactivateAccountBatch() {

        log.info("탈퇴 처리 대상 계정 조회 시작");

        // 탈퇴 처리 대상 계정 조회
        List<User> users = userRepository.findAllByUserStatusAndDeletedAtBefore(
                UserStatus.PENDING,
                LocalDateTime.now().minusWeeks(1)
        );
        log.info("탈퇴 처리 계정: {}개", users.size());

        users.forEach(User::deactivateAccount); // 엔티티 상태 변경
        log.info("탈퇴 처리 작업 완료");
    }

    /**
     * ACTIVE 계정 로그인
     *
     * @param email    이메일
     * @param password 비밀번호
     * @return User
     */
    private User authenticateActiveUser(String email, String password) {
        // 이메일 비밀번호 인증
        var user = authComponent.verifyEmailAndPasswordMatch(email, password);
        userValidator.validateUserIsActive(user);

        return user;
    }

    /**
     * 이메일을 사용해 User 가져오기
     *
     * @param email 이메일
     * @return User
     */
    private User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(BasicServerException::new);
    }
}
