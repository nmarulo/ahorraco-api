package dev.nmarulo.ahorraco_api.commons.handler;

import dev.nmarulo.ahorraco_api.commons.dtos.ErrorRes;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.Nullable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@RestControllerAdvice
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {
    
    private static final String INTERNAL_ERROR_MESSAGE = "Algo ha ido mal por nuestra parte. Inténtalo de nuevo.";
    
    private static final String CONFLICT_MESSAGE = "Alguien se ha adelantado y esa acción ya no vale. Inténtalo de nuevo.";
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleException(Exception ex) {
        log.error("Error no controlado", ex);
        
        return ResponseEntity.internalServerError()
                             .body(newErrorRes(INTERNAL_ERROR_MESSAGE, HttpStatus.INTERNAL_SERVER_ERROR));
    }
    
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Object> handleResponseStatusException(ResponseStatusException ex) {
        return ResponseEntity.status(ex.getStatusCode())
                             .body(new ErrorRes(ex.getBody()));
    }
    
    /**
     * Para los casos que se llame dos veces a la vez a la misma acción y al intentar guardar ya pueda.
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Object> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        log.warn("Violación de integridad", ex);
        
        return ResponseEntity.status(HttpStatus.CONFLICT)
                             .body(newErrorRes(CONFLICT_MESSAGE, HttpStatus.CONFLICT));
    }
    
    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex,
                                                             @Nullable Object body,
                                                             HttpHeaders headers,
                                                             HttpStatusCode statusCode,
                                                             WebRequest request) {
        return ResponseEntity.status(statusCode)
                             .body(newErrorRes(ex.getMessage(), statusCode));
    }
    
    private ErrorRes newErrorRes(final String detail, final HttpStatusCode statusCode) {
        final var problemDetail = ProblemDetail.forStatusAndDetail(statusCode, detail);
        
        return new ErrorRes(problemDetail);
    }
    
}
