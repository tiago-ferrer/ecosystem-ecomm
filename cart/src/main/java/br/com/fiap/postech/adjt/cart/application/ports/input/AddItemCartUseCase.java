package br.com.fiap.postech.adjt.cart.application.ports.input;

import br.com.fiap.postech.adjt.cart.domain.model.ItemCart;

public interface AddItemCartUseCase {
   
    ItemCart createProduct(ItemCart itemCart);
    
}
