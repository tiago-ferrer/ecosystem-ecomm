package br.com.fiap.postech.adjt.products.infrastructure.fakeStoreApi.client.response;

import java.math.BigDecimal;

public record ItemDTO(

		Long id,
		String title,
		BigDecimal price,
		String category,
		String description,
		String image
) {}
