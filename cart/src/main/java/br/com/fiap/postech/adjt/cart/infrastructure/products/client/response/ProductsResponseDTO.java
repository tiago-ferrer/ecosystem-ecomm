package br.com.fiap.postech.adjt.cart.infrastructure.products.client.response;

import java.math.BigDecimal;

public record ProductsResponseDTO(

		Long id,
		String title,
		BigDecimal price

) {}
