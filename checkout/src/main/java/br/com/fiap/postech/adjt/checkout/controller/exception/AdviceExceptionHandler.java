package br.com.fiap.postech.adjt.checkout.controller.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@ControllerAdvice
public class AdviceExceptionHandler {

    @ExceptionHandler({IllegalArgumentException.class})
    public ResponseEntity<StandardError> handleIllegalArgumentException(
            IllegalArgumentException e,
            HttpServletRequest request
    ) {
        HttpStatus status = HttpStatus.BAD_REQUEST;

        final StandardError err = StandardError.create(status, e.getMessage(), request.getRequestURI());

        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler({UnsupportedOperationException.class})
    public ResponseEntity<StandardError> handleUnsupportedOperationException(
            UnsupportedOperationException e,
            HttpServletRequest request
    ) {
        HttpStatus status = HttpStatus.BAD_REQUEST;

        final StandardError err = StandardError.create(status, e.getMessage(), request.getRequestURI());

        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler({NotFoundException.class, NoResourceFoundException.class})
    public ResponseEntity<StandardError> handleNotFoundException(NotFoundException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.NOT_FOUND;

        final StandardError err = StandardError.create(status, e.getMessage(), request.getRequestURI());

        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler({NotAuthorizedException.class})
    public ResponseEntity<StandardError> handleNotAuthorizedException(
            NotAuthorizedException e,
            HttpServletRequest request
    ) {
        HttpStatus status = HttpStatus.FORBIDDEN;

        final StandardError err = StandardError.create(status, e.getMessage(), request.getRequestURI());

        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler({StandardErrorException.class})
    public ResponseEntity<StandardError> handleStandardErrorException(
            StandardErrorException e,
            HttpServletRequest request
    ) {
        StandardError err = e.getStandardError();
        return ResponseEntity.status(err.status()).body(err);
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<StandardError> handleGenericException(Exception e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

        final StandardError err = StandardError.create(status, e.getMessage(), request.getRequestURI());

        return ResponseEntity.status(status).body(err);
    }
}
