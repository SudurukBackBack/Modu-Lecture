package com.sudurukbackback.modulecture.domain.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserProfileUpdateRequestDto {

    // 사용자 이름
    @Size(max = 20, message = "Nickname must not exceed 20 characters")
    @NotBlank(message = "Nickname is required")
    private String nickname;

}
