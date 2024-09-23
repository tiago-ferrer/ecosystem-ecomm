package br.com.fiap.postech.adjt.cart.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidConsumerIdException.class)
    public ResponseEntity<String> handleInvalidConsumerIdException(InvalidConsumerIdException e) {
        return ResponseEntity.badRequest().body("Invalid consumerId format");
    }

    @ExceptionHandler(InvalidItemIdException.class)
    public ResponseEntity<String> handleInvalidItemIdException(InvalidItemIdException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(InvalidItemQuantityException.class)
    public ResponseEntity<String> handleInvalidItemQuantityException(InvalidItemQuantityException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGlobalException(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("An unexpected error occurred: " + e.getMessage());
    }

    @ExceptionHandler(EmptyCartException.class)
    public ResponseEntity<String> handleEmptyCartException(Exception e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
