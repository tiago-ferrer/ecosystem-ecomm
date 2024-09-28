package br.com.fiap.postech.adjt.cart.domain.exception;

public class CartNotFoundException extends RuntimeException{

    public CartNotFoundException() {
    }

    public CartNotFoundException(String message) {
        super(message);
    }
}
