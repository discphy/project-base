package io.bareun.base.common.util;

import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

/**
 * HttpServletResponse와 관련된 일반 작업을 처리하는 유틸리티 클래스입니다.
 */
@Component
public class ResponseUtils {

    /**
     * 현재 HttpServletResponse를 반환합니다.
     *
     * @return 현재 HttpServletResponse
     * @throws IllegalStateException 요청 속성이 발견되지 않으면 발생
     */
    public static HttpServletResponse getCurrentHttpResponse() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            throw new IllegalStateException("No request attributes found");
        }
        return attributes.getResponse();
    }

    /**
     * 응답에 쿠키를 추가합니다.
     *
     * @param name   쿠키의 이름
     * @param value  쿠키의 값
     * @param maxAge 쿠키의 최대 수명 (초 단위)
     */
    public static void addCookie(String name, String value, int maxAge) {
        HttpServletResponse response = getCurrentHttpResponse();
        Cookie cookie = new Cookie(name, value);
        cookie.setMaxAge(maxAge);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    /**
     * 응답에서 쿠키를 제거합니다.
     *
     * @param name 제거할 쿠키의 이름
     */
    public static void removeCookie(String name) {
        HttpServletResponse response = getCurrentHttpResponse();
        Cookie cookie = new Cookie(name, null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
    }
}
