package br.com.fiap.postech.adjt.products.infrastructure.products.controller.dto;

public record ErrorHandlingResponseDTO(

		int codeError,
		String description
) {}
