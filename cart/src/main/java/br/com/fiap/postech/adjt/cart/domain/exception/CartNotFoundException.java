package br.com.fiap.postech.adjt.cart.domain.exception;

public class CartNotFoundException extends RuntimeException{
    
    public CartNotFoundException(String msg){
        super(msg);
    }
}
