package br.com.fiap.postech.adjt.cart.infrastructure.cart.controller;

import br.com.fiap.postech.adjt.cart.infrastructure.cart.controller.dto.ItemResponseDTO;
import br.com.fiap.postech.adjt.cart.infrastructure.cart.controller.dto.AdicionaItemRequestDTO;
import br.com.fiap.postech.adjt.cart.infrastructure.cart.controller.dto.DeletaItemRequestDTO;
import br.com.fiap.postech.adjt.cart.useCase.cart.CartUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Tag(
		name = "Cart",
		description = "Serviço de gerenciamento de carrinho de compras para consumidores em um ambiente de e-commerce"
)
@RestController
public class CartController {

	public static final String URL_ITEMS = "/items";
	public static final String URL_ITEM = "/item";

	private final CartUseCase service;

	public CartController(final CartUseCase service) {
		this.service = service;
	}

	@Operation(
			summary = "Adiciona um item ao carrinho de um consumidor específico."
	)
	@PostMapping(URL_ITEMS)
	public ResponseEntity<ItemResponseDTO> adiciona(@RequestBody @Valid final AdicionaItemRequestDTO dadosItem) {
		return ResponseEntity
				.status(HttpStatus.OK)
				.body(this.service.adiciona(dadosItem));

	}

	@Operation(
			summary = "Remove uma unidade do item específico do carrinho de um consumidor. Caso o item atinga quantidade zero, removê-lo."
	)
	@DeleteMapping(URL_ITEM)
	public ResponseEntity<ItemResponseDTO> deleta(@RequestBody @Valid final DeletaItemRequestDTO dadosItem) {
		return ResponseEntity
				.status(HttpStatus.OK)
				.body(this.service.deleta(dadosItem));

	}

}
