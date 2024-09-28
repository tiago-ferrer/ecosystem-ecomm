package br.com.fiap.postech.adjt.cart.infrastructure.adapters.output.persistence;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import br.com.fiap.postech.adjt.cart.application.ports.output.CartOutputPort;
import br.com.fiap.postech.adjt.cart.domain.exception.CartNotFoundException;
import br.com.fiap.postech.adjt.cart.domain.model.Cart;
import br.com.fiap.postech.adjt.cart.infrastructure.adapters.output.persistence.entity.CartEntity;
import br.com.fiap.postech.adjt.cart.infrastructure.adapters.output.persistence.entity.ItemCartEntity;
import br.com.fiap.postech.adjt.cart.infrastructure.adapters.output.persistence.mapper.CartMapper;
import br.com.fiap.postech.adjt.cart.infrastructure.adapters.output.persistence.mapper.ItemCartMapper;
import br.com.fiap.postech.adjt.cart.infrastructure.adapters.output.persistence.repository.CartRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CartPersistenceAdapter implements CartOutputPort {

	private final CartRepository cartRepository;

	private final CartMapper cartMapper;

	private final ItemCartMapper itemCartMapper;

	@Override
	public Cart addItemCart(Cart cart) {

		if (!isUUIDValid(cart.getConsumerId())) {
			throw new CartNotFoundException("Invalid consumerId format");
		}

		UUID consumerId = UUID.fromString(cart.getConsumerId());
		
		CartEntity cartEntity = cartRepository.findByConsumerId(consumerId)
				.orElse(new CartEntity(null, consumerId, new ArrayList<>()));

		Optional<ItemCartEntity> existingCartItem = cartEntity.getItemsCart().stream()
				.filter(item -> item.getItemId().equals(cart.getItemsCart().get(0).getItemId())).findFirst();

		if (existingCartItem.isPresent()) {
			return cartMapper.toCart(cartEntity);
		} else {

			List<ItemCartEntity> itemsCartEntity = cart.getItemsCart().stream().map(itemCartMapper::toEntity)
					.collect(Collectors.toList());

			cartEntity.getItemsCart().addAll(itemsCartEntity);

			CartEntity cartEntitySaved = cartRepository.save(cartEntity);

			return cartMapper.toCart(cartEntitySaved);
		}
	}

	@Override
	public Optional<Cart> getCartByConsumerId(UUID consumerId) {

		Optional<CartEntity> existingCart = cartRepository.findByConsumerId(consumerId);

		if (existingCart.isPresent()) {
			Cart cart = cartMapper.toCart(existingCart.get());
			return Optional.of(cart);
		}

		return Optional.empty();

	}

	private boolean isUUIDValid(String uuid) {
		Pattern pattern = Pattern
				.compile("^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$");
		
		return pattern.matcher(uuid).matches();
	}

}
