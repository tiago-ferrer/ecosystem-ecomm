package br.com.fiap.postech.adjt.checkout.infrastructure.dtos;


import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

public record CheckoutRequest(
        UUID consumerId,
        Integer amount,
        String currency,
        PaymentMethod payment_method
) {
}
