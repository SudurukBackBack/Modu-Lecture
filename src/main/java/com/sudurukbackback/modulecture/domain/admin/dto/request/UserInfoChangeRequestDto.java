package com.sudurukbackback.modulecture.domain.admin.dto.request;

import com.sudurukbackback.modulecture.domain.user.entity.enums.UserGrade;
import com.sudurukbackback.modulecture.domain.user.entity.enums.UserStatus;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserInfoChangeRequestDto {

    // 사용자 이름
    @Size(max = 20, message = "Nickname must not exceed 20 characters")
    String newNickname;

    // 사용자 등급
    UserGrade newGrade;

    // 사용자 상태(예: 활성화, 정지, 탈퇴 등)
    UserStatus newStatus;

}
