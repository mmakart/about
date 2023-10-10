package fintech.exceptions.web_client;

import org.springframework.http.HttpStatusCode;

public class WebClientIncorrectBulkRequestException
        extends WebClientBaseException {
    public WebClientIncorrectBulkRequestException(HttpStatusCode httpStatusCode,
            String errorMessage) {
        super(httpStatusCode, errorMessage);
    }

    public WebClientIncorrectBulkRequestException(Throwable cause,
            HttpStatusCode httpStatusCode,
            String errorMessage) {
        super(cause, httpStatusCode, errorMessage);
    }
}
