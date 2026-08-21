package dev.nmarulo.ahorraco_api.commons.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class DefaultHttpException extends ResponseStatusException {
    
    public DefaultHttpException(HttpStatus status, String reason) {
        this(status, reason, null);
    }
    
    public DefaultHttpException(HttpStatus status, String reason, Throwable cause) {
        super(status, reason, cause);
    }
    
}
