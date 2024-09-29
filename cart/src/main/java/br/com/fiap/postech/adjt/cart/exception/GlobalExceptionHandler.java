package br.com.fiap.postech.adjt.cart.exception;

import br.com.fiap.postech.adjt.cart.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidConsumerIdException.class)
    public ResponseEntity<ErrorResponse> handleInvalidConsumerIdException(InvalidConsumerIdException e) {
        return ResponseEntity.badRequest().body(new ErrorResponse("Invalid consumerId format"));
    }

    @ExceptionHandler(InvalidItemIdException.class)
    public ResponseEntity<ErrorResponse> handleInvalidItemIdException(InvalidItemIdException e) {
        return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
    }

    @ExceptionHandler(InvalidItemQuantityException.class)
    public ResponseEntity<ErrorResponse> handleInvalidItemQuantityException(InvalidItemQuantityException e) {
        return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGlobalException(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("An unexpected error occurred: " + e.getMessage());
    }

    @ExceptionHandler(EmptyCartException.class)
    public ResponseEntity<ErrorResponse> handleEmptyCartException(Exception e) {
        return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
    }
}
