package br.com.fiap.postech.adjt.cart.infra.exception;

public class InvalidUUIDException extends RuntimeException {

    public InvalidUUIDException() {
        super("Invalid consumerId format");
    }

}
