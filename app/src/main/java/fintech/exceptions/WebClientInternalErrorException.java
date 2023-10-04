package fintech.exceptions;

public class WebClientInternalErrorException extends RuntimeException {
    public WebClientInternalErrorException() {
        super();
    }

    public WebClientInternalErrorException(String message) {
        super(message);
    }

    public WebClientInternalErrorException(Throwable cause) {
        super(cause);
    }

    public WebClientInternalErrorException(String message, Throwable cause) {
        super(message, cause);
    }
}
