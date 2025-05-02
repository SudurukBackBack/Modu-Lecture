package com.sudurukbackback.modulecture.domain.auth.dto.response;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.ResponseCookie;

@Getter
@Builder
public class CookieResultDto {
    private ResponseCookie accessCookie;
    private ResponseCookie refreshCookie;
}