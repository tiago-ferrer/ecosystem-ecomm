package br.com.fiap.postech.adjt.checkout.exception;

public class InvalidOrderIdException extends RuntimeException{
    public InvalidOrderIdException(String message) {
        super(message);
    }
}
