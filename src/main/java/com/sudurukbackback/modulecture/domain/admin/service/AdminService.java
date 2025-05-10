package com.sudurukbackback.modulecture.domain.admin.service;

import com.sudurukbackback.modulecture.domain.admin.dto.request.AdminRegisterDto;
import com.sudurukbackback.modulecture.domain.admin.exception.AdminRegisterException;
import com.sudurukbackback.modulecture.domain.auth.component.AuthComponent;
import com.sudurukbackback.modulecture.domain.user.component.UserComponent;
import com.sudurukbackback.modulecture.domain.user.entity.User;
import com.sudurukbackback.modulecture.domain.user.entity.enums.UserGrade;
import com.sudurukbackback.modulecture.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Service
public class AdminService {

    private static final String ADMIN_CODE_PREFIX = "ADMIN_CODE:";
    private static final Random RANDOM = new Random();

    private final UserRepository userRepository;
    private final AuthComponent authComponent;
    private final UserComponent userComponent;
    private final StringRedisTemplate redisTemplate;

    public String generateCode() {
        // 코드 생성
        int count = 6;
        boolean letters = true;
        boolean numbers = true;
        char[] chars = null; // null이면 모든 문자 사용

        String code = RandomStringUtils.random(count, 0, 0, letters, numbers, chars, RANDOM).toUpperCase();

        String key = ADMIN_CODE_PREFIX + code;

        // 일회성 사용을 위해 redis에 저장
        redisTemplate.opsForValue().set(key, "TEMP", 1, TimeUnit.HOURS);

        return code;
    }

    @Transactional
    public void registerAdmin(AdminRegisterDto request) {

        String code = request.getAdminCode();

        // 관리자 가입 코드 인증 절차 필요
        checkAdminCode(code);

        String email = authComponent.emailNormalizer(request.getEmail());
        String password = authComponent.encodePassword(request.getPassword());

        // 닉네임 미설정 시 이메일을 통해 닉네임 설정
        String nickname = userComponent.generateNicknameFromEmail(request.getNickname(), email);

        // 이메일, 닉네임 중복 체크
        userComponent.checkEmailAndNicknameUniqueness(email, nickname);

        // Admin 객체 생성
        User admin = User.createUserEntity(email, password, nickname, UserGrade.ROLE_ADMIN);

        userRepository.save(admin);

        redisTemplate.delete(ADMIN_CODE_PREFIX + code);
    }


    private void checkAdminCode(String code) {
        String storedCode = redisTemplate.opsForValue().get(ADMIN_CODE_PREFIX + code);

        if (storedCode == null || !storedCode.equals("TEMP")) {
            throw new AdminRegisterException();
        }
    }

}
