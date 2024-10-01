package io.bareun.base.common.util;

import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * HttpServletRequest 및 HttpSession과 관련된 일반 작업을 처리하는 유틸리티 클래스입니다.
 */
@Component
public class RequestUtils {

    /**
     * 현재 HttpServletRequest를 반환합니다.
     *
     * @return 현재 HttpServletRequest
     * @throws IllegalStateException 요청 속성이 발견되지 않으면 발생
     */
    public static HttpServletRequest getCurrentHttpRequest() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            throw new IllegalStateException("No request attributes found");
        }
        return attributes.getRequest();
    }

    /**
     * 현재 HttpSession을 반환합니다. 필요 시 세션을 생성합니다.
     *
     * @return 현재 HttpSession
     */
    public static HttpSession getCurrentSession() {
        HttpServletRequest request = getCurrentHttpRequest();
        return request.getSession();
    }

    /**
     * 현재 HttpSession을 반환합니다.
     *
     * @param create 존재하지 않는 경우 새로운 세션을 생성할지 여부
     * @return 현재 HttpSession 또는 세션이 존재하지 않고 create가 false인 경우 null
     */
    public static HttpSession getCurrentSession(boolean create) {
        HttpServletRequest request = getCurrentHttpRequest();
        return request.getSession(create);
    }

    /**
     * 현재 세션에 속성을 설정합니다.
     *
     * @param name  속성의 이름
     * @param value 속성의 값
     */
    public static void setSessionAttribute(String name, Object value) {
        HttpSession session = getCurrentSession(true);
        session.setAttribute(name, value);
    }

    /**
     * 현재 세션에서 속성을 가져옵니다.
     *
     * @param name 속성의 이름
     * @return 속성의 값, 속성을 찾을 수 없거나 세션이 존재하지 않는 경우 null
     */
    public static Object getSessionAttribute(String name) {
        HttpSession session = getCurrentSession();
        return (session != null) ? session.getAttribute(name) : null;
    }

    /**
     * 현재 세션이 존재하면 무효화합니다.
     */
    public static void invalidateSession() {
        HttpSession session = getCurrentSession();
        if (session != null) {
            session.invalidate();
        }
    }

    /**
     * 현재 요청에서 속성을 가져옵니다.
     *
     * @param name 속성의 이름
     * @return 속성의 값, 속성을 찾을 수 없는 경우 null
     */
    public static Object getRequestAttribute(String name) {
        HttpServletRequest request = getCurrentHttpRequest();
        return request.getAttribute(name);
    }

    /**
     * 현재 요청에 속성을 설정합니다.
     *
     * @param name  속성의 이름
     * @param value 속성의 값
     */
    public static void setRequestAttribute(String name, Object value) {
        HttpServletRequest request = getCurrentHttpRequest();
        request.setAttribute(name, value);
    }

    /**
     * 현재 요청의 전체 URL을 반환합니다.
     *
     * @return 전체 URL
     */
    public static String getRequestURL() {
        HttpServletRequest request = getCurrentHttpRequest();
        return request.getRequestURL().toString();
    }

    /**
     * 현재 요청의 URI를 반환합니다.
     *
     * @return URI
     */
    public static String getRequestURI() {
        HttpServletRequest request = getCurrentHttpRequest();
        return request.getRequestURI();
    }

    /**
     * 현재 요청의 쿼리 문자열을 반환합니다.
     *
     * @return 쿼리 문자열
     */
    public static String getQueryString() {
        HttpServletRequest request = getCurrentHttpRequest();
        return request.getQueryString();
    }

    /**
     * 현재 요청의 스킴(예: http, https)을 반환합니다.
     *
     * @return 스킴
     */
    public static String getScheme() {
        HttpServletRequest request = getCurrentHttpRequest();
        return request.getScheme();
    }

    public static String getMethod() {
        HttpServletRequest request = getCurrentHttpRequest();
        return request.getMethod();
    }

    /**
     * 현재 요청의 서버 이름을 반환합니다.
     *
     * @return 서버 이름
     */
    public static String getServerName() {
        HttpServletRequest request = getCurrentHttpRequest();
        return request.getServerName();
    }

    /**
     * 현재 요청의 서버 포트를 반환합니다.
     *
     * @return 서버 포트
     */
    public static int getServerPort() {
        HttpServletRequest request = getCurrentHttpRequest();
        return request.getServerPort();
    }

    /**
     * 현재 요청을 수행한 클라이언트의 원격 IP 주소를 반환합니다.
     *
     * @return 원격 IP 주소
     */
    public static String getRemoteAddr() {
        HttpServletRequest request = getCurrentHttpRequest();
        return request.getHeader("X-Forwarded-For");
    }

    /**
     * 현재 요청의 호스트 헤더 값을 반환합니다.
     *
     * @return 호스트 헤더 값
     */
    public static String getHost() {
        HttpServletRequest request = getCurrentHttpRequest();
        return request.getHeader("Host");
    }

    /**
     * 현재 요청의 Context Path 값을 반환합니다.
     * @return Context Path 값
     */
    public static String getContextPath() {
        HttpServletRequest request = getCurrentHttpRequest();
        return request.getContextPath();
    }

    /**
     * 현재 요청의 호스트 헤더에서 서브도메인을 반환합니다.
     *
     * @return 서브도메인, 존재하지 않는 경우 null
     */
    public static String getSubDomain() {
        String host = getHost();
        if (host == null || host.isEmpty()) {
            return null;
        }
        String[] parts = host.split("\\.");
        if (parts.length > 2) {
            return parts[0];
        }
        return null;
    }

    /**
     * 현재 요청의 모든 쿼리 매개변수를 Map 형태로 반환합니다.
     *
     * @return 쿼리 매개변수의 Map
     */
    public static Map<String, String> getQueryParameters() {
        HttpServletRequest request = getCurrentHttpRequest();
        Map<String, String> queryParameters = new HashMap<>();
        Enumeration<String> parameterNames = request.getParameterNames();
        while (parameterNames.hasMoreElements()) {
            String paramName = parameterNames.nextElement();
            queryParameters.put(paramName, request.getParameter(paramName));
        }
        return queryParameters;
    }
}
