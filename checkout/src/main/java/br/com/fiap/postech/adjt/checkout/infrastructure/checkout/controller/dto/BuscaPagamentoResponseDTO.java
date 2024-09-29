package br.com.fiap.postech.adjt.checkout.infrastructure.checkout.controller.dto;

import java.math.BigDecimal;
import java.util.List;

public record BuscaPagamentoResponseDTO(

		String orderId,
		List<ItensPagamentoResponseDTO> items,
		String paymentType,
		BigDecimal value,
		String paymentStatus
) {}
