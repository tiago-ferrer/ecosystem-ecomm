package br.com.fiap.postech.adjt.cart.infrastructure.adapters.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import br.com.fiap.postech.adjt.cart.domain.service.CartService;
import br.com.fiap.postech.adjt.cart.infrastructure.adapters.output.persistence.CartPersistenceAdapter;
import br.com.fiap.postech.adjt.cart.infrastructure.adapters.output.persistence.gateway.ProdutoServiceClient;
import br.com.fiap.postech.adjt.cart.infrastructure.adapters.output.persistence.mapper.CartMapper;
import br.com.fiap.postech.adjt.cart.infrastructure.adapters.output.persistence.mapper.ItemCartMapper;
import br.com.fiap.postech.adjt.cart.infrastructure.adapters.output.persistence.repository.CartRepository;

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
			CartMapper cartMapper, ItemCartMapper itemCartMapper, ProdutoServiceClient produtoServiceClient ) {
		return new CartPersistenceAdapter(cartRepository, cartMapper, itemCartMapper, produtoServiceClient);
	}

	@Bean
	public CartService itemCartService(CartPersistenceAdapter cartPersistenceAdapter) {
		return new CartService(cartPersistenceAdapter);
	}

}
