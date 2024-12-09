package br.com.fiap.postech.adjt.cart.exception;

public class EmptyCartException extends RuntimeException {

    public EmptyCartException() {
        super("Empty cart");
    }

}
