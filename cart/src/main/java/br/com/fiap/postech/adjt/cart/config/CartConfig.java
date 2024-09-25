package br.com.fiap.postech.adjt.cart.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import br.com.fiap.postech.adjt.cart.gateways.CartGateway;
import br.com.fiap.postech.adjt.cart.infrastructure.controller.CartItemDTOMapper;
import br.com.fiap.postech.adjt.cart.infrastructure.gateways.CartEntityMapper;
import br.com.fiap.postech.adjt.cart.infrastructure.gateways.CartRepositoryGateway;
import br.com.fiap.postech.adjt.cart.infrastructure.gateways.ItemsCartEntityMapper;
import br.com.fiap.postech.adjt.cart.infrastructure.persistence.CartRepository;
import br.com.fiap.postech.adjt.cart.usescases.CartInteractor;

@Configuration
public class CartConfig {
  @Bean
  CartInteractor createUserCase(CartGateway cartGateway) {
    return new CartInteractor(cartGateway);
  }

  @Bean
  CartGateway userGateway(CartRepository repository, CartEntityMapper cartEntityMapper) {
    return new CartRepositoryGateway(repository, cartEntityMapper);
  }

  @Bean
  CartEntityMapper cartEntityMapper(ItemsCartEntityMapper itemsCartEntityMapper) {
    return new CartEntityMapper(itemsCartEntityMapper);
  }

  @Bean
  CartItemDTOMapper cartDTOMapper() {
    return new CartItemDTOMapper();
  }
}
