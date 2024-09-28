package br.com.fiap.postech.adjt.cart.infra.exception;

public class EmptyCartException extends RuntimeException {

    public EmptyCartException() {
        super("Empty cart");
    }

}
