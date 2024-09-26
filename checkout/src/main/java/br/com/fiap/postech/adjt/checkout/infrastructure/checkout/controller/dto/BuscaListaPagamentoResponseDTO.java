package br.com.fiap.postech.adjt.checkout.infrastructure.checkout.controller.dto;

import java.util.List;

public record BuscaListaPagamentoResponseDTO(

		List<BuscaPagamentoResponseDTO> orders
) {}
