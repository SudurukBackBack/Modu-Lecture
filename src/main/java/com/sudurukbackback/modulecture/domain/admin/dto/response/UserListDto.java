package com.sudurukbackback.modulecture.domain.admin.dto.response;

import com.sudurukbackback.modulecture.domain.user.entity.User;
import com.sudurukbackback.modulecture.domain.user.entity.enums.UserGrade;
import com.sudurukbackback.modulecture.domain.user.entity.enums.UserStatus;

public record UserListDto(
        long id,
        String email,
        String nickname,
        UserGrade grade,
        UserStatus status
) {
    public static UserListDto of(User user) {
        return new UserListDto(
                user.getId(),
                user.getEmail(),
                user.getNickname(),
                user.getGrade(),
                user.getUserStatus()
        );
    }
}
