package br.com.fiap.postech.adjt.checkout.infrastructure.payment.client.response;

public record PaymentResponseDTO(

		String orderId,
		String status
) {}
