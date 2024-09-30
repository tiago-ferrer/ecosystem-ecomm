package br.com.fiap.postech.adjt.cart.infrastructure.adapters.output.persistence.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.fiap.postech.adjt.cart.domain.model.ItemCart;
import br.com.fiap.postech.adjt.cart.infrastructure.adapters.output.persistence.entity.ItemCartEntity;

public class ItemCartMapper {

	@Autowired
	private ModelMapper mapper;

	public ItemCart toItemCart(ItemCartEntity entity) {
		return mapper.map(entity, ItemCart.class);
	}

	public ItemCartEntity toEntity(ItemCart itemCart) {
		return mapper.map(itemCart, ItemCartEntity.class);
	}

}
