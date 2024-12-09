package br.com.fiap.postech.adjt.checkout.exception;

public class InvalidConsumerId extends RuntimeException {
    public InvalidConsumerId() {
        super("Invalid consumerId format");
    }
}
