package br.com.fiap.postech.adjt.gateway.product;

public class ProductNotFoundException extends RuntimeException {

    public ProductNotFoundException() {
        super("Id não localizado");
    }

}
