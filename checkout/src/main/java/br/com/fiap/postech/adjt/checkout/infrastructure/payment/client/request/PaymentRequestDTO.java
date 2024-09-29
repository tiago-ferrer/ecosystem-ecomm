package br.com.fiap.postech.adjt.checkout.infrastructure.payment.client.request;

import br.com.fiap.postech.adjt.checkout.infrastructure.checkout.controller.dto.MetodoPagamentoRequestDTO;
import com.fasterxml.jackson.annotation.JsonInclude;

public record PaymentRequestDTO(

		@JsonInclude(JsonInclude.Include.NON_NULL)
		String orderId,

		@JsonInclude(JsonInclude.Include.NON_NULL)
		Long amount,

		@JsonInclude(JsonInclude.Include.NON_NULL)
		String currency,

		@JsonInclude(JsonInclude.Include.NON_NULL)
        MetodoPagamentoRequestDTO paymentMethod
) {}
