package fintech.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import fintech.config.WeatherApiDotComProperties;
import fintech.dao.home_task_6.WeatherJdbcDao;
import fintech.dto.WeatherApiDotComErrorMessageDto;
import fintech.exceptions.web_client.WebClientBaseException;
import fintech.exceptions.web_client.factory.WebClientExceptionFactory;
import fintech.models.home_task_6.Weather;
import io.github.resilience4j.ratelimiter.RateLimiter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.util.function.Function;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
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

    private final WeatherApiDotComProperties props;

    @Qualifier("standardWebApiDotComClientRateLimiter")
    private final RateLimiter rateLimiter;

    private final WeatherJdbcDao weatherDao;

    @Operation(summary = "Get current weather by city name")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Found the weather", content = {
                    @Content(mediaType = "application/json") }),
            @ApiResponse(responseCode = "400", description = "Wrong request"),
            @ApiResponse(responseCode = "403", description = "Problems with the API token"),
            @ApiResponse(responseCode = "404", description = "Requested city not found"),
            @ApiResponse(responseCode = "429", description = "Request limit exceeded"),
            @ApiResponse(responseCode = "500", description = "Unknown or some internal error occurred")
    })
    @GetMapping("/now/{city}")
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public Mono<String> getCurrentWeatherForCity(
            @Parameter(description = "The city to get weather for") @PathVariable("city") String city) {
        Function<String, Mono<String>> function = innerCity -> client.get()
                .uri(uriBuilder -> uriBuilder.queryParam("key", props.getAuthKey())
                        .queryParam("q", innerCity)
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(
                        httpCode -> httpCode.is4xxClientError() ||
                                httpCode.is5xxServerError(),
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
                .bodyToMono(String.class);
        Function<String, Mono<String>> rateLimitedFunction = RateLimiter.decorateFunction(rateLimiter, function);

        Mono<String> mono = rateLimitedFunction.apply(city);

        Weather weather = mono.map(jsonString -> {
            try {
                ObjectMapper mapper = new ObjectMapper();
                JsonNode node = mapper.readTree(jsonString);
                String weatherType = node.get("current").get("condition").get("text").asText();
                return new Weather(null, weatherType);
            } catch (JsonProcessingException e) {
                throw new RuntimeException("Error while parsing JSON", e);
            }
        })
                .block();
        weatherDao.save(weather);

        return mono;
    }
}
