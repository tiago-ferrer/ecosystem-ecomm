package br.com.fiap.postech.adjt.cart.infrastructure.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.fiap.postech.adjt.cart.domain.entity.Cart;
import br.com.fiap.postech.adjt.cart.usescases.CartInteractor;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/cart/item")
@AllArgsConstructor
public class CartItemsController {

	private final CartInteractor cartInteractorUseCase;
	private final CartItemDTOMapper cartItemDTOMapper;

	@PostMapping
	public CartItemsResponse addItems(@RequestBody CartItemsRequest request) {
		Cart cartBusinessObj = cartItemDTOMapper.toItemCart(request);
		
		Cart cart = cartInteractorUseCase.addItem(cartBusinessObj);
		
		return cartItemDTOMapper.toResponse(cart);
	}
}
