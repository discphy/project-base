package io.bareun.base.exception.handler;

import io.bareun.base.common.dto.response.ApiResponse;
import io.bareun.base.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.egovframe.rte.fdl.cmmn.exception.EgovBizException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.HttpStatus.*;

/**
 * ApiExceptionHandler는 Spring Web MVC에서 발생하는 예외를 처리하는 클래스입니다.
 * RestControllerAdvice 애노테이션을 사용하여 모든 @RestController에서 발생하는 예외를 처리합니다.
 * 각 예외에 따라 적절한 HTTP 상태 코드와 메시지를 반환합니다.
 */
@Slf4j
@RestControllerAdvice(annotations = RestController.class)
public class ApiExceptionHandler {

    /**
     * BusinessException을 처리하는 메서드입니다.
     *
     * @param e 발생한 BusinessException 객체
     * @return ApiResponse 객체 (실패 응답)
     */
    @ExceptionHandler(BusinessException.class)
    public ApiResponse<?> buisnessException(BusinessException e) {
        log.error("ApiException buisnessException", e);
        return ApiResponse.fail(e.getErrorCode().getCode(), e.getMessage());
    }

    /**
     * AuthenticationException을 처리하는 메서드입니다.
     *
     * @param e 발생한 AuthenticationException 객체
     * @return ApiResponse 객체 (UNAUTHORIZED 상태 응답)
     */
    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(UNAUTHORIZED)
    public ApiResponse<?> authenticationException(AuthenticationException e) {
        log.error("ApiException authenticationException", e);
        return ApiResponse.fail(UNAUTHORIZED.value(), e.getMessage());
    }

    /**
     * AccessDeniedException을 처리하는 메서드입니다.
     *
     * @param e 발생한 AccessDeniedException 객체
     * @return ApiResponse 객체 (FORBIDDEN 상태 응답)
     */
    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(FORBIDDEN)
    public ApiResponse<?> accessDeniedException(AccessDeniedException e) {
        log.error("ApiException accessDeniedException", e);
        return ApiResponse.fail(FORBIDDEN.value(), e.getMessage());
    }

    /**
     * EgovBizException을 처리하는 메서드입니다.
     *
     * @param e 발생한 EgovBizException 객체
     * @return ApiResponse 객체 (INTERNAL_SERVER_ERROR 상태 응답)
     */
    @ExceptionHandler(EgovBizException.class)
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    public ApiResponse<?> egovBizException(EgovBizException e) {
        log.error("ApiException EgovBizException", e);
        return ApiResponse.fail(INTERNAL_SERVER_ERROR.value(), e.getMessage());
    }

    /**
     * 그 외 모든 예외를 처리하는 메서드입니다.
     *
     * @param e 발생한 Exception 객체
     * @return ApiResponse 객체 (INTERNAL_SERVER_ERROR 상태 응답)
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    public ApiResponse<?> exception(Exception e) {
        log.error("ApiException Exception", e);
        return ApiResponse.fail(INTERNAL_SERVER_ERROR.value(), e.getMessage());
    }
}
