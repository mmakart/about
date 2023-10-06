package fintech.exceptions.web_client;

import org.springframework.http.HttpStatusCode;

public class WebClientNoAccessToResourceForSubscriptionPlanException
        extends WebClientBaseException {
    public WebClientNoAccessToResourceForSubscriptionPlanException(
            HttpStatusCode httpStatusCode, String errorMessage) {
        super(httpStatusCode, errorMessage);
    }

    public WebClientNoAccessToResourceForSubscriptionPlanException(
            Throwable cause, HttpStatusCode httpStatusCode, String errorMessage) {
        super(cause, httpStatusCode, errorMessage);
    }
}
