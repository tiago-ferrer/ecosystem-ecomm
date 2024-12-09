package br.com.fiap.postech.adjt.cart.exception;

public class InvalidItemQuantityException extends RuntimeException{

    public InvalidItemQuantityException() {
        super("Invalid itemId quantity");
    }

}
