package br.com.fiap.postech.adjt.products.infrastructure.products.controller;

import br.com.fiap.postech.adjt.products.infrastructure.products.controller.dto.ItemResponseDTO;
import br.com.fiap.postech.adjt.products.useCase.products.ProductsUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@Tag(
		name = "Products",
		description = "Serviço de gerenciamento de itens para consumidores em um ambiente de e-commerce"
)
@RestController
public class ProductsController {

	public static final String URL_ITEM = "/item/{itemId}/{quantity}";

	private final ProductsUseCase service;

	public ProductsController(final ProductsUseCase service) {
		this.service = service;
	}

	@Operation(
			summary = "Busca um item na API EXTERNA."
	)
	@GetMapping(URL_ITEM)
	public ResponseEntity<ItemResponseDTO> busca(@PathVariable final Long itemId,
												 @PathVariable final Long quantity) {
		return ResponseEntity
				.status(HttpStatus.OK)
				.body(this.service.busca(itemId, quantity));

	}

}
