package de.hskl.repominer.endpoints;

import javassist.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;

@ControllerAdvice
public class ExceptionResponder extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {IllegalArgumentException.class, IllegalStateException.class})
    public ResponseEntity<?> handleBadRequest(RuntimeException ex, WebRequest request) {
        return handleDefault(ex, request, HttpStatus.BAD_REQUEST, true);
    }

    @ExceptionHandler(value = {NotFoundException.class})
    public ResponseEntity<?> handleNotFound(RuntimeException ex, WebRequest request) {
        return handleDefault(ex, request, HttpStatus.NOT_FOUND, false);
    }

    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<ErrorDetails> handleException(RuntimeException ex, WebRequest request) {
        return handleDefault(ex, request, HttpStatus.INTERNAL_SERVER_ERROR, true);
    }

    private ResponseEntity<ErrorDetails> handleDefault(Exception ex, WebRequest request, HttpStatus status,
                                                       boolean printStackTrace) {
        if(printStackTrace) {
            ex.printStackTrace();
        }
        ErrorDetails errorDetails = new ErrorDetails(new Date(), ex.getMessage(),
                request.getDescription(false));
        return new ResponseEntity<>(errorDetails, status);
    }

    public static class ErrorDetails {
        private final Date timestamp;
        private final String message;
        private final String details;

        public ErrorDetails(Date timestamp, String message, String details) {
            super();
            this.timestamp = timestamp;
            this.message = message;
            this.details = details;
        }

        public Date getTimestamp() {
            return timestamp;
        }

        public String getMessage() {
            return message;
        }


        public String getDetails() {
            return details;
        }
    }

}
