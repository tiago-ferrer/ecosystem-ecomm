package br.com.fiap.postech.adjt.checkout.infrastructure.checkout.controller.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonInclude;

public record CamposMetodoPagamentoRequestDTO(

		@JsonInclude(JsonInclude.Include.NON_NULL)
		String number,

		@JsonInclude(JsonInclude.Include.NON_NULL)
		@JsonAlias("expiration_month")
		String expirationMonth,

		@JsonInclude(JsonInclude.Include.NON_NULL)
		@JsonAlias("expiration_year")
		String expirationYear,

		@JsonInclude(JsonInclude.Include.NON_NULL)
		String cvv,

		@JsonInclude(JsonInclude.Include.NON_NULL)
		String name
) {}
