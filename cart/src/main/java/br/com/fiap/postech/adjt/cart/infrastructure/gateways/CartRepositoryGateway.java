package br.com.fiap.postech.adjt.cart.infrastructure.gateways;

import java.util.UUID;

import br.com.fiap.postech.adjt.cart.domain.entity.Cart;
import br.com.fiap.postech.adjt.cart.gateways.CartGateway;
import br.com.fiap.postech.adjt.cart.infrastructure.persistence.CartRepository;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CartRepositoryGateway implements CartGateway {
	private final CartRepository repository;
	private final CartEntityMapper entityMapper;

	@Override
	public Cart addItem(Cart cart) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteItem(Cart cart) {
		// TODO Auto-generated method stub

	}

	@Override
	public Cart updateItem(Cart cart) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void clearCart(UUID consumerId) {
		// TODO Auto-generated method stub

	}

}
