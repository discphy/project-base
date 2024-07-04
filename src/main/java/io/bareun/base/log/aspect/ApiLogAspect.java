package io.bareun.base.log.aspect;

import io.bareun.base.common.util.ObjectMapperUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import static io.bareun.base.common.util.RequestUtils.*;

/**
 * ApiLoggingAspect는 REST 컨트롤러의 HTTP 요청을 로깅하기 위한 Aspect입니다.
 * <p>
 * 이 클래스는 @RestController 어노테이션이 붙은 클래스 내에서 메서드 호출 전에 HTTP 요청을 로깅합니다.
 * 로깅할 정보로는 IP 주소, HTTP 메서드, 요청 URL, 쿼리 스트링, 요청 바디가 포함됩니다.
 */
@Slf4j
@Aspect
@Component
public class ApiLogAspect {

    /**
     * {@link org.springframework.web.bind.annotation.RestController} 어노테이션이 붙은 클래스
     * 내의 모든 메서드를 포인트컷으로 설정합니다.
     */
    @Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
    public void restController() {
    }

    /**
     * 포인트컷에서 지정한 메서드 호출 전에 HTTP 요청을 로깅하는 메서드입니다.
     *
     * @param joinPoint 조인 포인트 객체로, 호출된 메서드와 그 파라미터 등을 추출하는 데 사용됩니다.
     */
    @Before("restController()")
    public void httpLogging(JoinPoint joinPoint) {
        String remoteAddr = getRemoteAddr();
        String method = getMethod();
        String requestURL = getRequestURL();
        String queryString = getQueryString();

        String requestBody = getRequestBody(joinPoint);

        log.info("HTTP Log IP : {} | Method : {} | URL : {} | Query : {} | Body {}",
                remoteAddr, method, requestURL, queryString, requestBody);
    }

    /**
     * 메서드에서 RequestBody 어노테이션이 붙은 파라미터의 값을 추출하여 문자열로 반환합니다.
     *
     * @param joinPoint 조인 포인트 객체로, 호출된 메서드와 그 파라미터 정보를 추출하는 데 사용됩니다.
     * @return 추출된 RequestBody의 문자열 표현
     */
    private String getRequestBody(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method methodObject = signature.getMethod();
        Annotation[][] parameterAnnotations = methodObject.getParameterAnnotations();
        Object[] args = joinPoint.getArgs();

        for (int i = 0; i < args.length; i++) {
            for (Annotation annotation : parameterAnnotations[i]) {
                if (annotation instanceof RequestBody) {
                    return ObjectMapperUtils.toString(args[i]);
                }
            }
        }

        return "";
    }

}
