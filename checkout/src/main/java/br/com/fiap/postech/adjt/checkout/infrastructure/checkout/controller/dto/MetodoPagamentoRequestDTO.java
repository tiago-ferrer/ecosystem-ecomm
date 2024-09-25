package br.com.fiap.postech.adjt.checkout.infrastructure.checkout.controller.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

public record MetodoPagamentoRequestDTO(

		@JsonInclude(JsonInclude.Include.NON_NULL)
		String type,

		@JsonInclude(JsonInclude.Include.NON_NULL)
		CamposMetodoPagamentoRequestDTO fields
) {}
