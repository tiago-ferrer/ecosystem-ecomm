package br.com.fiap.postech.adjt.cart.exception;

public class InvalidUUIDException extends RuntimeException {

    public InvalidUUIDException() {
        super("Invalid consumerId format");
    }

}
