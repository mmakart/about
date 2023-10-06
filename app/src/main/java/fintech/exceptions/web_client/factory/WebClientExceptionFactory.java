package fintech.exceptions.web_client.factory;

import fintech.exceptions.web_client.WebClientApiKeyDisabledException;
import fintech.exceptions.web_client.WebClientBaseException;
import fintech.exceptions.web_client.WebClientIncorrectBulkRequestException;
import fintech.exceptions.web_client.WebClientInternalErrorException;
import fintech.exceptions.web_client.WebClientLocationNotFoundException;
import fintech.exceptions.web_client.WebClientNoAccessToResourceForSubscriptionPlanException;
import fintech.exceptions.web_client.WebClientQuotaPerMonthExceededException;
import org.springframework.http.HttpStatus;

public class WebClientExceptionFactory
        extends AbstractWebClientExceptionFactory {

    @Override
    public WebClientBaseException getException(int errorCode,
            String errorMessage) {
        return switch (errorCode) {

            // Errors we are responsible for
            case 1002, 1003, 1005, 2006 ->
                new WebClientInternalErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal error occurred");

            // Errors user should know about
            case 1006 -> new WebClientLocationNotFoundException(HttpStatus.NOT_FOUND, errorMessage);
            case 2007 -> new WebClientQuotaPerMonthExceededException(HttpStatus.FORBIDDEN, errorMessage);
            case 2008 -> new WebClientApiKeyDisabledException(HttpStatus.FORBIDDEN, errorMessage);
            case 2009 ->
                new WebClientNoAccessToResourceForSubscriptionPlanException(HttpStatus.FORBIDDEN, errorMessage);
            case 9000, 9001 -> new WebClientIncorrectBulkRequestException(HttpStatus.BAD_REQUEST, errorMessage);

            // Erros user shouldn't know about
            case 9999 ->
                new WebClientInternalErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "Unknown error occurred");

            default -> throw new RuntimeException("Unknown error code");
        };
    }
}
