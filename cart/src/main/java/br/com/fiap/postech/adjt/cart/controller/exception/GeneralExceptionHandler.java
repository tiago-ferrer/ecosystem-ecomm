package br.com.fiap.postech.adjt.cart.controller.exception;

import br.com.fiap.postech.adjt.cart.model.dto.response.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.UUID;

@ControllerAdvice
public class GeneralExceptionHandler {

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
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

    @ExceptionHandler({
            IllegalArgumentException.class,
            InvalidConsumerIdFormatException.class,
            NotFoundException.class
    })
    public ResponseEntity<ErrorResponse> handleBadRequestException(RuntimeException e) {
        ErrorResponse errorResponse = new ErrorResponse(e.getMessage());
        return ResponseEntity.badRequest().body(errorResponse);
    }
}
