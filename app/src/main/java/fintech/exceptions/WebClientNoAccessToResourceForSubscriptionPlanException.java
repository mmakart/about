package fintech.exceptions;

public class WebClientNoAccessToResourceForSubscriptionPlanException
        extends RuntimeException {
    public WebClientNoAccessToResourceForSubscriptionPlanException() {
        super();
    }

    public WebClientNoAccessToResourceForSubscriptionPlanException(
            String message) {
        super(message);
    }

    public WebClientNoAccessToResourceForSubscriptionPlanException(
            Throwable cause) {
        super(cause);
    }

    public WebClientNoAccessToResourceForSubscriptionPlanException(
            String message, Throwable cause) {
        super(message, cause);
    }
}
