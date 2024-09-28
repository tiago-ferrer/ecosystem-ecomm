package br.com.fiap.postech.adjt.cart.domain.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ApiError {
    private Integer statusCode;
    private String message;
}
