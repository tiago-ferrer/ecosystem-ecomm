package br.com.fiap.postech.adjt.cart.service;

import br.com.fiap.postech.adjt.cart.dto.AddOrRemoveItemRequest;
import br.com.fiap.postech.adjt.cart.dto.CartResponse;
import br.com.fiap.postech.adjt.cart.dto.ItemRequest;

public interface CartService {

    void createCartItem(ItemRequest request);

    void addItemToCart(AddOrRemoveItemRequest request);

    void removeItemFromCart(AddOrRemoveItemRequest request);

    CartResponse getCart(String consumerId);

    void deleteAllItens(String consumerId);
}
