package com.sudurukBackBack.Modu_Lecture.domain.user.dto.request;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserRegistrationRequestDto extends BaseAuthRequest {

    // 사용자 이름
    @Size(max = 20, message = "User name must not exceed 20 characters")
    private String username;

}
