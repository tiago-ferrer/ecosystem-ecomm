package br.com.fiap.postech.adjt.cart.infrastructure.controller.dtos;

import java.util.Arrays;
import java.util.List;

import br.com.fiap.postech.adjt.cart.domain.entity.Cart;
import br.com.fiap.postech.adjt.cart.domain.entity.ItemCart;

public class CartItemMapper {
	public CartGetItemsResponseDto toResponse(Cart cart) {
		return new CartGetItemsResponseDto(cart.itemsCart());
	}

	public Cart toItemCart(CreateItemsDto request) {

		List<ItemCart> itens = Arrays.asList(new ItemCart(request.itemId(), request.quantity()));

		return new Cart(request.consumerId(), itens);
	}
}
