package br.com.fiap.postech.adjt.cart.domain.service;

import br.com.fiap.postech.adjt.cart.application.ports.input.ClearCartUseCase;
import br.com.fiap.postech.adjt.cart.application.ports.input.GetItemsCartByCustomerIdUseCase;
import br.com.fiap.postech.adjt.cart.application.ports.input.RemoveItemCartUseCase;
import br.com.fiap.postech.adjt.cart.application.ports.input.SaveItemCartUseCase;
import br.com.fiap.postech.adjt.cart.application.ports.input.UpdateItemCartUseCase;
import br.com.fiap.postech.adjt.cart.application.ports.output.CartOutputPort;
import br.com.fiap.postech.adjt.cart.domain.exception.CartNotFoundException;
import br.com.fiap.postech.adjt.cart.domain.model.Cart;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CartService
		implements SaveItemCartUseCase, GetItemsCartByCustomerIdUseCase, RemoveItemCartUseCase, UpdateItemCartUseCase, ClearCartUseCase {

	private final CartOutputPort cartOutputPort;

	@Override
	public Cart getItemsCartByCustomerId(String itemId) {
		return cartOutputPort.getCartByConsumerId(itemId)
				.orElseThrow(() -> new CartNotFoundException("Empty cart"));
	}

	@Override
	public Cart createItemCart(Cart cart) {
		return cartOutputPort.addItemCart(cart);
	}

	@Override
	public Cart removeItemCart(Cart cart) {
		return cartOutputPort.removeItemCart(cart).orElseThrow(() -> new CartNotFoundException("Invalid itemId"));
	}

	@Override
	public Cart updateItemCartUseCase(Cart cart) {
		return cartOutputPort.updateItemCartUseCase(cart)
				.orElseThrow(() -> new CartNotFoundException("Invalid itemId"));
	}

	@Override
	public Boolean clearCartByCustomerId(String customerId) {
		return cartOutputPort.clearItemCart(customerId);
	}

}
