package fintech.controllers;

import fintech.dto.WeatherApiDotComErrorMessageDto;
import fintech.exceptions.WebClientApiKeyDisabledException;
import fintech.exceptions.WebClientIncorrectBulkRequestException;
import fintech.exceptions.WebClientInternalErrorException;
import fintech.exceptions.WebClientLocationNotFoundException;
import fintech.exceptions.WebClientNoAccessToResourceForSubscriptionPlanException;
import fintech.exceptions.WebClientQuotaPerMonthExceededException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/weather")
@RequiredArgsConstructor
public class WeatherApiDotComClientController {

    @Qualifier("weatherApiDotComClient")
    private final WebClient client;

    @Value("${weather_api_dot_com.auth_key}")
    private String authKey;

    @GetMapping("/now/{city}")
    public Mono<Object> getCurrentWeatherForCity(@PathVariable("city") String city) {
        return client.get()
                .uri(uriBuilder -> uriBuilder.queryParam("key", authKey)
                        .queryParam("q", city)
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(
                        httpCode -> httpCode.is4xxClientError() || httpCode.is5xxServerError(),
                        WeatherApiDotComClientController::convertErrorResponseToException)
                .bodyToMono(Object.class);
    }

    private static Mono<Exception> convertErrorResponseToException(ClientResponse response) {
        WeatherApiDotComErrorMessageDto errorMessageDto = response.bodyToMono(WeatherApiDotComErrorMessageDto.class)
                .block();
        int errorCode = errorMessageDto.getError().getCode();
        String errorMessage = errorMessageDto.getError().getMessage();
        Exception exception = convertErrorToException(errorCode, errorMessage);
        return Mono.error(exception);
    }

    private static Exception convertErrorToException(int errorCode,
            String errorMessage) {
        return switch (errorCode) {

            // Errors we are responsible for
            case 1002, 1003, 1005, 2006 -> new WebClientInternalErrorException("Internal error occurred");

            // Errors user should know about
            case 1006 -> new WebClientLocationNotFoundException(errorMessage);
            case 2007 -> new WebClientQuotaPerMonthExceededException(errorMessage);
            case 2008 -> new WebClientApiKeyDisabledException(errorMessage);
            case 2009 -> new WebClientNoAccessToResourceForSubscriptionPlanException(errorMessage);
            case 9000, 9001 -> new WebClientIncorrectBulkRequestException(errorMessage);

            // Erros user shouldn't know about
            case 9999 -> new WebClientInternalErrorException("Unknown error occurred");

            default -> throw new RuntimeException("Unknown error code");
        };
    }
}
