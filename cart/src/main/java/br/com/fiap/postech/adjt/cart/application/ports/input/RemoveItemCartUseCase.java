package br.com.fiap.postech.adjt.cart.application.ports.input;

import br.com.fiap.postech.adjt.cart.domain.model.Cart;

public interface RemoveItemCartUseCase {
   
	Cart removeItemCart(Cart cart);
    
}
