package fintech.controllers.exception_handlers;

import fintech.controllers.WeatherApiDotComClientController;
import fintech.dto.WebClientErrorMessageDto;
import fintech.exceptions.web_client.WebClientBaseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@RestControllerAdvice(assignableTypes = WeatherApiDotComClientController.class)
public class WeatherApiDotComClientControllerExceptionHandler {

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<WebClientErrorMessageDto> handleGeneralException(Throwable ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new WebClientErrorMessageDto(ex.getMessage()));
    }

    @ExceptionHandler(WebClientResponseException.class)
    public ResponseEntity<WebClientErrorMessageDto> handleGeneralWebClientException(WebClientResponseException ex) {
        return ResponseEntity.status(ex.getStatusCode())
                .body(new WebClientErrorMessageDto(ex.getStatusText()));
    }

    @ExceptionHandler(WebClientBaseException.class)
    public ResponseEntity<WebClientErrorMessageDto> handleSpecificWebClientException(WebClientBaseException ex) {
        return ResponseEntity.status(ex.getHttpStatusCode())
                .body(ex.getErrorMessageDto());
    }
}
