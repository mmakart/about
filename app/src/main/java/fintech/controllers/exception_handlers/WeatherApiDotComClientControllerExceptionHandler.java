package fintech.controllers.exception_handlers;

import fintech.controllers.WeatherApiDotComClientController;
import fintech.exceptions.WebClientApiKeyDisabledException;
import fintech.exceptions.WebClientIncorrectBulkRequestException;
import fintech.exceptions.WebClientInternalErrorException;
import fintech.exceptions.WebClientLocationNotFoundException;
import fintech.exceptions.WebClientNoAccessToResourceForSubscriptionPlanException;
import fintech.exceptions.WebClientQuotaPerMonthExceededException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@RestControllerAdvice(assignableTypes = WeatherApiDotComClientController.class)
public class WeatherApiDotComClientControllerExceptionHandler {

    @ExceptionHandler(Throwable.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<String> handleGeneralException(Throwable ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ex.getMessage());
    }

    @ExceptionHandler(WebClientResponseException.class)
    public ResponseEntity<String> handleGeneralWebClientException(WebClientResponseException ex) {
        return ResponseEntity.status(ex.getStatusCode())
                .body(ex.getResponseBodyAsString());
    }

    @ExceptionHandler(WebClientApiKeyDisabledException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<String> handleApiKeyDisabledException(WebClientApiKeyDisabledException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.getMessage());
    }

    @ExceptionHandler(WebClientIncorrectBulkRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleIncorrectBulkRequestException(
            WebClientIncorrectBulkRequestException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(WebClientInternalErrorException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<String> handleInternalErrorException(WebClientInternalErrorException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ex.getMessage());
    }

    @ExceptionHandler(WebClientLocationNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<String> handleLocationNotFoundException(WebClientLocationNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(WebClientNoAccessToResourceForSubscriptionPlanException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<String> handleNoAccessToResourceForSubscriptionPlanException(
            WebClientNoAccessToResourceForSubscriptionPlanException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.getMessage());
    }

    @ExceptionHandler(WebClientQuotaPerMonthExceededException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<String> handleQuotaPerMonthExceededException(
            WebClientQuotaPerMonthExceededException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.getMessage());
    }
}
