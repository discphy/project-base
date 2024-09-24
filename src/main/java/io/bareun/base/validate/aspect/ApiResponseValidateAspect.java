package io.bareun.base.validate.aspect;

import io.bareun.base.common.dto.response.ApiResponse;
import io.bareun.base.exception.InvalidApiResponseException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
@ConditionalOnProperty(name = "base.aspect.api-response-validate.enabled", havingValue = "true", matchIfMissing = true)
public class ApiResponseValidateAspect {

    @Around("within(@org.springframework.web.bind.annotation.RestController *)")
    public Object validateApiResponse(ProceedingJoinPoint joinPoint) throws Throwable {
        Object result = joinPoint.proceed();

        if (result == null) {
            return null;
        }

        // 리턴 값이 ApiResponse or ResponseEntity<?>(파일 다운로드)
        if (!(result instanceof ApiResponse) && !(result instanceof ResponseEntity<?>)) {
            throw new InvalidApiResponseException("ApiResponse 반환 값이 아닙니다. " +
                    "Method() : " + joinPoint.getSignature().getName());
        }

        return result;
    }
}
