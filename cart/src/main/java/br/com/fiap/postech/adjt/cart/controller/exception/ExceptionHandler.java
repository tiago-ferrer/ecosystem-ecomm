package br.com.fiap.postech.adjt.cart.controller.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.UUID;

public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<String> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        if (e.getRequiredType() == UUID.class) {
            return ResponseEntity
                    .badRequest()
                    .body("Invalid consumerId format");
        }
        return ResponseEntity
                .badRequest()
                .body("Invalid input format");
    }

}
