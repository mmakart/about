package fintech.exceptions;

public class WeatherNotFoundException extends RuntimeException {
    public WeatherNotFoundException() {
        super();
    }

    public WeatherNotFoundException(String message) {
        super(message);
    }

    public WeatherNotFoundException(Throwable cause) {
        super(cause);
    }

    public WeatherNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
