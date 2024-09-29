package br.com.fiap.postech.adjt.cart.infrastructure.cart.controller;

import br.com.fiap.postech.adjt.cart.infrastructure.cart.controller.dto.*;
import br.com.fiap.postech.adjt.cart.useCase.cart.CartUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(
		name = "Cart",
		description = """
				Esta aplicação fornece um serviço de gerenciamento de carrinho de compras para consumidores em um
				ambiente de e-commerce. O serviço permite que os usuários interajam com seus carrinhos de compras de
				forma eficiente e prática através de uma API RESTful
				"""
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
	public ResponseEntity<ItemResponseDTO> deleta(@RequestBody @Valid final ItemAndConsumerIdRequestDTO dadosItem) {
		return ResponseEntity
				.status(HttpStatus.OK)
				.body(this.service.deleta(dadosItem));

	}

	@Operation(
			summary = "Incrementa uma unidade de um item específico do carrinho de um consumidor."
	)
	@PutMapping(URL_ITEM)
	public ResponseEntity<ItemResponseDTO> atualiza(@RequestBody @Valid final ItemAndConsumerIdRequestDTO dadosItem) {
		return ResponseEntity
				.status(HttpStatus.OK)
				.body(this.service.atualiza(dadosItem));

	}

	@Operation(
			summary = """
						Deve retorna o carrinho de um consumidor.
						
						ATENÇÃO: ESSE ENDPOINT NÃO FUNCIONA VIA SWAGGER, A REQUEST DEVE SER REALIZADA VIA CURL OU COM 
						O WEBCLIENT DO SPRING BOOT
					"""
	)
	@GetMapping
	public ResponseEntity<InfoItensResponseDTO> busca(@RequestBody @Valid final ConsumerIdRequestDTO consumer) {
		return ResponseEntity
				.status(HttpStatus.OK)
				.body(this.service.busca(consumer));

	}

	@Operation(
			summary = "Remove todo o carrinho de um consumidor."
	)
	@DeleteMapping
	public ResponseEntity<ItemResponseDTO> deletaOCarrinho(@RequestBody @Valid final ConsumerIdRequestDTO consumer) {
		return ResponseEntity
				.status(HttpStatus.OK)
				.body(this.service.deletaOCarrinho(consumer));

	}

}
