package com.sudurukbackback.modulecture.domain.user.service;

import com.sudurukbackback.modulecture.domain.auth.component.AuthComponent;
import com.sudurukbackback.modulecture.domain.community.repository.CommentRepository;
import com.sudurukbackback.modulecture.domain.community.repository.PostRepository;
import com.sudurukbackback.modulecture.domain.user.component.UserComponent;
import com.sudurukbackback.modulecture.domain.user.dto.request.PasswordUpdateRequestDto;
import com.sudurukbackback.modulecture.domain.user.dto.request.UserDeleteRequestDto;
import com.sudurukbackback.modulecture.domain.user.dto.response.UserProfileResponseDto;
import com.sudurukbackback.modulecture.domain.user.entity.User;
import com.sudurukbackback.modulecture.domain.user.entity.enums.ProfileField;
import com.sudurukbackback.modulecture.domain.user.entity.enums.UserGrade;
import com.sudurukbackback.modulecture.domain.user.entity.enums.UserStatus;
import com.sudurukbackback.modulecture.domain.user.exception.SamePasswordException;
import com.sudurukbackback.modulecture.domain.user.repository.UserRepository;
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
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthComponent authComponent;
    private final UserComponent userComponent;

    @Transactional
    public void updatePassword(Authentication auth, PasswordUpdateRequestDto request) {
        // 본인 인증
        User user = authenticateActiveUser(auth.getName(), request.getCurrentPassword());

        String originalPassword = user.getPassword();
        String newPassword = request.getNewPassword();

        // 기존의 비밀번호와 새 비밀번호가 일치한지 확인
        if (passwordEncoder.matches(newPassword, originalPassword)) {
            throw new SamePasswordException();
        }

        authComponent.encodePassword(newPassword);

        user.updateProfile(ProfileField.PASSWORD, newPassword);
    }

    @Transactional
    public void deactivateAccount(Authentication auth, UserDeleteRequestDto request) {
        // 본인 인증
        User user = authenticateActiveUser(auth.getName(), request.getCurrentPassword());

        user.deactivateAccount(UserStatus.PENDING);
    }

    public UserProfileResponseDto getUserProfile(String uuid) {
        // 사용자 정보 가져오기
        User user = userComponent.getUserByUuid(uuid);

        return UserProfileResponseDto.of(user.getEmail(), user.getNickname());
    }

    @Transactional
    public UserProfileResponseDto updateUserProfile(String uuid, String newNickname) {
        // 닉네임 중복 확인
        userComponent.validateNicknameUniqueness(newNickname);

        // 사용자 정보 가져오기
        User user = userComponent.getUserByUuid(uuid);
        user.updateProfile(ProfileField.NICKNAME, newNickname);

        return UserProfileResponseDto.of(user.getEmail(), user.getNickname());
    }

    /**
     * 사용자가 승급 요건을 충족했는지 확인하고 승급하는 메서드
     *
     * @param userId 사용자 ID
     */
    @Transactional
    public void checkUserGradeUp(Long userId) {
        // User 객체 가져오기
        User user = userComponent.getUserById(userId);
        UserGrade currentGrade = user.getGrade();

        // 골드 또는 플래티넘이면 승급 불가
        if (currentGrade == UserGrade.ROLE_GOLD || currentGrade == UserGrade.ROLE_PLATINUM) {
            return;
        }

        // 승급 조건 확인
        boolean canUpgrade = switch (currentGrade) {
            case ROLE_BRONZE -> canUpgradeToSilver(user);
            case ROLE_SILVER -> canUpgradeToGold(user);
            default -> false;
        };

        if (canUpgrade) {
            user.upgradeGrade();
        }
    }

    // 브론즈 → 실버 승급 조건
    private boolean canUpgradeToSilver(User user) {
        return postRepository.countByUserId(user.getId()) >= 10 &&
                commentRepository.countByUserId(user.getId()) >= 20;
    }

    // 실버 → 골드 승급 조건
    private boolean canUpgradeToGold(User user) {
        return postRepository.countByUserId(user.getId()) >= 50 &&
                commentRepository.countByUserId(user.getId()) >= 100;
    }

    // Batch (PENDING 상태인 계정 최종 탈퇴 처리)
    @Transactional
    public void batchDeactivateAccounts() {

        log.info("탈퇴 처리 대상 계정 조회 시작");

        // 탈퇴 처리 대상 계정 조회
        List<User> users = userRepository.findAllByUserStatusAndDeletedAtBefore(
                UserStatus.PENDING,
                LocalDateTime.now().minusWeeks(1)
        );
        log.info("탈퇴 처리 계정: {}개", users.size());

        users.forEach(user -> user.deactivateAccount(UserStatus.DELETED)); // 엔티티 상태 변경
        log.info("탈퇴 처리 작업 완료");
    }

    /**
     * ACTIVE 계정 로그인
     *
     * @param email 이메일
     * @param password 비밀번호
     * @return User
     */
    private User authenticateActiveUser(String email, String password) {
        // 이메일 비밀번호 인증
        var user = authComponent.verifyEmailAndPasswordMatch(email, password);
        userComponent.validateUserIsActive(user);

        return user;
    }
}
