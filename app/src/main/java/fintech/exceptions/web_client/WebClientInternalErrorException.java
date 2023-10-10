package fintech.exceptions.web_client;

import org.springframework.http.HttpStatusCode;

public class WebClientInternalErrorException extends WebClientBaseException {
    public WebClientInternalErrorException(HttpStatusCode httpStatusCode,
            String errorMessage) {
        super(httpStatusCode, errorMessage);
    }

    public WebClientInternalErrorException(Throwable cause,
            HttpStatusCode httpStatusCode,
            String errorMessage) {
        super(cause, httpStatusCode, errorMessage);
    }
}
