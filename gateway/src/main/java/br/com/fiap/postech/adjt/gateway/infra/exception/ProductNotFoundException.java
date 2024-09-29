package br.com.fiap.postech.adjt.gateway.infra.exception;

public class ProductNotFoundException extends RuntimeException {

    public ProductNotFoundException() {
        super("Id não localizado");
    }

}
