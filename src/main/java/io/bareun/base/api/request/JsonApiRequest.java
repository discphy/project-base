package io.bareun.base.api.request;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;

/**
 * JSON 형식의 API 요청을 표현하는 인터페이스입니다.
 *
 * @param <T> 응답 타입
 */
public interface JsonApiRequest<T> extends ApiRequest<T> {

    /**
     * 기본 HTTP 메서드를 POST로 반환합니다.
     *
     * @return POST 메서드
     */
    @Override
    default HttpMethod getMethod() {
        return HttpMethod.POST;
    }

    /**
     * 기본 HTTP 헤더에 "Content-Type: application/json"을 추가하여 반환합니다.
     *
     * @return 요청에 사용될 HTTP 헤더
     */
    @Override
    default HttpHeaders getHeaders() {
        HttpHeaders headers = ApiRequest.super.getHeaders();
        headers.add("Content-Type", "application/json");
        return headers;
    }
}
