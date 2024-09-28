package br.com.fiap.postech.adjt.cart.application.ports.input;

public interface ClearCartUseCase {
   
    Boolean clearCartByCustomerId(String customerId);
    
}
