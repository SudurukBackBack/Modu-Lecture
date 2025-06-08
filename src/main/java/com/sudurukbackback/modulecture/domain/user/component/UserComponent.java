package com.sudurukbackback.modulecture.domain.user.component;

import com.sudurukbackback.modulecture.domain.auth.exception.WrongAuthenticationException;
import com.sudurukbackback.modulecture.domain.user.entity.User;
import com.sudurukbackback.modulecture.domain.user.entity.enums.UserStatus;
import com.sudurukbackback.modulecture.domain.user.exception.AccountNotActiveException;
import com.sudurukbackback.modulecture.domain.user.exception.EmailAlreadyExistsException;
import com.sudurukbackback.modulecture.domain.user.exception.SameNicknameException;
import com.sudurukbackback.modulecture.domain.user.repository.UserRepository;
import com.sudurukbackback.modulecture.global.exception.BasicServerException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@RequiredArgsConstructor
@Component
public class UserComponent {

    private final UserRepository userRepository;

    /**
     * 이메일을 사용해 User 가져오기
     *
     * @param email 이메일
     * @return User
     */
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(BasicServerException::new);
    }

    /**
     * ID를 사용해 User 가져오기
     *
     * @param id 사용자 ID
     * @return User
     */
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(BasicServerException::new);
    }

    /**
     * ID를 사용해 User 가져오기
     *
     * @param uuid UUID
     * @return User
     */
    public User getUserByUuid(String uuid) {
        return userRepository.findByUuid(uuid)
                .orElseThrow(BasicServerException::new);
    }

    /**
     * 이메일 중복 확인
     *
     * @param email 이메일
     */
    private void validateEmailUniqueness(String email) {
        boolean emailExists = userRepository.existsByEmail(email);

        if (emailExists) {
            throw new EmailAlreadyExistsException();
        }
    }

    /**
     * 입력된 nickname이 없을 경우 이메일에서 추출하여 적용 (일반 사용자용)
     *
     * @param nickname 입력한 nickname
     * @param email 사용자의 이메일 주소
     * @return nickname (입력된 경우), 또는 이메일에서 추출한 닉네임 (입력되지 않은 경우)
     */
    public String generateNicknameFromEmail(String nickname, String email) {
        return Optional.ofNullable(nickname)
                .filter(n -> !n.isEmpty())
                .orElseGet(() -> email.substring(0, email.indexOf("@")));
    }

    /**
     * 닉네임 중복 확인
     *
     * @param nickname 닉네임
     */
    public void validateNicknameUniqueness(String nickname) {
        boolean nicknameExists = userRepository.existsByNickname(nickname);

        if (nicknameExists) {
            throw new SameNicknameException();
        }
    }

    /**
     * 사용자 계정 상태 확인 (Active)
     *
     * @param user 사용자
     */
    public void validateUserIsActive(User user) {
        if (user.getUserStatus() != UserStatus.ACTIVE) {
            throw new AccountNotActiveException();
        }
    }

    /**
     * 사용자 계정 상태에 따른 로그인 로직
     *
     * @param user 사용자
     */
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

    /**
     * 이메일과 닉네임이 이미 존재하는지 확인
     *
     * @param email    이메일
     * @param nickname 닉네임
     */
    public void checkEmailAndNicknameUniqueness(String email, String nickname) {
        // email 가입 가능 여부 확인
        validateEmailUniqueness(email);
        // 닉네임 중복 확인
        validateNicknameUniqueness(nickname);
    }
}
