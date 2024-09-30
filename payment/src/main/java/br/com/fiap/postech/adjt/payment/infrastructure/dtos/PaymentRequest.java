package br.com.fiap.postech.adjt.payment.infrastructure.dtos;

import java.util.UUID;

public record PaymentRequest(
        UUID orderId,
        Integer amount,
        String currency,
        PaymentMethod payment_method
) {
}
