package com.sudurukbackback.modulecture.domain.auth.service;

import com.sudurukbackback.modulecture.domain.user.entity.CustomOAuth2User;
import com.sudurukbackback.modulecture.domain.user.entity.User;
import com.sudurukbackback.modulecture.domain.user.entity.enums.ProfileField;
import com.sudurukbackback.modulecture.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class OAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);
        String oauthClientName = userRequest.getClientRegistration().getClientName(); // "google" 또는 "naver"

        String uuid;
        String email;
        String nickname;
        String provider = oauthClientName;

        // OAuth 서비스 제공자 별 정보 추출
        if (provider.equals("naver")) {
            // 소셜 로그인 후 response 저장
            Map<String, String> responseMap = (Map<String, String>) oAuth2User.getAttributes().get("response");

            // response에서 받아온 데이터 분리
            uuid = "naver-" + responseMap.get("id").substring(0, 14);
            email = responseMap.get("email");
            nickname = responseMap.get("nickname");

        } else if (provider.equals("Google")) {
            // 구글은 attributes Map에 바로 정보가 있음
            String googleId = (String) oAuth2User.getAttributes().get("sub");
            email = (String) oAuth2User.getAttributes().get("email");
            nickname = (String) oAuth2User.getAttributes().get("nickname");

            uuid = "google-" + googleId;

        } else {
            throw new OAuth2AuthenticationException("지원하지 않는 OAuth2 제공자 입니다: " + oauthClientName);
        }

        // 사용자 정보 저장 또는 정보 업데이트
        Optional<User> existingUserByUuid = userRepository.findByUuid(uuid);
        User user;

        if (existingUserByUuid.isEmpty()) {
            user = User.createSocialUser(uuid, email, nickname, provider);
            userRepository.save(user);

        } else {
            user = existingUserByUuid.get();
            user.updateProfile(ProfileField.NICKNAME, nickname);
        }

        return new CustomOAuth2User(uuid);
    }
}
