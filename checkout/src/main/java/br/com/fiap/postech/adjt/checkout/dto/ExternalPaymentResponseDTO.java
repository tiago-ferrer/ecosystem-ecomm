package br.com.fiap.postech.adjt.checkout.dto;

import br.com.fiap.postech.adjt.checkout.model.PaymentStatus;

public record ExternalPaymentResponseDTO(
        String orderId,
        String status
) {
}
