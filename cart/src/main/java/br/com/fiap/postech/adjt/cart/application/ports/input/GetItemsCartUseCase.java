package br.com.fiap.postech.adjt.cart.application.ports.input;

import java.util.UUID;

import br.com.fiap.postech.adjt.cart.domain.model.Cart;

public interface GetItemsCartUseCase {
   
    Cart getItemCartByItemId(UUID itemId);
    
}
