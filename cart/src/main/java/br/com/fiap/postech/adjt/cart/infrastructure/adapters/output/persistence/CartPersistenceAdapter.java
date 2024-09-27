package br.com.fiap.postech.adjt.cart.infrastructure.adapters.output.persistence;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import br.com.fiap.postech.adjt.cart.application.ports.output.CartOutputPort;
import br.com.fiap.postech.adjt.cart.domain.model.Cart;
import br.com.fiap.postech.adjt.cart.domain.model.ItemCart;
import br.com.fiap.postech.adjt.cart.infrastructure.adapters.output.persistence.entity.CartEntity;
import br.com.fiap.postech.adjt.cart.infrastructure.adapters.output.persistence.entity.ItemCartEntity;
import br.com.fiap.postech.adjt.cart.infrastructure.adapters.output.persistence.mapper.CartMapper;
import br.com.fiap.postech.adjt.cart.infrastructure.adapters.output.persistence.mapper.ItemCartMapper;
import br.com.fiap.postech.adjt.cart.infrastructure.adapters.output.persistence.repository.CartRepository;
import br.com.fiap.postech.adjt.cart.infrastructure.adapters.output.persistence.repository.ItemCartRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CartPersistenceAdapter implements CartOutputPort {

    private final CartRepository cartRepository;

    private final ItemCartRepository itemCartRepository;

    private final CartMapper cartMapper;

    private final ItemCartMapper itemCartMapper;
    
    @Override
    public ItemCart saveProduct(ItemCart itemCart) {
    	
    	CartEntity cartEntity = null;
    	
    	Optional<CartEntity> optionalCartSaved = cartRepository.findByConsumerId(itemCart.getConsumerId());

    	ItemCartEntity itemCartEntity = itemCartMapper.toEntity(itemCart);
    	
    	if (optionalCartSaved.isPresent()) {
    		cartEntity = optionalCartSaved.get();
    	} else {
    		
    		Cart cart = new Cart(itemCart.getConsumerId(), Arrays.asList(itemCart));
    		
    		CartEntity entity = cartMapper.toEntity(cart);
    		cartEntity = cartRepository.save(entity);
    	}
    	
        itemCartRepository.save(itemCartEntity);
        
        return itemCartMapper.toItemCart(itemCartEntity) ;
    }

    @Override
    public Optional<ItemCart> getProductById(UUID id) {
        Optional<ItemCartEntity> productEntity = itemCartRepository.findById(id);

        if(productEntity.isEmpty()) {
            return Optional.empty();
        }

        ItemCart product = itemCartMapper.toItemCart(productEntity.get());
        return Optional.of(product);
    }
    
}
