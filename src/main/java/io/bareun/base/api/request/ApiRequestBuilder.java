package io.bareun.base.api.request;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.util.MultiValueMap;

/**
 * API 요청을 빌드하는 빌더 클래스입니다.
 *
 * @param <T> 응답 타입
 */
public class ApiRequestBuilder<T> {

    private HttpMethod method;
    private String url;
    private Object body;
    private Class<T> responseType;
    private final HttpHeaders headers = new HttpHeaders();

    /**
     * 새로운 ApiRequestBuilder 인스턴스를 반환합니다.
     *
     * @param <T> 응답 타입
     * @return 새로운 ApiRequestBuilder 인스턴스
     */
    public static <T> ApiRequestBuilder<T> builder() {
        return new ApiRequestBuilder<>();
    }

    /**
     * HTTP 메서드를 설정합니다.
     *
     * @param method HTTP 메서드
     * @return 현재 ApiRequestBuilder 인스턴스
     */
    public ApiRequestBuilder<T> method(HttpMethod method) {
        this.method = method;
        return this;
    }

    /**
     * 요청할 URL을 설정합니다.
     *
     * @param url 요청할 URL
     * @return 현재 ApiRequestBuilder 인스턴스
     */
    public ApiRequestBuilder<T> url(String url) {
        this.url = url;
        return this;
    }

    /**
     * 요청 헤더에 키와 값을 추가합니다.
     *
     * @param key 헤더 키
     * @param value 헤더 값
     * @return 현재 ApiRequestBuilder 인스턴스
     */
    public ApiRequestBuilder<T> header(String key, String value) {
        this.headers.add(key, value);
        return this;
    }

    /**
     * 여러 헤더를 추가합니다.
     *
     * @param headers 추가할 헤더 맵
     * @return 현재 ApiRequestBuilder 인스턴스
     */
    public ApiRequestBuilder<T> headers(MultiValueMap<String, String> headers) {
        this.headers.addAll(headers);
        return this;
    }

    /**
     * 요청 본문을 설정합니다.
     *
     * @param body 요청 본문
     * @return 현재 ApiRequestBuilder 인스턴스
     */
    public ApiRequestBuilder<T> body(Object body) {
        this.body = body;
        return this;
    }

    /**
     * 응답 타입을 설정합니다.
     *
     * @param responseType 응답 타입 클래스
     * @return 현재 ApiRequestBuilder 인스턴스
     */
    public ApiRequestBuilder<T> responseType(Class<T> responseType) {
        this.responseType = responseType;
        return this;
    }

    /**
     * 설정된 값들로 {@link ApiRequest} 인스턴스를 빌드합니다.
     *
     * @return 빌드된 ApiRequest 인스턴스
     */
    public ApiRequest<T> build() {
        return new DefaultApiRequest<>(method, url, headers, body, responseType);
    }

    /**
     * 기본 API 요청 구현 클래스입니다.
     *
     * @param <T> 응답 타입
     */
    @Data
    @RequiredArgsConstructor
    private static class DefaultApiRequest<T> implements ApiRequest<T> {

        private final HttpMethod method;
        private final String url;
        private final HttpHeaders headers;
        private final Object body;
        private final Class<T> responseType;
    }
}
