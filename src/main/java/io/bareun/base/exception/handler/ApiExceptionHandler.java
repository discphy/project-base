package io.bareun.base.exception.handler;

import io.bareun.base.common.dto.response.ApiResponse;
import io.bareun.base.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.HttpStatus.*;

@Slf4j
@RestControllerAdvice(annotations = RestController.class)
public class ApiExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ApiResponse<?> buisnessException(BusinessException e) {
        log.error("ApiException buisnessException", e);
        return ApiResponse.fail(e.getErrorCode().getCode(), e.getMessage());
    }

    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(UNAUTHORIZED)
    public ApiResponse<?> authenticationException(AuthenticationException e) {
        log.error("ApiException authenticationException", e);
        return ApiResponse.fail(UNAUTHORIZED.value(), e.getMessage());
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(FORBIDDEN)
    public ApiResponse<?> accessDeniedException(AccessDeniedException e) {
        log.error("ApiException accessDeniedException", e);
        return ApiResponse.fail(FORBIDDEN.value(), e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    public ApiResponse<?> exception(Exception e) {
        log.error("ApiException Exception", e);
        return ApiResponse.fail(INTERNAL_SERVER_ERROR.value(), e.getMessage());
    }
}
