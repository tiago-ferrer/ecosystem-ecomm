package br.com.fiap.postech.adjt.checkout.infrastructure.checkout.controller.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

public record SolicitaPagamentoRequestDTO(

		@JsonInclude(JsonInclude.Include.NON_NULL)
		String consumerId,

		@JsonInclude(JsonInclude.Include.NON_NULL)
		Long amount,

		@JsonInclude(JsonInclude.Include.NON_NULL)
		String currency,

		@JsonInclude(JsonInclude.Include.NON_NULL)
		MetodoPagamentoRequestDTO paymentMethod
) {}
