package io.bareun.base.api.request;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;

import java.util.function.Consumer;

/**
 * API 요청을 표현하는 인터페이스입니다.
 *
 * @param <T> 응답 타입
 */
public interface ApiRequest<T> {

    /**
     * HTTP 메서드를 반환합니다.
     *
     * @return 요청에 사용될 HTTP 메서드
     */
    HttpMethod getMethod();

    /**
     * 요청할 URL을 반환합니다.
     *
     * @return 요청할 URL
     */
    String getUrl();

    /**
     * 요청 본문을 반환합니다.
     *
     * @return 요청 본문
     */
    Object getBody();

    /**
     * 응답 타입을 반환합니다.
     *
     * @return 응답 타입 클래스
     */
    Class<T> getResponseType();

    /**
     * HTTP 헤더를 반환합니다. 기본적으로 빈 {@link HttpHeaders}를 반환합니다.
     *
     * @return 요청에 사용될 HTTP 헤더
     */
    default HttpHeaders getHeaders() {
        return new HttpHeaders();
    }

    /**
     * 성공적인 응답을 처리하는 기본 소비자를 반환합니다.
     * 기본적으로 로그를 남깁니다.
     *
     * @return 성공적인 응답을 처리하는 {@link Consumer}
     */
    default Consumer<T> getSubscribe() {
        return (response) -> getLogger().info("Api request subscribe: {}", response);
    }

    /**
     * 오류를 처리하는 기본 소비자를 반환합니다.
     * 기본적으로 오류 로그를 남깁니다.
     *
     * @return 오류를 처리하는 {@link Consumer}
     */
    default Consumer<Throwable> getError() {
        return (error) -> getLogger().error("Api request error", error);
    }

    /**
     * 로거를 반환합니다. 기본적으로 현재 클래스의 로거를 반환합니다.
     *
     * @return 로거 인스턴스
     */
    default Logger getLogger() {
        return LoggerFactory.getLogger(this.getClass());
    }

    /**
     * 성공적인 응답을 처리합니다. 기본적으로 {@link #getSubscribe()} 소비자를 호출합니다.
     *
     * @param response 응답 본문
     */
    default void subscribe(T response) {
        getSubscribe().accept(response);
    }

    /**
     * 오류를 처리합니다. 기본적으로 {@link #getError()} 소비자를 호출합니다.
     *
     * @param throwable 처리할 오류
     */
    default void error(Throwable throwable) {
        getError().accept(throwable);
    }
}
