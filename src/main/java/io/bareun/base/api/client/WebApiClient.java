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

@Slf4j
@Component
public class WebApiClient {

    private final WebClient webClient;

    private static final int MAX_MEMORY = 5 * 1024 * 1024;

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

    public <T> T callReturn(ApiRequest<T> request) {
        return retrieve(request).block();
    }

    public <T> void call(ApiRequest<T> request) {
        retrieve(request).subscribe(request::subscribe, request::error);
    }

    private <T> Mono<T> retrieve(ApiRequest<T> request) {
        return requestSpec(request).retrieve().bodyToMono(request.getResponseType());
    }

    private RequestHeadersSpec<?> requestSpec(ApiRequest<?> request) {
        RequestBodySpec spec = webClient.method(request.getMethod())
                .uri(request.getUrl())
                .headers(h -> h.addAll(request.getHeaders()));

        return request.getBody() != null ? spec.bodyValue(request.getBody()) : spec;
    }
}
