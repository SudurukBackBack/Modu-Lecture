package com.sudurukbackback.modulecture.domain.auth.service;

import com.sudurukbackback.modulecture.domain.user.entity.CustomOAuth2User;
import com.sudurukbackback.modulecture.domain.user.entity.User;
import com.sudurukbackback.modulecture.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class OAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);
        String oauthClientName = userRequest.getClientRegistration().getClientName();

        User newUser;
        String uuid = null;
        String email;
        String nickname;

        // oauth 서비스 제공자가 naver인 경우
        if (oauthClientName.equals("naver")) {
            // 소셜 로그인 후 response 저장
            Map<String, String> responseMap = (Map<String, String>) oAuth2User.getAttributes().get("response");

            // response에서 받아온 데이터 분리
            uuid = "naver_" + responseMap.get("id").substring(0, 14);
            email = responseMap.get("email");
            nickname = responseMap.get("nickname");

            // 이메일로 기존 사용자 확인
            Optional<User> existingUser = userRepository.findByUuid(uuid);

            if (existingUser.isEmpty()) {
                // 사용자가 존재하지 않으면 새로운 사용자 생성 및 저장
                newUser = User.createSocialUser(uuid, email, nickname, "naver");
                userRepository.save(newUser);
            }
        }

        return new CustomOAuth2User(uuid);
    }
}
