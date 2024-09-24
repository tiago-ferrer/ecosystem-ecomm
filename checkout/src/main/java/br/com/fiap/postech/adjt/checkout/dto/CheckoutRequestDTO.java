package br.com.fiap.postech.adjt.checkout.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CheckoutRequestDTO(
        String consumerId,
        @JsonProperty("amount") Double amount,
        @JsonProperty("currency") String currency,
        @JsonProperty("payment_method") PaymentMethodRequestDTO paymentMethod
) {
}