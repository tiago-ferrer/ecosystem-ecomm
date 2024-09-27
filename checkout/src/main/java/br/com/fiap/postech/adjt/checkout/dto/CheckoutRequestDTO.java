package br.com.fiap.postech.adjt.checkout.dto;

import br.com.fiap.postech.adjt.checkout.validation.ValidUUID;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public record CheckoutRequestDTO(
        @ValidUUID String consumerId,
        @JsonProperty("amount") Double amount,
        @JsonProperty("currency") String currency,
        @JsonProperty("payment_method") PaymentMethodRequestDTO paymentMethod
) {
}