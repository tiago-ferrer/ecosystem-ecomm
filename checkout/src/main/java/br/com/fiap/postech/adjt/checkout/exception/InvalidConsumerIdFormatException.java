package br.com.fiap.postech.adjt.checkout.exception;

public class InvalidConsumerIdFormatException extends RuntimeException {
    public InvalidConsumerIdFormatException() {
        super("Invalid consumerId format");
    }
}
