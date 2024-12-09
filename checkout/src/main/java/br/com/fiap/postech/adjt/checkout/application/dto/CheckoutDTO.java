package br.com.fiap.postech.adjt.checkout.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

@Validated
public record CheckoutDTO(
        @NotBlank(message = "O codigo do consumidor é obrigatório.")
        @NotNull(message = "O codigo do consumidor não pode ser nulo.")
        @NotEmpty(message = "O codigo do consumidor não pode ser nulo.")
        String consumerId,
        Double amount,
        String currency,
        @NotBlank(message = "Os dados de pagamento são obrigatórios.")
        @NotNull(message = "Os dados de pagamento não podem ser nulos.")
        @JsonProperty("payment_method")
        PaymentDTO paymentMethod
) {
}