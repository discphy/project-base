package io.bareun.base.web.config;

import io.bareun.base.web.support.QueryParamArgumentResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * {@code WebArgumentResolverConfig}는 스프링 MVC에서 커스텀
 * {@link HandlerMethodArgumentResolver}를 등록하는 설정 클래스입니다.
 * <p>
 * 이 클래스는 {@link QueryParamArgumentResolver}를 Spring MVC에 추가하여,
 * 메소드 파라미터에 쿼리 파라미터를 바인딩할 수 있도록 지원합니다.
 * </p>
 */
@Configuration
@RequiredArgsConstructor
public class WebArgumentResolveerConfig implements WebMvcConfigurer {

    private final QueryParamArgumentResolver queryParamArgumentResolver;

    /**
     * {@inheritDoc}
     *
     * <p>여기서는 {@link QueryParamArgumentResolver}를 Argument Resolver 목록에 추가하여,
     * HTTP 요청에서 쿼리 파라미터를 처리하는 메커니즘을 활성화합니다.</p>
     *
     * @param resolvers 스프링이 사용하는 {@link HandlerMethodArgumentResolver} 목록
     */
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(queryParamArgumentResolver);
    }
}
