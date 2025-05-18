package com.sudurukbackback.modulecture.domain.admin.service;

import com.sudurukbackback.modulecture.domain.admin.dto.request.AdminRegisterRequestDto;
import com.sudurukbackback.modulecture.domain.admin.dto.request.UserInfoChangeRequestDto;
import com.sudurukbackback.modulecture.domain.admin.dto.response.UserListDto;
import com.sudurukbackback.modulecture.domain.admin.exception.AdminRegisterException;
import com.sudurukbackback.modulecture.domain.admin.exception.NoUpdateRequestException;
import com.sudurukbackback.modulecture.domain.auth.component.AuthComponent;
import com.sudurukbackback.modulecture.domain.user.component.UserComponent;
import com.sudurukbackback.modulecture.domain.user.entity.User;
import com.sudurukbackback.modulecture.domain.user.entity.enums.ProfileField;
import com.sudurukbackback.modulecture.domain.user.entity.enums.UserGrade;
import com.sudurukbackback.modulecture.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;
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
    public void registerAdmin(AdminRegisterRequestDto request) {

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

        // 사용한 Code 폐기
        redisTemplate.delete(ADMIN_CODE_PREFIX + code);
    }

    /**
     * 제공된 검색 키워드를 기반으로 사용자의 페이징된 목록을 검색합니다.
     * 키워드가 제공되면 닉네임 또는 이메일로 대소문자를 구분하지 않고 사용자를 검색합니다.
     * 그렇지 않으면 모든 사용자를 검색합니다.
     *
     * @param keyword  닉네임 또는 이메일로 사용자를 필터링하는 데 사용되는 검색 키워드입니다. null 또는 비어 있을 수 있습니다.
     * @param pageable 페이징 및 정렬 정보
     * @return 페이징된 사용자 목록 (UserListDto로 매핑됨)
     */
    public Page<UserListDto> getUserList(String keyword, Pageable pageable) {
        // 사용자 엔티티를 담는 Page 객체
        Page<User> userPage;

        // 검색 키워드가 null이 아니고 비어 있지 않은 경우 (검색 조건이 있는 경우)
        if (keyword != null && !keyword.isEmpty()) {
            // 닉네임 또는 이메일에 검색 키워드를 포함하는 경우
            // Pageable 정보에 따라 페이징하여 조회
            // ignoreCase 옵션을 사용하여 대소문자 구분 없이 검색
            userPage = userRepository.findByNicknameContainingIgnoreCaseOrEmailContainingIgnoreCase(keyword, keyword, pageable);
        } else {
            // 검색 키워드가 없는 경우 (검색 조건이 없는 경우) -> 모두 조회
            userPage = userRepository.findAll(pageable);
        }

        // 조회된 Page<User> 객체의 각 User 엔티티를 UserListDto::of 메서드를 사용하여 UserListDto 객체로 변환
        return userPage.map(UserListDto::of);
    }

    /**
     * Redis 캐시 내에서 제공된 관리자 등록 코드의 존재 여부와 상태 값을 확인하여 유효성을 검증합니다.
     * 코드가 유효하지 않거나 필수 조건을 충족하지 못하면 예외를 발생시킵니다.
     *
     * @param code 검증할 관리자 등록 코드
     * @throws AdminRegisterException 코드가 유효하지 않거나 캐시에서 찾을 수 없는 경우
     */
    private void checkAdminCode(String code) {
        String storedCode = redisTemplate.opsForValue().get(ADMIN_CODE_PREFIX + code);

        if (storedCode == null || !storedCode.equals("TEMP")) {
            throw new AdminRegisterException();
        }
    }

    /**
     * 주어진 요청에 따라 사용자 정보를 업데이트합니다. 이 메서드를 통해 사용자의 닉네임, 등급 또는 상태를 업데이트할 수 있습니다.
     * 업데이트할 필드가 요청에 제공되지 않으면 예외가 발생합니다.
     *
     * @param userId  정보를 업데이트할 사용자의 ID입니다.
     * @param request 사용자의 프로필 필드 (예: 닉네임, 등급, 상태)에 대한 새로운 값을 담고 있는 UserInfoChangeRequestDto의 인스턴스입니다.
     * @throws NoUpdateRequestException 요청에 업데이트할 필드가 없는 경우 발생합니다.
     */
    @Transactional
    public void changeUserInfo(Long userId, UserInfoChangeRequestDto request) {

        User user = userComponent.getUserById(userId);

        // 변경 사항이 없을 경우
        boolean noChanges = Objects.equals(request.getNewNickname(), user.getNickname()) &&
                            Objects.equals(request.getNewGrade(), user.getGrade()) &&
                            Objects.equals(request.getNewStatus(), user.getUserStatus());

        if (noChanges) {
            throw new NoUpdateRequestException();
        }

        Optional.ofNullable(request.getNewNickname())
                .ifPresent(newNickname -> user.updateProfile(ProfileField.NICKNAME, newNickname));

        Optional.ofNullable(request.getNewGrade())
                .ifPresent(newGrade -> user.updateProfile(ProfileField.GRADE, newGrade));

        Optional.ofNullable(request.getNewStatus())
                .ifPresent(newStatus -> user.updateProfile(ProfileField.STATUS, newStatus));
    }
}