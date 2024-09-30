package br.com.fiap.postech.adjt.cart.infrastructure.adapters.output.persistence;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import br.com.fiap.postech.adjt.cart.application.ports.output.CartOutputPort;
import br.com.fiap.postech.adjt.cart.domain.exception.CartNotFoundException;
import br.com.fiap.postech.adjt.cart.domain.model.Cart;
import br.com.fiap.postech.adjt.cart.infrastructure.adapters.output.persistence.entity.CartEntity;
import br.com.fiap.postech.adjt.cart.infrastructure.adapters.output.persistence.entity.ItemCartEntity;
import br.com.fiap.postech.adjt.cart.infrastructure.adapters.output.persistence.gateway.ProductDTO;
import br.com.fiap.postech.adjt.cart.infrastructure.adapters.output.persistence.gateway.ProdutoServiceClient;
import br.com.fiap.postech.adjt.cart.infrastructure.adapters.output.persistence.mapper.CartMapper;
import br.com.fiap.postech.adjt.cart.infrastructure.adapters.output.persistence.mapper.ItemCartMapper;
import br.com.fiap.postech.adjt.cart.infrastructure.adapters.output.persistence.repository.CartRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CartPersistenceAdapter implements CartOutputPort {

	private final CartRepository cartRepository;

	private final CartMapper cartMapper;

	private final ItemCartMapper itemCartMapper;
	
	private final ProdutoServiceClient produtoServiceClient; 

	@Override
	public Cart addItemCart(Cart cart) {

		UUID consumerId = UUID.fromString(cart.getConsumerId());

		CartEntity cartEntity = cartRepository.findByConsumerId(consumerId)
				.orElse(new CartEntity(null, consumerId, new ArrayList<>()));

		Optional<ItemCartEntity> existingCartItem = cartEntity.getItemsCart().stream()
				.filter(item -> item.getItemId().equals(cart.getItemsCart().get(0).getItemId())).findFirst();
		
		long itemId = Long.parseLong(cart.getItemsCart().get(0).getItemId());
		
		ResponseEntity<ProductDTO> product = produtoServiceClient.getProductById(itemId);
		
		if (!product.getStatusCode().equals(HttpStatus.OK)) {
			throw new CartNotFoundException("Invalid itemId does not exist");
		}

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
	public Optional<Cart> getCartByConsumerId(String consumerId) {

		UUID uuid = UUID.fromString(consumerId);

		Optional<CartEntity> existingCart = cartRepository.findByConsumerId(uuid);

		if (existingCart.isPresent()) {
			Cart cart = cartMapper.toCart(existingCart.get());
			return Optional.of(cart);
		}

		return Optional.empty();

	}

	@Override
	public Optional<Cart> removeItemCart(Cart cart) {

		UUID uuid = UUID.fromString(cart.getConsumerId());
		Optional<CartEntity> existingCart = cartRepository.findByConsumerId(uuid);

		if (existingCart.isPresent()) {

			List<ItemCartEntity> itemsCart = new ArrayList<ItemCartEntity>();

			CartEntity cartEntity = existingCart.get();

			for (ItemCartEntity item : cartEntity.getItemsCart()) {
				if (item.getItemId().equals(cart.getItemsCart().get(0).getItemId())) {
					item.setQuantity(item.getQuantity() - 1);
				}

				if (item.getQuantity() > 1) {
					itemsCart.add(item);
				}

			}

			cartEntity.setItemsCart(itemsCart);

			CartEntity savedEntity = cartRepository.save(cartEntity);
			return Optional.of(cartMapper.toCart(savedEntity));

		}

		return Optional.empty();
	}

	@Override
	public Optional<Cart> updateItemCartUseCase(Cart cart) {
		
		UUID uuid = UUID.fromString(cart.getConsumerId());
		Optional<CartEntity> existingCart = cartRepository.findByConsumerId(uuid);

		if (existingCart.isPresent()) {

			List<ItemCartEntity> itemsCart = new ArrayList<ItemCartEntity>();

			CartEntity cartEntity = existingCart.get();
			
			// Item cart not exist throw exception?
			cartEntity.getItemsCart().stream()
			.filter(item -> item.getItemId().equals(cart.getItemsCart().get(0).getItemId())).findFirst()
			.orElseThrow(() -> new CartNotFoundException("Invalid itemId"));
			
			for (ItemCartEntity item : cartEntity.getItemsCart()) {
				if (item.getItemId().equals(cart.getItemsCart().get(0).getItemId())) {
					item.setQuantity(item.getQuantity() +1);
				}
				
				itemsCart.add(item);
			}

			cartEntity.setItemsCart(itemsCart);

			CartEntity savedEntity = cartRepository.save(cartEntity);
			return Optional.of(cartMapper.toCart(savedEntity));

		}

		return Optional.empty();
	}

	@Override
	public Boolean clearItemCart(String consumerId) {
		
		UUID uuid = UUID.fromString(consumerId);
		Optional<CartEntity> existingCart = cartRepository.findByConsumerId(uuid);

		if (existingCart.isPresent()) {
			cartRepository.delete(existingCart.get());
			return true;
		}
			
		return false;
	}

}
