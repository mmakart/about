package fintech.dto;

import lombok.Data;

@Data
public class WeatherApiDotComErrorMessageDto {
    @Data
    public static class Error {
        private int code;
        private String message;
    }

    private Error error;
}
