package com.sudurukBackBack.Modu_Lecture.domain.user.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class BaseUserRequest {

    // 이메일 형식
    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    private String email;

    // 비밀번호 형식
    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
            message = "Password must be at least 8 characters long, contain at least one uppercase letter, one lowercase letter, one number, and one special character"
    )
    @NotBlank(message = "Password is required")
    private String password;

}
