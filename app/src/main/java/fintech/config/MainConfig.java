package fintech.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class MainConfig {
    @Bean
    public WebClient weatherApiDotComClient() {
        return WebClient.builder()
                .baseUrl("https://api.weatherapi.com/v1/current.json")
                .build();
    }
}