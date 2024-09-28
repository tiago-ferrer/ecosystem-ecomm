package br.com.fiap.postech.adjt.cart.domain.service;

import java.util.UUID;

import br.com.fiap.postech.adjt.cart.application.ports.input.GetItemsCartUseCase;
import br.com.fiap.postech.adjt.cart.application.ports.input.SaveItemCartUseCase;
import br.com.fiap.postech.adjt.cart.application.ports.output.CartOutputPort;
import br.com.fiap.postech.adjt.cart.domain.exception.CartNotFoundException;
import br.com.fiap.postech.adjt.cart.domain.model.Cart;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CartService implements SaveItemCartUseCase, GetItemsCartUseCase {

    private final CartOutputPort cartOutputPort;
    
    @Override
    public Cart getItemCartByItemId(UUID itemId) {
        System.out.println("Retornando item do carrinho por itemId " + itemId);
        
        cartOutputPort.getCartByConsumerId(itemId);
        
        return cartOutputPort.getCartByConsumerId(itemId)
        		.orElseThrow(() -> new CartNotFoundException( "error: Empty cart"));
    }

    @Override
    public Cart createItemCart(Cart cart) {
        System.out.println("save ItemCart");
        return cartOutputPort.addItemCart(cart);
    }
    
}
