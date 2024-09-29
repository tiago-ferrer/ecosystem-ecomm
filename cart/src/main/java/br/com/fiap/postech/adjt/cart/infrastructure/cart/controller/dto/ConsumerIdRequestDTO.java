package br.com.fiap.postech.adjt.cart.infrastructure.cart.controller.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

public record ConsumerIdRequestDTO(

		@JsonInclude(JsonInclude.Include.NON_NULL)
		String consumerId
) {}
