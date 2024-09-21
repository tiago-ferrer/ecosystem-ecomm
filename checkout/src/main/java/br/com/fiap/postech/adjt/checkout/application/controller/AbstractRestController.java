package br.com.fiap.postech.adjt.checkout.application.controller;

import br.com.fiap.postech.adjt.checkout.application.dto.ErrorDTO;
import br.com.fiap.postech.adjt.checkout.domain.exception.AppException;
import jakarta.persistence.MappedSuperclass;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@MappedSuperclass
public class AbstractRestController {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(AppException.class)
    public ErrorDTO handleValidationExceptions(AppException ex) {
        return new ErrorDTO(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
    }

}
