package fintech.exceptions.web_client;

import org.springframework.http.HttpStatusCode;

public class WebClientLimitRateExceededException
        extends WebClientBaseException {
    public WebClientLimitRateExceededException(HttpStatusCode httpStatusCode,
            String errorMessage) {
        super(httpStatusCode, errorMessage);
    }

    public WebClientLimitRateExceededException(Throwable cause,
            HttpStatusCode httpStatusCode,
            String errorMessage) {
        super(cause, httpStatusCode, errorMessage);
    }
}
