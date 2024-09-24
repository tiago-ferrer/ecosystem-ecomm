package br.com.fiap.postech.adjt.checkout.exception;

public class InvalidPaymentMethodException extends RuntimeException {
    public InvalidPaymentMethodException() {
        super("Invalid payment information");
    }
}
