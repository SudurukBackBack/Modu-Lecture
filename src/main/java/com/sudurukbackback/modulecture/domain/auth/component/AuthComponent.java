package com.sudurukbackback.modulecture.domain.auth.component;

import com.sudurukbackback.modulecture.domain.auth.exception.WrongAuthenticationException;
import com.sudurukbackback.modulecture.domain.auth.service.LoginAttemptService;
import com.sudurukbackback.modulecture.domain.user.component.UserComponent;
import com.sudurukbackback.modulecture.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class AuthComponent {

    private final PasswordEncoder passwordEncoder;
    private final LoginAttemptService loginAttemptService;
    private final UserComponent userComponent;

    /**
     * 제공된 이메일 주소를 소문자로 변환하여 형식을 정규화합니다.
     *
     * @param email 정규화할 이메일 주소
     * @return 소문자로 변환된 정규화된 이메일 주소
     */
    public String emailNormalizer(String email) {
        return email.toLowerCase();
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
    public void validatePassword(String inputPassword, String encodedPassword, int remainAttempts) {
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
        User user = userComponent.getUserByEmail(email);
        int remainAttempts = loginAttemptService.getRemainingLoginAttempts(email);

        validatePassword(password, user.getPassword(), remainAttempts);

        return user;
    }
}
