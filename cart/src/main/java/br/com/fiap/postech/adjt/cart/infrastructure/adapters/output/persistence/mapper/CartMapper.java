package br.com.fiap.postech.adjt.cart.infrastructure.adapters.output.persistence.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.fiap.postech.adjt.cart.domain.model.Cart;
import br.com.fiap.postech.adjt.cart.infrastructure.adapters.output.persistence.entity.CartEntity;

public class CartMapper {

	@Autowired
	private ModelMapper mapper;

	public Cart toCart(CartEntity entity) {
		return mapper.map(entity, Cart.class);
	}

	public CartEntity toEntity(Cart cartDomain) {
		return mapper.map(cartDomain, CartEntity.class);
	}

}
