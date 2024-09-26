package br.com.fiap.postech.adjt.checkout.exception;

public class InvalidOrderUuidFormatException extends RuntimeException {
    public InvalidOrderUuidFormatException() {
        super("Invalid orderId format");
    }
}
