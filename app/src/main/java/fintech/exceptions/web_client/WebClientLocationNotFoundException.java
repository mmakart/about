package fintech.exceptions.web_client;

import org.springframework.http.HttpStatusCode;

public class WebClientLocationNotFoundException extends WebClientBaseException {
    public WebClientLocationNotFoundException(HttpStatusCode httpStatusCode,
            String errorMessage) {
        super(httpStatusCode, errorMessage);
    }

    public WebClientLocationNotFoundException(Throwable cause,
            HttpStatusCode httpStatusCode,
            String errorMessage) {
        super(cause, httpStatusCode, errorMessage);
    }
}
