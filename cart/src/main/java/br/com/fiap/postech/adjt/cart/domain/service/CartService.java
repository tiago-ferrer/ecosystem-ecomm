package br.com.fiap.postech.adjt.cart.domain.service;

import br.com.fiap.postech.adjt.cart.application.ports.input.SaveItemCartUseCase;

import java.util.UUID;

import br.com.fiap.postech.adjt.cart.application.ports.input.GetItemsCartUseCase;
import br.com.fiap.postech.adjt.cart.application.ports.output.CartOutputPort;
import br.com.fiap.postech.adjt.cart.domain.exception.CartNotFoundException;
import br.com.fiap.postech.adjt.cart.domain.model.ItemCart;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CartService implements SaveItemCartUseCase, GetItemsCartUseCase {

    private final CartOutputPort cartOutputPort;
    
    @Override
    public ItemCart getItemCartByItemId(UUID itemId) {
        System.out.println("Retornando Producto por ID");
        return cartOutputPort.getProductById(itemId)
                                .orElseThrow(() -> new CartNotFoundException("No se encontro el producto con ID: " + itemId));
    }

    @Override
    public ItemCart createItemCart(ItemCart itemCart) {
        System.out.println("save ItemCart");
        return cartOutputPort.saveProduct(itemCart);
    }
    
}
