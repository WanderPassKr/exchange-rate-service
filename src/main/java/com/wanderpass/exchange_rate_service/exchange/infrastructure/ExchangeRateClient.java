package com.wanderpass.exchange_rate_service.exchange.infrastructure;

import com.wanderpass.exchange_rate_service.exchange.application.dto.ExchangeRateResponse;
import com.wanderpass.exchange_rate_service.exchange.config.OpenExchangeProperties;
import com.wanderpass.exchange_rate_service.exchange.exception.BusinessException;
import com.wanderpass.exchange_rate_service.exchange.exception.ErrorCode;
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
                        Mono.error(new BusinessException(ErrorCode.EXTERNAL_API_BAD_REQUEST)))
                .onStatus(status -> status.is5xxServerError(), response ->
                        Mono.error(new BusinessException(ErrorCode.EXTERNAL_API_SERVER_ERROR)))
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
            case UNAUTHORIZED -> new BusinessException(ErrorCode.EXTERNAL_API_UNAUTHORIZED);
            case TOO_MANY_REQUESTS -> new BusinessException(ErrorCode.EXTERNAL_API_RATE_LIMITED);
            default -> new BusinessException(ErrorCode.EXTERNAL_API_UNKNOWN, ex.getMessage());
        };
    }
}