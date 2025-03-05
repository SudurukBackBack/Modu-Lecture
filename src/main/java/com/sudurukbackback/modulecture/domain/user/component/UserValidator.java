package com.sudurukbackback.modulecture.domain.user.component;

import com.sudurukbackback.modulecture.domain.user.entity.User;
import com.sudurukbackback.modulecture.domain.user.entity.enums.UserStatus;
import com.sudurukbackback.modulecture.domain.user.exception.AccountNotActiveException;
import com.sudurukbackback.modulecture.domain.user.exception.SameNicknameException;
import com.sudurukbackback.modulecture.domain.user.exception.WrongAuthenticationException;
import com.sudurukbackback.modulecture.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class UserValidator {

    private final UserRepository userRepository;

    // 닉네임 중복 체크
    public void validateNicknameUniqueness(String nickname) {
        boolean emailExists = userRepository.existsByNickname(nickname);

        if (emailExists) {
            throw new SameNicknameException();
        }
    }

    // 계좌 활성화 여부 확인
    public void validateUserIsActive(User user) {
        if (user.getUserStatus() != UserStatus.ACTIVE) {
            throw new AccountNotActiveException();
        }
    }

    // 로그인 계정 상태 확인
    public void validateUserStatus(User user) {
        // Pending: Active 변환 (탈퇴 요청 철회)
        if (user.getUserStatus() == UserStatus.PENDING) {
            user.reactiveAccount();
            userRepository.save(user);

        // Deleted: 로그인 거부
        } else if (user.getUserStatus() == UserStatus.DELETED) {
            throw new WrongAuthenticationException();

        // Active 제외한 나머지: 로그인 거부 -> 관리자 문의로 유도
        } else if (user.getUserStatus() != UserStatus.ACTIVE) {
            throw new AccountNotActiveException();
        }
    }
}
