package com.sudurukbackback.modulecture.domain.auth.component;

import com.sudurukbackback.modulecture.domain.user.entity.User;
import com.sudurukbackback.modulecture.domain.user.exception.EmailAlreadyExistsException;
import com.sudurukbackback.modulecture.domain.auth.exception.WrongAuthenticationException;
import com.sudurukbackback.modulecture.domain.user.repository.UserRepository;
import com.sudurukbackback.modulecture.domain.auth.service.LoginAttemptService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class AuthComponent {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final LoginAttemptService loginAttemptService;

    /**
     * 이메일 중복 체크
     *
     * @param email 이메일
     */
    public void validateEmailUniqueness(String email) {
        boolean emailExists = userRepository.existsByEmail(email);

        if (emailExists) {
            throw new EmailAlreadyExistsException();
        }
    }

    /**
     * 이메일로 UserEntity 가져오기
     *
     * @param email 이메일
     * @return UserEntity
     */
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(WrongAuthenticationException::new);
    }

    /**
     * 비밀번호 인코딩
     *
     * @param password 비밀번호
     * @return password(인코딩된 값)
     */
    public String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }

    /**
     * 비밀번호 일치 확인
     *
     * @param inputPassword 입력한 비밀번호
     * @param encodedPassword 실제 비밀번호 (인코딩 된 상태)
     */
    private void validatePassword(String inputPassword, String encodedPassword, int remainAttempts) {
        if (!passwordEncoder.matches(inputPassword, encodedPassword)) {
            if (remainAttempts == -1) {
                throw new WrongAuthenticationException();
            } else {
                throw new WrongAuthenticationException(remainAttempts);
            }
        }
    }

    /**
     * 이메일, 비밀번호 매칭 검증
     *
     * @param email 이메일
     * @param password 비밀번호
     * @return UserEntity
     */
    public User verifyEmailAndPasswordMatch(String email, String password) {
        User user = findUserByEmail(email);
        int remainAttempts = loginAttemptService.getRemainingLoginAttempts(email);

        validatePassword(password, user.getPassword(), remainAttempts);

        return user;
    }
}
