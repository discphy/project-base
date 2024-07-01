package io.bareun.base.api.request;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.util.MultiValueMap;

public class ApiRequestBuilder<T> {

    private HttpMethod method;
    private String url;
    private Object body;
    private Class<T> responseType;

    private final HttpHeaders headers = new HttpHeaders();

    public static <T> ApiRequestBuilder<T> builder() {
        return new ApiRequestBuilder<>();
    }

    public ApiRequestBuilder<T> method(HttpMethod method) {
        this.method = method;
        return this;
    }

    public ApiRequestBuilder<T> url(String url) {
        this.url = url;
        return this;
    }

    public ApiRequestBuilder<T> header(String key, String value) {
        this.headers.add(key, value);
        return this;
    }

    public ApiRequestBuilder<T> headers(MultiValueMap<String, String> headers) {
        this.headers.addAll(headers);
        return this;
    }

    public ApiRequestBuilder<T> body(Object body) {
        this.body = body;
        return this;
    }

    public ApiRequestBuilder<T> responseType(Class<T> responseType) {
        this.responseType = responseType;
        return this;
    }

    public ApiRequest<T> build() {
        return new DefaultApiRequest<>(method, url, headers, body, responseType);
    }

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
