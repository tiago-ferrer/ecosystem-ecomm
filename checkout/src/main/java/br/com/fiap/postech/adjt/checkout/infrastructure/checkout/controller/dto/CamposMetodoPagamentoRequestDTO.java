package br.com.fiap.postech.adjt.checkout.infrastructure.checkout.controller.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

public record CamposMetodoPagamentoRequestDTO(

		@JsonInclude(JsonInclude.Include.NON_NULL)
		String number,

		@JsonInclude(JsonInclude.Include.NON_NULL)
		String expirationMonth,

		@JsonInclude(JsonInclude.Include.NON_NULL)
		String expirationYear,

		@JsonInclude(JsonInclude.Include.NON_NULL)
		String cvv,

		@JsonInclude(JsonInclude.Include.NON_NULL)
		String name
) {}
