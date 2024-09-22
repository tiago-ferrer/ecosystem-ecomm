package br.com.fiap.postech.adjt.products.infrastructure.products.controller;

import br.com.fiap.postech.adjt.products.useCase.products.ProductsUseCase;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RestController;

@Tag(
		name = "Products",
		description = "Serviço de gerenciamento de itens para consumidores em um ambiente de e-commerce"
)
@RestController
public class ProductsController {

	private final ProductsUseCase service;

	public ProductsController(final ProductsUseCase service) {
		this.service = service;
	}

}
