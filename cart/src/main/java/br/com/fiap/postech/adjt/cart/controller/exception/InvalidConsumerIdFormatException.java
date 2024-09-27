package br.com.fiap.postech.adjt.cart.controller.exception;

public class InvalidConsumerIdFormatException extends RuntimeException {
    public InvalidConsumerIdFormatException() {
        super("Invalid consumerId format");
    }
}
