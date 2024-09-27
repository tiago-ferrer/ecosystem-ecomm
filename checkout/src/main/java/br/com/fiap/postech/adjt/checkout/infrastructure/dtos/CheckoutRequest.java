package br.com.fiap.postech.adjt.checkout.infrastructure.dtos;


import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

public record CheckoutRequest(
        @NotBlank(message = "Deve preencher o consumerId")
        UUID consumerId,
        @NotBlank(message = "Deve preencher o amount")
        Integer amount,
        @NotBlank(message = "Deve currency o amount")
        String currency,
        @NotBlank(message = "Deve payment_method o amount")
        PaymentMethod payment_method
) {
}
