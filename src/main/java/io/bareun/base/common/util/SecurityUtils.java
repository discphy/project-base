package io.bareun.base.common.util;

import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Spring Security 관련 작업을 처리하는 유틸리티 클래스입니다.
 */
@Component
public class SecurityUtils {

    /**
     * 현재 Authentication 객체를 가져옵니다.
     *
     * @return 현재 Authentication 객체
     * @throws AuthenticationCredentialsNotFoundException 인증 객체가 null인 경우
     */
    private static Authentication getCurrentAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw new AuthenticationCredentialsNotFoundException("Authentication object is null");
        }
        return authentication;
    }

    /**
     * 현재 인증된 사용자의 principal을 가져옵니다.
     *
     * @return principal 객체
     * @throws AuthenticationCredentialsNotFoundException 인증 객체 또는 principal이 null인 경우
     */
    public static Object getUserPrincipal() {
        Authentication authentication = getCurrentAuthentication();
        Object principal = authentication.getPrincipal();
        if (principal == null) {
            throw new AuthenticationCredentialsNotFoundException("Principal is null");
        }
        return principal;
    }

    /**
     * 현재 인증된 사용자의 UserDetails 객체를 가져옵니다.
     *
     * @return 현재 인증된 사용자의 UserDetails 객체
     * @throws UsernameNotFoundException 사용자 세부 정보를 찾을 수 없는 경우
     */
    public static UserDetails getUserDetails() {
        Object principal = getUserPrincipal();
        return principal instanceof UserDetails ? (UserDetails) principal : null;
    }

    /**
     * 현재 인증된 사용자의 사용자 이름을 가져옵니다.
     *
     * @return 현재 인증된 사용자의 사용자 이름
     * @throws AuthenticationCredentialsNotFoundException 인증 객체 또는 principal이 null인 경우
     */
    public static String getUsername() {
        UserDetails userDetails = getUserDetails();
        return Optional.of(userDetails).orElse(null).getUsername();
    }

    /**
     * 현재 인증된 사용자가 특정 역할을 가지고 있는지 확인합니다.
     *
     * @param role 확인할 역할 (예: "ROLE_ADMIN")
     * @return 현재 인증된 사용자가 지정된 역할을 가지고 있으면 true, 그렇지 않으면 false
     * @throws AuthenticationCredentialsNotFoundException 인증 객체 또는 principal이 null인 경우
     */
    public static boolean hasRole(String role) {
        Authentication authentication = getCurrentAuthentication();
        return authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals(role));
    }

    /**
     * 현재 사용자가 인증되었는지 확인합니다.
     *
     * @return 사용자가 인증되었으면 true, 그렇지 않으면 false
     */
    public static boolean isAuthenticated() {
        return getCurrentAuthentication().isAuthenticated();
    }

    /**
     * 현재 인증된 사용자가 지정된 역할 중 하나라도 가지고 있는지 확인합니다.
     *
     * @param roles 확인할 역할 목록
     * @return 현재 인증된 사용자가 지정된 역할 중 하나라도 가지고 있으면 true, 그렇지 않으면 false
     * @throws AuthenticationCredentialsNotFoundException 인증 객체 또는 principal이 null인 경우
     */
    public static boolean hasAnyRole(String... roles) {
        Authentication authentication = getCurrentAuthentication();
        return authentication.getAuthorities().stream()
                .anyMatch(authority -> {
                    for (String role : roles) {
                        if (authority.getAuthority().equals(role)) {
                            return true;
                        }
                    }
                    return false;
                });
    }
}
