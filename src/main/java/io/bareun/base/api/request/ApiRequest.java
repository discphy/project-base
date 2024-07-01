package io.bareun.base.api.request;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.util.MultiValueMap;

import java.util.function.Consumer;

public interface ApiRequest<T> {

    HttpMethod getMethod();

    String getUrl();

    Object getBody();

    Class<T> getResponseType();

    default HttpHeaders getHeaders() {
        return new HttpHeaders();
    }

    default Consumer<T> getSubscribe() {
        return (response) -> getLogger().info("Api request subscribe: {}", response);
    }

    default Consumer<Throwable> getError() {
        return (error) -> getLogger().error("Api request error", error);
    }

    default Logger getLogger() {
        return LoggerFactory.getLogger(this.getClass());
    }

    default ApiRequest<T> header(String key, String value) {
        getHeaders().add(key, value);
        return this;
    }

    default ApiRequest<T> headers(MultiValueMap<String, String> headers) {
        getHeaders().addAll(headers);
        return this;
    }

    default void subscribe(T response) {
        getSubscribe().accept(response);
    }

    default void error(Throwable throwable) {
        getError().accept(throwable);
    }
}
