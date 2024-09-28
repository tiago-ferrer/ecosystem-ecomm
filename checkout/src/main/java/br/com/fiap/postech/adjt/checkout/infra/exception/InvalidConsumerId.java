package br.com.fiap.postech.adjt.checkout.infra.exception;

public class InvalidConsumerId extends RuntimeException {
    public InvalidConsumerId() {
        super("Invalid consumerId format");
    }
}
