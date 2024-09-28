package br.com.fiap.postech.adjt.cart.infrastructure.adapters.input.rest.data.request;

import java.util.Arrays;

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
	
	
	public static Cart mapRemoveItemCartUseCaseRequest(RemoveItemCartUseCaseRequest removeItemCartUseCaseRequest) {
		
		ItemCart itemCart = new ItemCart(removeItemCartUseCaseRequest.getItemId(), 1);
		
		Cart cart = Cart.builder()
			.consumerId(removeItemCartUseCaseRequest.getConsumerId())
			.itemsCart(Arrays.asList(itemCart))
			.build();
		
		return cart;
	}
	
	public static Cart mapUpdateItemCartUseCaseRequest(UpdateItemCartUseCaseRequest updateItemCartUseCaseRequest) {
		
		ItemCart itemCart = new ItemCart(updateItemCartUseCaseRequest.getItemId(), 1);
		
		Cart cart = Cart.builder()
			.consumerId(updateItemCartUseCaseRequest.getConsumerId())
			.itemsCart(Arrays.asList(itemCart))
			.build();
		
		return cart;
	}

}
