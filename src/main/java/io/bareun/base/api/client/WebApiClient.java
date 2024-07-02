package io.bareun.base.api.client;

import io.bareun.base.api.request.ApiRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.RequestBodySpec;
import org.springframework.web.reactive.function.client.WebClient.RequestHeadersSpec;
import org.springframework.web.util.DefaultUriBuilderFactory;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

/**
 * {@link WebClient}를 사용하여 웹 API 호출을 수행하는 클라이언트입니다.
 */
@Slf4j
@Component
public class WebApiClient {

    private final WebClient webClient;

    private static final int MAX_MEMORY = 5 * 1024 * 1024;

    /**
     * 지정된 {@link WebClient.Builder}를 사용하여 새로운 {@code WebApiClient}를 생성합니다.
     *
     * @param builder WebClient 인스턴스를 생성하기 위해 사용할 WebClient.Builder
     */
    public WebApiClient(WebClient.Builder builder) {
        DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory();
        factory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.VALUES_ONLY);

        this.webClient = builder
                .clientConnector(new ReactorClientHttpConnector(HttpClient.create().compress(true)))
                .uriBuilderFactory(factory)
                .exchangeStrategies(ExchangeStrategies.builder()
                        .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(MAX_MEMORY))
                        .build())
                .build();
    }

    /**
     * 주어진 API 요청을 동기적으로 호출하고 응답을 반환합니다.
     *
     * @param <T> 응답 본문의 타입
     * @param request 호출할 API 요청
     * @return API 요청의 응답 본문
     */
    public <T> T callReturn(ApiRequest<T> request) {
        return retrieve(request).block();
    }

    /**
     * 주어진 API 요청을 비동기적으로 호출합니다.
     *
     * @param <T> 응답 본문의 타입
     * @param request 호출할 API 요청
     */
    public <T> void call(ApiRequest<T> request) {
        retrieve(request).subscribe(request::subscribe, request::error);
    }

    /**
     * 주어진 API 요청에 대한 응답을 {@link Mono}로 반환합니다.
     *
     * @param <T> 응답 본문의 타입
     * @param request 응답을 가져올 API 요청
     * @return API 요청의 응답 본문을 내보내는 Mono
     */
    private <T> Mono<T> retrieve(ApiRequest<T> request) {
        return requestSpec(request).retrieve().bodyToMono(request.getResponseType());
    }

    /**
     * 주어진 API 요청에 대한 {@link RequestHeadersSpec}를 생성합니다.
     *
     * @param request 스펙을 생성할 API 요청
     * @return 주어진 API 요청에 대한 RequestHeadersSpec
     */
    private RequestHeadersSpec<?> requestSpec(ApiRequest<?> request) {
        RequestBodySpec spec = webClient.method(request.getMethod())
                .uri(request.getUrl())
                .headers(h -> h.addAll(request.getHeaders()));

        return request.getBody() != null ? spec.bodyValue(request.getBody()) : spec;
    }
}
