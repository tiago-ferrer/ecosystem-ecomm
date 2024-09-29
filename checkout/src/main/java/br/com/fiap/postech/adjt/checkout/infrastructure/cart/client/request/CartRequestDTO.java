package br.com.fiap.postech.adjt.checkout.infrastructure.cart.client.request;

import com.fasterxml.jackson.annotation.JsonInclude;

public record CartRequestDTO(

		@JsonInclude(JsonInclude.Include.NON_NULL)
		String consumerId
) {}
