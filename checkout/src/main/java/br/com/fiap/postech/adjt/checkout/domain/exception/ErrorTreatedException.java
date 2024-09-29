package br.com.fiap.postech.adjt.checkout.domain.exception;

public class ErrorTreatedException extends RuntimeException {
    public ErrorTreatedException(String message) {
        super(message);
    }
}
