package br.com.fiap.postech.adjt.cart.application.ports.input;

import java.util.UUID;

import br.com.fiap.postech.adjt.cart.domain.model.ItemCart;

public interface GetItemsCartUseCase {
   
    ItemCart getItemCartByItemId(UUID itemId);
    
}
