package br.com.fiap.postech.adjt.checkout.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

@Validated
public record PaymentCheckoutDTO(
        String orderId,
        Double amount,
        String currency,
        PaymentDTO paymentMethod
) {
}