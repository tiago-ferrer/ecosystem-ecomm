package br.com.fiap.postech.adjt.cart.infrastructure.adapters.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import br.com.fiap.postech.adjt.cart.domain.service.CartService;
import br.com.fiap.postech.adjt.cart.infrastructure.adapters.output.persistence.CartPersistenceAdapter;
import br.com.fiap.postech.adjt.cart.infrastructure.adapters.output.persistence.mapper.CartMapper;
import br.com.fiap.postech.adjt.cart.infrastructure.adapters.output.persistence.mapper.ItemCartMapper;
import br.com.fiap.postech.adjt.cart.infrastructure.adapters.output.persistence.repository.CartRepository;
import br.com.fiap.postech.adjt.cart.infrastructure.adapters.output.persistence.repository.ItemCartRepository;

/**
 * Configuracion BEANS
 */
@Configuration
public class BeanConfiguration {

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}

	@Bean
	public ItemCartMapper itemCartMapper() {
		return new ItemCartMapper();
	}

	@Bean
	public CartMapper cartMapper() {
		return new CartMapper();
	}
	
	@Bean
	public CartPersistenceAdapter cartPersistenceAdapter(CartRepository cartRepository,
			ItemCartRepository itemCartRepository, CartMapper cartMapper, ItemCartMapper itemCartMapper) {
		return new CartPersistenceAdapter(cartRepository, itemCartRepository, cartMapper, itemCartMapper);
	}

	@Bean
	public CartService itemCartService(CartPersistenceAdapter cartPersistenceAdapter) {
		return new CartService(cartPersistenceAdapter);
	}

}
