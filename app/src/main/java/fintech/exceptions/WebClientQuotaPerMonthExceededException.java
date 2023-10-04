package fintech.exceptions;

public class WebClientQuotaPerMonthExceededException extends RuntimeException {
    public WebClientQuotaPerMonthExceededException() {
        super();
    }

    public WebClientQuotaPerMonthExceededException(String message) {
        super(message);
    }

    public WebClientQuotaPerMonthExceededException(Throwable cause) {
        super(cause);
    }

    public WebClientQuotaPerMonthExceededException(String message,
            Throwable cause) {
        super(message, cause);
    }
}
