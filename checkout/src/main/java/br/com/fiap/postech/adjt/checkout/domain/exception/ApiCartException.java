package br.com.fiap.postech.adjt.checkout.domain.exception;

public class ApiCartException extends RuntimeException {
    public ApiCartException(String message) {
        super(message);
    }
}
