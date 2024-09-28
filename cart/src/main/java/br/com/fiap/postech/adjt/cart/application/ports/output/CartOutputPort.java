package br.com.fiap.postech.adjt.cart.application.ports.output;

import java.util.Optional;
import java.util.UUID;

import br.com.fiap.postech.adjt.cart.domain.model.Cart;

public interface CartOutputPort {
    
    Cart addItemCart(Cart cart);
    
    Optional<Cart> getCartByConsumerId(UUID consumerId);
    
}
