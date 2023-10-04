package fintech.exceptions;

public class WebClientLocationNotFoundException extends RuntimeException {
    public WebClientLocationNotFoundException() {
        super();
    }

    public WebClientLocationNotFoundException(String message) {
        super(message);
    }

    public WebClientLocationNotFoundException(Throwable cause) {
        super(cause);
    }

    public WebClientLocationNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
