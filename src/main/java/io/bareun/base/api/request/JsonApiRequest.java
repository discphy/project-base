package io.bareun.base.api.request;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;

public interface JsonApiRequest<T> extends ApiRequest<T> {

    @Override
    default HttpMethod getMethod() {
        return HttpMethod.POST;
    }

    @Override
    default HttpHeaders getHeaders() {
        HttpHeaders headers = ApiRequest.super.getHeaders();
        headers.add("Content-Type", "application/json");
        return headers;
    }
}
