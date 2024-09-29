package br.com.fiap.postech.adjt.cart.config;

import br.com.fiap.postech.adjt.cart.dto.ErrorRecord;
import br.com.fiap.postech.adjt.cart.exceptions.CartException;
import lombok.AllArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.Optional;


@RestControllerAdvice
@AllArgsConstructor
public class ErrorHandlerConfig {

    private final MessageSource messageSource;

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorRecord> cartNotFound(MethodArgumentNotValidException e) {
        String message = Optional.ofNullable(e)
                .map(MethodArgumentNotValidException::getBindingResult)
                .map(BindingResult::getFieldErrors)
                .map(List::getFirst)
                .map(error -> messageSource.getMessage(error, LocaleContextHolder.getLocale()))
                .orElse(null);
        return ResponseEntity.badRequest().body(new ErrorRecord(message));
    }

    @ExceptionHandler(CartException.class)
    public ResponseEntity<ErrorRecord> cartError(CartException e) {
        return ResponseEntity.badRequest().body(new ErrorRecord(e.getMessage()));
    }
    @ExceptionHandler(HttpMessageConversionException.class)
    public ResponseEntity<ErrorRecord> cartError(HttpMessageConversionException e) {
        return ResponseEntity.badRequest().body(new ErrorRecord(e.getMessage()));
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorRecord> cartError(Exception e) {
        return ResponseEntity.internalServerError().build();
    }

}
