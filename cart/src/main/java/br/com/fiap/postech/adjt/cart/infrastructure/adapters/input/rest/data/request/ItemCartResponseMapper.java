package br.com.fiap.postech.adjt.cart.infrastructure.adapters.input.rest.data.request;

import java.util.Arrays;
import java.util.UUID;

import br.com.fiap.postech.adjt.cart.domain.model.Cart;
import br.com.fiap.postech.adjt.cart.domain.model.ItemCart;

public class ItemCartResponseMapper {

	public static Cart map(ItemCartRequest itemCartRequest) {
		
		ItemCart itemCart = new ItemCart(itemCartRequest.getItemId(), itemCartRequest.getQuantity());
		
		Cart cart = Cart.builder()
			.consumerId(itemCartRequest.getConsumerId())
			.itemsCart(Arrays.asList(itemCart))
			.build();
		
		return cart;
	}

}
