package br.com.fiap.postech.adjt.cart.usescases;

import java.util.UUID;

import br.com.fiap.postech.adjt.cart.domain.entity.Cart;
import br.com.fiap.postech.adjt.cart.gateways.CartGateway;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CartInteractor {

	private final CartGateway gateway;

	public Cart addItem(Cart cart) {
		return gateway.addItem(cart);
	}

	void deleteItem(Cart cart) {
		gateway.deleteItem(cart);
	}

	Cart updateItem(Cart cart) {
		gateway.updateItem(cart);
		return null;
	}

	void clearCart(UUID consumerId) {
		gateway.clearCart(consumerId);
	}

}
