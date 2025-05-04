package com.sudurukbackback.modulecture.domain.auth.service;

import com.sudurukbackback.modulecture.domain.auth.component.AuthComponent;
import com.sudurukbackback.modulecture.domain.auth.dto.request.UserLoginRequestDto;
import com.sudurukbackback.modulecture.domain.auth.dto.request.UserRegistrationRequestDto;
import com.sudurukbackback.modulecture.domain.auth.dto.response.CookieResultDto;
import com.sudurukbackback.modulecture.domain.auth.exception.WrongAuthenticationException;
import com.sudurukbackback.modulecture.domain.user.component.UserComponent;
import com.sudurukbackback.modulecture.domain.user.entity.User;
import com.sudurukbackback.modulecture.domain.user.entity.enums.UserGrade;
import com.sudurukbackback.modulecture.domain.user.entity.enums.UserStatus;
import com.sudurukbackback.modulecture.domain.user.repository.UserRepository;
import com.sudurukbackback.modulecture.global.exception.BasicServerException;
import com.sudurukbackback.modulecture.global.security.JwtTokenProvider;
import com.sudurukbackback.modulecture.global.security.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Slf4j
@RequiredArgsConstructor
@Service
public class AuthService implements UserDetailsService {

    private final UserRepository userRepository;
    private final AuthComponent authComponent;
    private final UserComponent userComponent;
    private final LoginAttemptService loginAttemptService;
    private final JwtTokenProvider jwtTokenProvider;
    private final StringRedisTemplate redisTemplate;
    private final CookieService cookieService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(email));
    }

    public Authentication getAuthentication(String token) {

        String username = JwtUtil.getUsername(token);
        List<GrantedAuthority> authorities = JwtUtil.getAuthorities(token);
        UserDetails userDetails = loadUserByUsername(username);

        return new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
    }

    @Transactional
    public User signUp(UserRegistrationRequestDto request) {

        String email = emailNormalizer(request.getEmail());
        String password = request.getPassword();

        // 닉네임 미설정 시 임의의 닉네임 부여
        String nickname = generateRandomNickname(request.getNickname());

        // 이메일, 닉네임 중복 체크
        userComponent.checkEmailAndNicknameUniqueness(email, nickname);

        return userRepository.save(User.builder()
                .email(email)
                .password(authComponent.encodePassword(password))
                .nickname(nickname)
                .grade(UserGrade.ROLE_BRONZE)
                .userStatus(UserStatus.ACTIVE)
                .createdAt(LocalDateTime.now())
                .build());
    }

    public CookieResultDto signIn(UserLoginRequestDto request) {

        String email = emailNormalizer(request.getEmail());

        // 로그인 시도 횟수 확인 및 잠금 처리
        if (!loginAttemptService.checkAndIncrementLoginAttempts(email)) {
            long remainingTime = loginAttemptService.getRemainingLockoutTime(email);
            throw new WrongAuthenticationException(remainingTime);
        }

        // 이메일 및 비밀번호 검증, User 객체 반환
        User user = authComponent.verifyEmailAndPasswordMatch(email, request.getPassword());

        // 사용자 계정 상태 검증
        userComponent.validateUserStatus(user);

        // 로그인 성공 시 시도 횟수 초기화
        loginAttemptService.resetLoginAttempts(email);

        // 토큰 생성 후 쿠키 생성
        CookieResultDto cookies = generateTokensAndCreateCookiesByEmail(email);

        // refreshToken을 Redis에 저장
        storeRefreshTokenInRedis(email, cookies.getRefreshCookie().getValue());

        return cookies;
    }

    public CookieResultDto logout(HttpServletRequest request) {

        String token = JwtUtil.resolveToken(request, "access");
        log.info("로그아웃 요청 들어옴 - token: {}", token);

        if (token == null) {
            throw new BasicServerException();
        }

        logoutTokenInRedis(token);

        var cookies = cookieService.createCookies("", "");

        return CookieResultDto.builder()
                .accessCookie(cookies.get("access_cookie"))
                .refreshCookie(cookies.get("refresh_cookie"))
                .build();
    }

    public CookieResultDto refreshToken(HttpServletRequest request) {

        String refreshToken = JwtUtil.resolveToken(request, "refresh");
        log.info("토큰 갱신 요청 처리 시작");

        if (!JwtUtil.validateToken(refreshToken)) {
            log.error("토큰 검증 실패");
            throw new BasicServerException();
        }

        String email = JwtUtil.getUsername(refreshToken);
        String token = redisTemplate.opsForValue().get("RT:" + email);

        if (!refreshToken.equals(token)) {
            log.error("토큰 값 불일치로 인한 토큰 갱신 작업 중단");
            throw new BasicServerException();
        }

        // 토큰 생성 후 쿠키 생성
        CookieResultDto cookies = generateTokensAndCreateCookiesByEmail(email);

        // refreshToken을 Redis에 저장
        storeRefreshTokenInRedis(email, cookies.getRefreshCookie().getValue());

        return cookies;
    }

    private String emailNormalizer(String email) {
        return email.toLowerCase();
    }

    /**
     * 입력된 nickname이 없을 경우 랜덤 생성
     *
     * @param nickname 입력한 nickname
     * @return 랜덤 생성된 nickname (입력한 nickname이 null일 경우)
     */
    private String generateRandomNickname(String nickname) {
        return Optional.ofNullable(nickname)
                .filter(n -> !n.isEmpty())
                .orElse("User" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));
    }

    /**
     * 주어진 이메일에 대한 토큰(접근 토큰 및 리프레시 토큰)을 생성하고, 해당 토큰에 대한 쿠키를 생성합니다.
     * 생성된 토큰은 쿠키에 담겨 {@link CookieResultDto} 형태로 반환됩니다.
     *
     * @param email 토큰 생성을 위해 사용자를 식별하는 이메일 주소
     * @return 접근 및 리프레시 쿠키를 담고 있는 {@link CookieResultDto}
     */
    private CookieResultDto generateTokensAndCreateCookiesByEmail(String email) {
        // 이메일을 통해 사용자 식별 후 토큰 생성
        var tokens = jwtTokenProvider.generateToken(email);
        String accessToken = tokens.get("access_token");
        String refreshToken = tokens.get("refresh_token");

        // 생성한 토큰을 사용해 쿠키 생성
        var cookies = cookieService.createCookies(accessToken, refreshToken);

        return CookieResultDto.builder()
                .accessCookie(cookies.get("access_cookie"))
                .refreshCookie(cookies.get("refresh_cookie"))
                .build();
    }

    /**
     * 사용자의 토큰을 Redis에 "블랙리스트" 토큰으로 저장하여 로그아웃 처리하고,
     * 관련된 리프레시 토큰이 있다면 Redis에서 삭제합니다.
     *
     * 이 메서드는 토큰을 "BL:" 접두사가 붙은 키로 Redis에 설정하고, "logout"으로 표시합니다.
     * 토큰은 원래의 만료 시간과 함께 저장됩니다. 또한, 관련된 리프레시 토큰이 있다면,
     * 토큰에서 추출한 사용자 이름과 "RT:" 접두사를 사용하여 Redis에서 삭제합니다.
     *
     * @param token 로그아웃 처리되어 Redis에 블랙리스트로 등록될 JWT 접근 토큰
     */
    public void logoutTokenInRedis(String token) {

        long expiration = JwtUtil.getExpiration(token);

        redisTemplate.opsForValue().set(
                "BL:" + token,
                "logout",
                expiration,
                TimeUnit.MILLISECONDS);

        redisTemplate.delete("RT:" + JwtUtil.getUsername(token));
    }

    /**
     * 리프레시 토큰을 Redis에 2일의 만료 시간으로 저장합니다. 토큰은
     * "RT:{이메일}" 형식의 키로 저장되며, 여기서 {이메일}은 제공된 이메일 주소입니다.
     *
     * @param email Redis 키의 일부로 사용될 사용자의 이메일 주소
     * @param token Redis에 저장될 리프레시 토큰
     */
    public void storeRefreshTokenInRedis(String email, String token) {
        redisTemplate.opsForValue().set(
                "RT:" + email,
                token,
                2,
                TimeUnit.DAYS);
    }
}
