package fintech.controllers;

import fintech.dto.WeatherApiDotComErrorMessageDto;
import fintech.exceptions.web_client.WebClientBaseException;
import fintech.exceptions.web_client.factory.WebClientExceptionFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
                        response -> {
                            WeatherApiDotComErrorMessageDto errorMessageDto = response
                                    .bodyToMono(WeatherApiDotComErrorMessageDto.class)
                                    .block();
                            int errorCode = errorMessageDto.getError().getCode();
                            String errorMessage = errorMessageDto.getError().getMessage();

                            WebClientExceptionFactory exceptionFactory = new WebClientExceptionFactory();
                            WebClientBaseException exception = exceptionFactory.getException(errorCode, errorMessage);
                            return Mono.error(exception);
                        })
                .bodyToMono(Object.class);
    }
}
