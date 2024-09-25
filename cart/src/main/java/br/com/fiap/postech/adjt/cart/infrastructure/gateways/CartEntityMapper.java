package br.com.fiap.postech.adjt.cart.infrastructure.gateways;

import java.util.List;
import java.util.stream.Collectors;

import br.com.fiap.postech.adjt.cart.domain.entity.Cart;
import br.com.fiap.postech.adjt.cart.domain.entity.ItemCart;
import br.com.fiap.postech.adjt.cart.infrastructure.persistence.CartEntity;
import br.com.fiap.postech.adjt.cart.infrastructure.persistence.ItemCartEntity;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CartEntityMapper {

	private final ItemsCartEntityMapper itemsCartEntityMapper;

	CartEntity toEntity(Cart cartDomainObj) {
		List<ItemCartEntity> itemsCartEntity = cartDomainObj.itemsCart().stream().map(itemsCartEntityMapper::toEntity).collect(Collectors.toList());
		return new CartEntity(cartDomainObj.consumerId(), itemsCartEntity);
	}

	Cart toDomainObj(CartEntity userEntity) {
		List<ItemCart> itemsCart = userEntity.getItens().stream().map(itemsCartEntityMapper::toDomainObj).collect(Collectors.toList());
		
		return new Cart(userEntity.getConsumerId(), itemsCart);
	}

}
