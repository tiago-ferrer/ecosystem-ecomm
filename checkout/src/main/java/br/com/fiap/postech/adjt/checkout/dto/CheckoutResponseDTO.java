package br.com.fiap.postech.adjt.checkout.dto;

import br.com.fiap.postech.adjt.checkout.model.PaymentStatus;
import lombok.Builder;

@Builder
public record CheckoutResponseDTO(
        String orderId,
        PaymentStatus paymentStatus
) {
}
