package fintech.exceptions.web_client.factory;

import fintech.exceptions.web_client.WebClientBaseException;

public abstract class AbstractWebClientExceptionFactory {
    public abstract WebClientBaseException getException(int errorCode,
            String errorMessage);
}
