package fintech.dto;

import lombok.Data;

@Data
public class WeatherApiDotComErrorMessageDto {
    @Data
    public static class Error {
        int code;
        String message;
    }

    private Error error;
}
