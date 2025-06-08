package com.sudurukbackback.modulecture.domain.auth.handler;

import com.sudurukbackback.modulecture.domain.auth.dto.response.CookieResultDto;
import com.sudurukbackback.modulecture.domain.auth.service.AuthService;
import com.sudurukbackback.modulecture.domain.auth.service.CookieService;
import com.sudurukbackback.modulecture.domain.user.entity.CustomOAuth2User;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;

@Slf4j
@RequiredArgsConstructor
@Component
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final AuthService authService;
    private final CookieService cookieService;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException, ServletException {
        try {
            CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();

            String uuid = oAuth2User.getName();

            CookieResultDto cookies = authService.generateTokensAndCreateCookies(uuid);

            authService.storeRefreshTokenInRedis(uuid, cookies.getRefreshCookie().getValue());

            cookieService.setCookiesInHttpHeader(response, cookies.getAccessCookie(), cookies.getRefreshCookie());

            // 리다이렉트 대신 HTML과 자바스크립트를 사용하여 새로고침을 구현
            response.setContentType("text/html;charset=UTF-8");
            PrintWriter out = response.getWriter();
            out.println("<html>");
            out.println("<head>");
            out.println("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">");
            out.println("<script>");
            out.println("window.location.href = '/main';");
            out.println("window.onload = function() {");
            out.println("  setTimeout(function() {");
            out.println("    window.location.reload();");
            out.println("  }, 500);");
            out.println("};");
            out.println("</script>");
            out.println("</head>");
            out.println("<body>");
            out.println("<p>로그인 성공. 메인 페이지로 이동 중...</p>");
            out.println("</body>");
            out.println("</html>");
            out.flush();

        } catch (Exception e) {
            log.error("OAuth2 인증 후 토큰 생성 또는 쿠키 생성 및 저장 과정에서 오류 발생: {}", e.getMessage());
            response.sendRedirect("/error?message=authentication_error");
        }
    }

}
