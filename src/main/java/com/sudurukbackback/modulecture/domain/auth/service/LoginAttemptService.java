package com.sudurukbackback.modulecture.domain.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Service
public class LoginAttemptService {

    private static final String LOGIN_ATTEMPT_KEY_PREFIX = "login_attempts:";
    private static final int MAX_ATTEMPTS = 5;
    private static final long LOCK_DURATION_IN_SECONDS = 30;

    private final StringRedisTemplate redisTemplate;

    /**
     * 로그인 Lock 여부를 확인하여 로그인이 가능한 계정인지 확인하는 메서드
     *
     * @param email 이메일
     * @return boolean
     */
    public boolean isLoginAllowed(String email) {

        String attemptsStr = redisTemplate.opsForValue().get(LOGIN_ATTEMPT_KEY_PREFIX + email);

        // 로그인 실패 내역이 없는 경우 true
        if (attemptsStr == null) {
            return true;
        }

        try {
            int attempts = Integer.parseInt(attemptsStr);
            return attempts < MAX_ATTEMPTS;
        } catch (NumberFormatException e) {
            return true;
        }
    }

    /**
     * 사용자의 남은 로그인 잠금 시간을 초 단위로 반환
     *
     * @param email 이메일
     * @return 남은 시간(초 단위)
     */
    public long getRemainingLockoutTime(String email) {
        Long expire = redisTemplate.getExpire(LOGIN_ATTEMPT_KEY_PREFIX + email, TimeUnit.SECONDS);
        return expire == null || expire < 0 ? 0 : expire;
    }

    /**
     * 잠금까지 남은 로그인 횟수를 int형으로 반환
     *
     * @param email 이메일
     * @return 남은 횟수
     */
    public int getRemainingLoginAttempts(String email) {

        String attemptsStr = redisTemplate.opsForValue().get(LOGIN_ATTEMPT_KEY_PREFIX + email);
        if (attemptsStr == null) {
            return -1;
        }

        try {
            int attempts = Integer.parseInt(attemptsStr);
            return MAX_ATTEMPTS - attempts;
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    /**
     * 로그인 시도 횟수 증가
     *
     * @param email 이메일
     */
    public void incrementLoginAttempts(String email) {
        redisTemplate.opsForValue().increment(LOGIN_ATTEMPT_KEY_PREFIX + email);
        redisTemplate.expire(LOGIN_ATTEMPT_KEY_PREFIX + email, LOCK_DURATION_IN_SECONDS, TimeUnit.SECONDS);
    }

    /**
     * 로그인 시도 횟수 초기화
     *
     * @param email 이메일
     */
    public void resetLoginAttempts(String email) {
        redisTemplate.delete(LOGIN_ATTEMPT_KEY_PREFIX + email);
    }

    /**
     * 로그인 가능 여부 확인 및 횟수 증가
     *
     * @param email 이메일
     * @return boolean
     */
    public boolean checkAndIncrementLoginAttempts(String email) {
        if (!isLoginAllowed(email)) {
            return false;
        }

        incrementLoginAttempts(email);
        return true;
    }

}
