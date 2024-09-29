package br.com.fiap.postech.adjt.checkout.exception;

public class EmptyCartException extends RuntimeException {
    public EmptyCartException() {
        super("Empty cart");
    }
}
