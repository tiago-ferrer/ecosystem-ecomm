package br.com.fiap.postech.adjt.checkout.exception;

public class CartNotFoundException extends RuntimeException {
    public CartNotFoundException(String cartNotFound) {
        super(cartNotFound);
    }
}
