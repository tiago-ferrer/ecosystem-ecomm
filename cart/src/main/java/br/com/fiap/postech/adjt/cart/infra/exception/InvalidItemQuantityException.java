package br.com.fiap.postech.adjt.cart.infra.exception;

public class InvalidItemQuantityException extends RuntimeException{

    public InvalidItemQuantityException() {
        super("Invalid itemId quantity");
    }

}
