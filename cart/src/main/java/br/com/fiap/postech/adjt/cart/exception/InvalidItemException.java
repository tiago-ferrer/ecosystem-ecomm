package br.com.fiap.postech.adjt.cart.exception;

public class InvalidItemException extends RuntimeException {

    public InvalidItemException() {
        super("Invalid itemId does not exist");
    }

}
