package br.com.fiap.postech.adjt.checkout.exception;

public class CartConsumerException extends RuntimeException {
    public CartConsumerException(String invalidConsumer) {
        super(invalidConsumer);
    }
}
