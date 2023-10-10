package fintech.exceptions.web_client;

import org.springframework.http.HttpStatusCode;

public class WebClientApiKeyDisabledException extends WebClientBaseException {
    public WebClientApiKeyDisabledException(HttpStatusCode httpStatusCode,
            String errorMessage) {
        super(httpStatusCode, errorMessage);
    }

    public WebClientApiKeyDisabledException(Throwable cause,
            HttpStatusCode httpStatusCode,
            String errorMessage) {
        super(cause, httpStatusCode, errorMessage);
    }
}
