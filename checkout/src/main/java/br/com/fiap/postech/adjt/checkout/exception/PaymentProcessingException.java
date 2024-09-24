package br.com.fiap.postech.adjt.checkout.exception;

public class PaymentProcessingException extends RuntimeException {
    public PaymentProcessingException() {
        super("Payment processing failed");
    }
}
