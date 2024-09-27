package br.com.fiap.postech.adjt.checkout.config;

import br.com.fiap.postech.adjt.checkout.exception.CartConsumerException;
import br.com.fiap.postech.adjt.checkout.exception.CartNotFoundException;
import br.com.fiap.postech.adjt.checkout.exception.CustonErrorResponse;
import br.com.fiap.postech.adjt.checkout.exception.InvalidOrderIdException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.Optional;

@RestControllerAdvice
@RequiredArgsConstructor
public class CustomExceptionHandler {

    private final MessageSource messageSource;

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CustonErrorResponse> cartNotFound(MethodArgumentNotValidException e) {
        String message = Optional.ofNullable(e)
                .map(MethodArgumentNotValidException::getBindingResult)
                .map(BindingResult::getFieldErrors)
                .map(List::getFirst)
                .map(error -> messageSource.getMessage(error, LocaleContextHolder.getLocale()))
                .orElse(null);
        return ResponseEntity.badRequest().body(new CustonErrorResponse(message));
    }

    @ExceptionHandler(InvalidOrderIdException.class)
    public ResponseEntity<CustonErrorResponse> InvalidOrderId(InvalidOrderIdException e){
        CustonErrorResponse errorResponse = new CustonErrorResponse("Invalid orderId format");
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CartConsumerException.class)
    public ResponseEntity<CustonErrorResponse> CartConsumer(CartConsumerException e){
        CustonErrorResponse errorResponse = new CustonErrorResponse("Invalid consumerId format");
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CartNotFoundException.class)
    public ResponseEntity<CustonErrorResponse> CartConsumer(CartNotFoundException e){
        CustonErrorResponse errorResponse = new CustonErrorResponse("Empty cart");
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}
