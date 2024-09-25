package br.com.fiap.postech.adjt.cart.infrastructure.gateways;

import br.com.fiap.postech.adjt.cart.domain.entity.ItemCart;
import br.com.fiap.postech.adjt.cart.infrastructure.persistence.ItemCartEntity;

public class ItemsCartEntityMapper {
	ItemCartEntity toEntity(ItemCart itemCartDomainObj) {
		return new ItemCartEntity(itemCartDomainObj.itemId(), itemCartDomainObj.quantity());
	}

	ItemCart toDomainObj(ItemCartEntity itemCartEntity) {
		return new ItemCart(itemCartEntity.getItemId(), itemCartEntity.getQuantity());
	}

}
