package com.ms.hoopi.common.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

@Component
public class CookieUtil {

    private static final String ACCESS_TOKEN_COOKIE_NAME = "access_token";
    private static final String REFRESH_TOKEN_COOKIE_NAME = "refresh_token";

    // 쿠키 생성
    public void create(HttpServletResponse response, String name, String value, int expiry, boolean httpOnly, boolean secure) {
        ResponseCookie cookie = ResponseCookie.from(name, value)
                .path("/")
                .httpOnly(httpOnly)
                .secure(secure)
                .maxAge(expiry)
                .sameSite("Strict")
                .build();
        response.addHeader("Set-Cookie", cookie.toString());
    }

    // acs token 쿠키 생성
    public void createAccessTokenCookie(HttpServletResponse response, String accessToken, boolean secure) {
        deleteAccessTokenCookie(response, true);
        create(response, ACCESS_TOKEN_COOKIE_NAME, accessToken, 15 * 60, true, true);
    }

    // 쿠키에서 acs token 추출
    public String getAccessTokenFromCookie(HttpServletRequest request) {
        return getCookieValue(request, ACCESS_TOKEN_COOKIE_NAME);
    }

    // 쿠키 값 추출
    private String getCookieValue(HttpServletRequest request, String name) {
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if (cookie.getName().equals(name)) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    // acs token 쿠키 삭제
    public void deleteAccessTokenCookie(HttpServletResponse response, boolean secure) {
        create(response, ACCESS_TOKEN_COOKIE_NAME, "", 0, true, true);
    }

}
