package br.com.fiap.postech.adjt.cart.infrastructure.cart.controller.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

public record ItemAndConsumerIdRequestDTO(

		@JsonInclude(JsonInclude.Include.NON_NULL)
		String consumerId,

		@JsonInclude(JsonInclude.Include.NON_NULL)
		Long itemId
) {}
