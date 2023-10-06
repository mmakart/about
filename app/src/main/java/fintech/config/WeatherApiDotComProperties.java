package fintech.config;

import jakarta.validation.constraints.NotBlank;
import lombok.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@Value
@ConfigurationProperties(prefix = "weather-api-dot-com")
public class WeatherApiDotComProperties {

    @NotBlank
    private final String authKey;
}
