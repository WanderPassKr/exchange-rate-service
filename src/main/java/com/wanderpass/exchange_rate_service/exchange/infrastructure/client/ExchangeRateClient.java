package com.wanderpass.exchange_rate_service.exchange.infrastructure.client;

import com.wanderpass.exchange_rate_service.exchange.application.dto.ExchangeRateResponse;
import com.wanderpass.exchange_rate_service.exchange.config.OpenExchangeProperties;
import com.wanderpass.exchange_rate_service.exchange.exception.exchange.ExchangeRateApiException;
import com.wanderpass.exchange_rate_service.exchange.exception.type.StatusCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;

import static org.springframework.http.HttpStatus.TOO_MANY_REQUESTS;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Component
@RequiredArgsConstructor
public class ExchangeRateClient {

    private final WebClient.Builder webClientBuilder;
    private final OpenExchangeProperties openExchangeProperties;

    private WebClient webClient() {
        return webClientBuilder.baseUrl(openExchangeProperties.getBaseUrl()).build();
    }

    private String buildLatestRatesUri() {
        return UriComponentsBuilder.fromPath("/latest.json")
                .queryParam("app_id", openExchangeProperties.getApiKey())
                .queryParam("base", "USD")
                .toUriString();
    }

    public Mono<ExchangeRateResponse> getLatestRates() {
        return webClient()
                .get()
                .uri(buildLatestRatesUri())
                .retrieve()
                .onStatus(status -> status.is4xxClientError(), response ->
                        Mono.error(new ExchangeRateApiException(
                                StatusCode.EXTERNAL_API_BAD_REQUEST,
                                "환율 API 4xx 오류가 발생했습니다. 잘못된 요청일 수 있습니다."
                        )))
                .onStatus(status -> status.is5xxServerError(), response ->
                        Mono.error(new ExchangeRateApiException(
                                StatusCode.EXTERNAL_API_SERVER_ERROR,
                                "환율 API 5xx 오류가 발생했습니다. 외부 서버 문제일 수 있습니다."
                        )))
                .bodyToMono(ExchangeRateResponse.class);
    }

    public Mono<ExchangeRateResponse> getLatestRatesWithRetry() {
        return webClient()
                .get()
                .uri(buildLatestRatesUri())
                .retrieve()
                .bodyToMono(ExchangeRateResponse.class)
                .retryWhen(defaultRetryPolicy())
                .timeout(Duration.ofSeconds(10))
                .onErrorMap(WebClientResponseException.class, this::mapWebClientException);
    }

    private Retry defaultRetryPolicy() {
        return Retry.backoff(3, Duration.ofSeconds(2));
    }

    private Throwable mapWebClientException(WebClientResponseException ex) {
        return switch (ex.getStatusCode()) {
            case UNAUTHORIZED -> new ExchangeRateApiException(
                    StatusCode.EXTERNAL_API_UNAUTHORIZED,
                    "환율 API 인증이 실패했습니다. API 키를 확인해주세요."
            );
            case TOO_MANY_REQUESTS -> new ExchangeRateApiException(
                    StatusCode.EXTERNAL_API_RATE_LIMITED,
                    "환율 API 요청이 너무 많습니다. 잠시 후 다시 시도해주세요."
            );
            default -> new ExchangeRateApiException(
                    StatusCode.EXTERNAL_API_UNKNOWN,
                    "환율 API 요청 중 알 수 없는 오류가 발생했습니다: " + ex.getMessage()
            );
        };
    }
}
