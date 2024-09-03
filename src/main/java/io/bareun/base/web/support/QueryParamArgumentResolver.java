package io.bareun.base.web.support;

import io.bareun.base.common.dto.map.BaseMap;
import io.bareun.base.web.annotation.QueryParam;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.Map;

/**
 * {@code QueryParamArgumentResolver}는 HTTP 요청의 쿼리 파라미터를 처리하기 위한
 * {@link HandlerMethodArgumentResolver}의 구현체입니다.
 * <p>
 * 해당 리졸버는 메소드 파라미터에 {@link QueryParam} 어노테이션이 존재하는지 확인하고,
 * 요청 쿼리 파라미터를 {@link BaseMap} 객체로 변환하여 컨트롤러 메소드에 전달합니다.
 * </p>
 */
@Component
public class QueryParamArgumentResolver implements HandlerMethodArgumentResolver {

    /**
     * 주어진 파라미터가 {@link QueryParam} 어노테이션을 가지고 있는지 확인합니다.
     *
     * @param parameter 확인할 메소드 파라미터
     * @return {@code true} if the parameter has {@link QueryParam} annotation, {@code false} otherwise
     */
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(QueryParam.class);
    }

    /**
     * 요청의 쿼리 파라미터를 가져와 {@link BaseMap} 객체로 변환합니다.
     *
     * <p>
     * 요청 쿼리 파라미터는 키-값 쌍의 맵으로 전달되며, 각 키에 해당하는 첫 번째 값을 {@link BaseMap}에 저장합니다.
     * </p>
     *
     * @param parameter    메소드 파라미터
     * @param mavContainer 현재 요청의 {@link ModelAndViewContainer}
     * @param webRequest   {@link NativeWebRequest}로부터 전달받은 요청 객체
     * @param binderFactory 바인더 객체 팩토리
     * @return 요청 쿼리 파라미터가 담긴 {@link BaseMap} 객체
     */
    @Override
    public Object resolveArgument(MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) {
        Map<String, String[]> parameterMap = webRequest.getParameterMap();
        BaseMap baseMap = new BaseMap();

        if (!parameterMap.isEmpty()) {
            for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
                if (entry.getValue().length > 0) {
                    baseMap.put(entry.getKey(), entry.getValue()[0]);
                }
            }
        }

        return baseMap;
    }
}
