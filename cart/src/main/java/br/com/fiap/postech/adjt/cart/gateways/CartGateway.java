package br.com.fiap.postech.adjt.cart.gateways;

import java.util.UUID;

import br.com.fiap.postech.adjt.cart.domain.entity.Cart;

public interface CartGateway {
	
	Cart addItem(Cart cart);
	
	void deleteItem(Cart cart);
	
	Cart updateItem(Cart cart);
	
	void clearCart(UUID consumerId);

}
