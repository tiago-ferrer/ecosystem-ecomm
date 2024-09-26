package br.com.fiap.postech.adjt.cart.infrastructure.controller;

import br.com.fiap.postech.adjt.cart.infrastructure.controller.dtos.CartGetItemsResponseDto;
import br.com.fiap.postech.adjt.cart.infrastructure.controller.dtos.CartItemMapper;
import br.com.fiap.postech.adjt.cart.infrastructure.controller.dtos.CreateItemsDto;
import org.springframework.web.bind.annotation.*;

import br.com.fiap.postech.adjt.cart.domain.entity.Cart;
import br.com.fiap.postech.adjt.cart.usescases.CartInteractor;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/cart")
@AllArgsConstructor
public class CartItemsController {

	private final CartInteractor cartInteractorUseCase;
	private final CartItemMapper cartItemMapper;

	@PostMapping("/items")
	public CartGetItemsResponseDto addItems(@RequestBody CreateItemsDto request) {
		Cart cartBusinessObj = cartItemMapper.toItemCart(request);
		
		Cart cart = cartInteractorUseCase.addItem(cartBusinessObj);
		
		return cartItemMapper.toResponse(cart);
	}

	@DeleteMapping("/item")
	public CartGetItemsResponseDto delete(@RequestBody CreateItemsDto request) {
		Cart cartBusinessObj = cartItemMapper.toItemCart(request);

		Cart cart = cartInteractorUseCase.addItem(cartBusinessObj);

		return cartItemMapper.toResponse(cart);
	}
}
