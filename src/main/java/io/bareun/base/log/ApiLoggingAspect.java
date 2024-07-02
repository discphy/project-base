package io.bareun.base.log;

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

@Slf4j
@Aspect
@Component
public class ApiLoggingAspect {

    @Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
    public void restController() {
    }

    @Before("restController()")
    public void httpLogging(JoinPoint joinPoint) {

        String remoteAddr = getRemoteAddr();
        String method = getMethod();
        String requestURL = getRequestURL();
        String queryString = getQueryString();

        String requestBody = getRequestBody(joinPoint);

        log.info("HTTP Logging IP : {} | Method : {} | URL : {} | Query : {} | Body {}",
                remoteAddr, method, requestURL, queryString, requestBody);
    }

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
