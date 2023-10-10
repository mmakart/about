package fintech.exceptions.web_client;

import fintech.dto.WebClientErrorMessageDto;
import lombok.Getter;
import org.springframework.http.HttpStatusCode;

@Getter
public abstract class WebClientBaseException extends RuntimeException {
    private static final String STANDARD_MESSAGE = "Some web client error occurred.";

    private final WebClientErrorMessageDto errorMessageDto;
    private final HttpStatusCode httpStatusCode;

    public WebClientBaseException(HttpStatusCode httpStatusCode,
            String errorMessage) {
        super(STANDARD_MESSAGE);
        this.errorMessageDto = new WebClientErrorMessageDto(errorMessage);
        this.httpStatusCode = httpStatusCode;
    }

    public WebClientBaseException(Throwable cause, HttpStatusCode httpStatusCode,
            String errorMessage) {
        super(STANDARD_MESSAGE, cause);
        this.errorMessageDto = new WebClientErrorMessageDto(errorMessage);
        this.httpStatusCode = httpStatusCode;
    }
}
