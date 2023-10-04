package fintech.exceptions;

public class WebClientIncorrectBulkRequestException extends RuntimeException {
    public WebClientIncorrectBulkRequestException() {
        super();
    }

    public WebClientIncorrectBulkRequestException(String message) {
        super(message);
    }

    public WebClientIncorrectBulkRequestException(Throwable cause) {
        super(cause);
    }

    public WebClientIncorrectBulkRequestException(String message,
            Throwable cause) {
        super(message, cause);
    }
}
