package fintech.exceptions;

public class IncorrectDateAndTimeFormatException extends RuntimeException {
    public IncorrectDateAndTimeFormatException() {
        super();
    }

    public IncorrectDateAndTimeFormatException(String message) {
        super(message);
    }

    public IncorrectDateAndTimeFormatException(Throwable cause) {
        super(cause);
    }

    public IncorrectDateAndTimeFormatException(String message, Throwable cause) {
        super(message, cause);
    }
}
