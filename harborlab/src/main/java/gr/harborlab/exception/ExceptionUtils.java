package gr.harborlab.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.UUID;


public class ExceptionUtils extends RuntimeException {
    private final ErrorType errorType;

    public ExceptionUtils(ErrorType errorType, String message) {
        super(message);
        this.errorType = errorType;
    }

    public ErrorType getErrorType() {
        return errorType;
    }

    public enum ErrorType {
        BAD_REQUEST, RESOURCE_NOT_FOUND
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ServerError> handleGlobalException(Exception ex, WebRequest request) {
        var error = new ServerError(
                "An unexpected error occurred",
                UUID.randomUUID().toString()
        );
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }


}


