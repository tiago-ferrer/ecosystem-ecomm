package br.com.fiap.postech.adjt.cart.application.ports.output;

import java.util.Optional;

import br.com.fiap.postech.adjt.cart.domain.model.Cart;

public interface CartOutputPort {
    
    Cart addItemCart(Cart cart);
    
    Optional<Cart> getCartByConsumerId(String consumerId);
    
    Optional<Cart> removeItemCart(Cart cart);
    
    Optional<Cart> updateItemCartUseCase(Cart cart);

    Boolean clearItemCart(String consumerId);
    
}
