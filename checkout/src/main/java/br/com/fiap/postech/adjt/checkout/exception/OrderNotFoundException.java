package br.com.fiap.postech.adjt.checkout.exception;

public class OrderNotFoundException extends RuntimeException {
    public OrderNotFoundException(String s) {
        super(s);
    }
}
