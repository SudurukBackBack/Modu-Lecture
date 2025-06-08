package com.sudurukbackback.modulecture.domain.auth.service;

import com.sudurukbackback.modulecture.domain.auth.component.AuthComponent;
import com.sudurukbackback.modulecture.domain.auth.dto.request.UserLoginRequestDto;
import com.sudurukbackback.modulecture.domain.auth.dto.request.UserRegistrationRequestDto;
import com.sudurukbackback.modulecture.domain.auth.dto.response.CookieResultDto;
import com.sudurukbackback.modulecture.domain.user.component.UserComponent;
import com.sudurukbackback.modulecture.domain.user.entity.User;
import com.sudurukbackback.modulecture.domain.user.entity.enums.UserGrade;
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

import java.util.List;
import java.util.UUID;
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
    public UserDetails loadUserByUsername(String uuid) throws UsernameNotFoundException {
        // uuid로 사용자 찾기 시도
        return userRepository.findByUuid(uuid)
                .orElseThrow(() -> new UsernameNotFoundException(uuid));
    }

    public Authentication getAuthentication(String token) {

        String uuid = JwtUtil.getUsername(token);
        List<GrantedAuthority> authorities = JwtUtil.getAuthorities(token);
        UserDetails userDetails = loadUserByUsername(uuid);

        return new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
    }

    @Transactional
    public User signUp(UserRegistrationRequestDto request) {

        String email = authComponent.emailNormalizer(request.getEmail());
        String password = authComponent.encodePassword(request.getPassword());

        // 닉네임 미설정 시 이메일을 통해 닉네임 설정
        String nickname = userComponent.generateNicknameFromEmail(request.getNickname(), email);

        // 이메일, 닉네임 중복 체크
        userComponent.checkEmailAndNicknameUniqueness(email, nickname);

        String uuid = "modu_" + UUID.randomUUID();

        User newUserEntity = User.createUser(uuid, email, password, nickname, UserGrade.ROLE_BRONZE);

        return userRepository.save(newUserEntity);
    }

    @Transactional
    public CookieResultDto signIn(UserLoginRequestDto request) {

        String email = authComponent.emailNormalizer(request.getEmail());

        // 이메일 및 비밀번호 검증 (내부에서 잠금 확인 및 시도 횟수 증가 처리)
        User user = authComponent.verifyEmailAndPasswordMatch(email, request.getPassword());
        String uuid = user.getUuid();

        // 사용자 계정 상태 검증
        userComponent.validateUserStatus(user);

        // 로그인 성공 시 시도 횟수 초기화
        loginAttemptService.resetLoginAttempts(uuid);

        // 토큰 생성 후 쿠키 생성
        CookieResultDto cookies = generateTokensAndCreateCookies(uuid);

        // refreshToken을 Redis에 저장
        storeRefreshTokenInRedis(uuid, cookies.getRefreshCookie().getValue());

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

        String uuid = JwtUtil.getUsername(refreshToken);
        String token = redisTemplate.opsForValue().get("RT:" + uuid);

        if (!refreshToken.equals(token)) {
            log.error("토큰 값 불일치로 인한 토큰 갱신 작업 중단");
            throw new BasicServerException();
        }

        // 토큰 생성 후 쿠키 생성
        CookieResultDto cookies = generateTokensAndCreateCookies(uuid);

        // refreshToken을 Redis에 저장
        storeRefreshTokenInRedis(uuid, cookies.getRefreshCookie().getValue());

        return cookies;
    }

    /**
     * 주어진 이메일에 대한 토큰(접근 토큰 및 리프레시 토큰)을 생성하고, 해당 토큰에 대한 쿠키를 생성합니다.
     * 생성된 토큰은 쿠키에 담겨 {@link CookieResultDto} 형태로 반환됩니다.
     *
     * @param uuid 토큰 생성을 위해 사용자를 식별하는 코드
     * @return 접근 및 리프레시 쿠키를 담고 있는 {@link CookieResultDto}
     */
    public CookieResultDto generateTokensAndCreateCookies(String uuid) {
        // 이메일을 통해 사용자 식별 후 토큰 생성
        var tokens = jwtTokenProvider.generateToken(uuid);
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
     * <p>
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
     * "RT:{uuid}" 형식의 키로 저장되며, 여기서 {uuid}은 제공된 uuid입니다.
     *
     * @param uuid Redis 키의 일부로 사용될 사용자 식별 코드
     * @param token Redis에 저장될 리프레시 토큰
     */
    public void storeRefreshTokenInRedis(String uuid, String token) {
        redisTemplate.opsForValue().set(
                "RT:" + uuid,
                token,
                2,
                TimeUnit.DAYS);
    }
}
