package fintech.exceptions;

public class WebClientApiKeyDisabledException extends RuntimeException {
    public WebClientApiKeyDisabledException() {
        super();
    }

    public WebClientApiKeyDisabledException(String message) {
        super(message);
    }

    public WebClientApiKeyDisabledException(Throwable cause) {
        super(cause);
    }

    public WebClientApiKeyDisabledException(String message, Throwable cause) {
        super(message, cause);
    }
}
