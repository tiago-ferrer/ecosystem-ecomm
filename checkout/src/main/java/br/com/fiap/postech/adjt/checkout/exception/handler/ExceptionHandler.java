package br.com.fiap.postech.adjt.checkout.exception.handler;

import br.com.fiap.postech.adjt.checkout.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public class ExceptionHandler {


    @org.springframework.web.bind.annotation.ExceptionHandler(InvalidOrderUuidFormatException.class)
    public ResponseEntity<Object> handleInvalidOrderUuidException(InvalidOrderUuidFormatException ex) {
        return buildResponseEntity(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(InvalidPaymentMethodException.class)
    public ResponseEntity<Object> handleInvalidPaymentMethodException(InvalidPaymentMethodException ex) {
        return buildResponseEntity(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(PaymentProcessingException.class)
    public ResponseEntity<Object> handlePaymentProcessingException(PaymentProcessingException ex) {
        return buildResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(EmptyCartException.class)
    public ResponseEntity<Object> handleEmptyCartException(EmptyCartException ex) {
        return buildResponseEntity(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(InvalidConsumerIdFormatException.class)
    public ResponseEntity<Object> handleInvalidConsumerIdFormatException(InvalidConsumerIdFormatException ex) {
        return buildResponseEntity(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    private ResponseEntity<Object> buildResponseEntity(final HttpStatus httpStatus,
                                                       final String message) {
        final ApiError apiError =
                new ApiError(message);

        return ResponseEntity.status(httpStatus).body(apiError);
    }
}
