package br.com.fiap.postech.adjt.cart.infrastructure.controller;

import java.util.Arrays;
import java.util.List;

import br.com.fiap.postech.adjt.cart.domain.entity.Cart;
import br.com.fiap.postech.adjt.cart.domain.entity.ItemCart;

public class CartItemDTOMapper {
	CartItemsResponse toResponse(Cart cart) {
		return new CartItemsResponse(cart.itemsCart());
	}

	public Cart toItemCart(CartItemsRequest request) {

		List<ItemCart> itens = Arrays.asList(new ItemCart(request.itemId(), request.quantity()));

		return new Cart(request.consumerId(), itens);
	}
}
