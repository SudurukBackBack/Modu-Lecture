package com.sudurukbackback.modulecture.domain.admin.dto.request;

import com.sudurukbackback.modulecture.domain.auth.dto.request.BaseAuthRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class AdminRegisterDto extends BaseAuthRequest {

    // 사용자 이름
    @Size(max = 20, message = "Nickname must not exceed 20 characters")
    private String nickname;

    // 관리자 가입을 위한 비공개 코드(관계자만 알 수 있는 코드)
    @NotBlank(message = "Admin Code is required.")
    private String adminCode;
}
