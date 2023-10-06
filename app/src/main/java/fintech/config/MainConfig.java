package fintech.config;

import fintech.exceptions.web_client.WebClientLimitRateExceededException;
import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import java.time.Duration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@EnableConfigurationProperties(WeatherApiDotComProperties.class)
public class MainConfig {
    @Bean
    public WebClient weatherApiDotComClient() {
        return WebClient.builder()
                .baseUrl("https://api.weatherapi.com/v1/current.json")
                .build();
    }

    // @Bean
    public RateLimiter standardWebApiDotComClientRateLimiter() {
        RateLimiterConfig config = RateLimiterConfig.custom()
                .limitRefreshPeriod(Duration.ofDays(30))
                .limitForPeriod(1_000_000)
                .timeoutDuration(Duration.ofSeconds(5))
                .build();

        RateLimiterRegistry registry = RateLimiterRegistry.of(config);

        return registry.rateLimiter("standard_rate_limiter");
    }

    @Bean
    public RateLimiter testWebApiDotComClientRateLimiter() {
        RateLimiterConfig config = RateLimiterConfig.custom()
                .limitRefreshPeriod(Duration.ofMinutes(1))
                .limitForPeriod(3)
                .timeoutDuration(Duration.ofSeconds(5))
                .build();

        RateLimiterRegistry registry = RateLimiterRegistry.of(config);

        RateLimiter rateLimiter = registry.rateLimiter("test_rate_limiter");
        rateLimiter.getEventPublisher().onFailure(event -> {
            throw new WebClientLimitRateExceededException(
                    HttpStatus.TOO_MANY_REQUESTS,
                    "Request limit rate exceeded for now. Please wait.");
        });
        return rateLimiter;
    }
}
