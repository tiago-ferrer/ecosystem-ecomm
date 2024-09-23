package br.com.fiap.postech.adjt.products.infrastructure.products.controller.dto;

import java.math.BigDecimal;

public record ItemResponseDTO(

		Long id,
		String title,
		BigDecimal price

) {}
