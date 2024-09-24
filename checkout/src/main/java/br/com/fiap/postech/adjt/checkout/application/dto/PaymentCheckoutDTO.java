package br.com.fiap.postech.adjt.checkout.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.validation.annotation.Validated;

@Validated
public record PaymentCheckoutDTO(
        String orderId,
        Double amount,
        String currency,
        PaymentDTO payment_method
) {
}