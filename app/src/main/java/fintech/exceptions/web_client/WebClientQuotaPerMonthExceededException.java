package fintech.exceptions.web_client;

import org.springframework.http.HttpStatusCode;

public class WebClientQuotaPerMonthExceededException
        extends WebClientBaseException {
    public WebClientQuotaPerMonthExceededException(HttpStatusCode httpStatusCode,
            String errorMessage) {
        super(httpStatusCode, errorMessage);
    }

    public WebClientQuotaPerMonthExceededException(Throwable cause,
            HttpStatusCode httpStatusCode,
            String errorMessage) {
        super(cause, httpStatusCode, errorMessage);
    }
}
