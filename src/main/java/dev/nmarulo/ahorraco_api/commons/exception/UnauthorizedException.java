package dev.nmarulo.ahorraco_api.commons.exception;

import org.springframework.http.HttpStatus;

public class UnauthorizedException extends DefaultHttpException {
    
    public UnauthorizedException(String reason) {
        super(HttpStatus.UNAUTHORIZED, reason);
    }
    
    public UnauthorizedException(String reason, Throwable cause) {
        super(HttpStatus.UNAUTHORIZED, reason, cause);
    }
    
}
