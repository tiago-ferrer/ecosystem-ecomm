package br.com.fiap.postech.adjt.checkout.application.dto;

public record CheckoutDTO(
        String consumerId,
        Double amount,
        String currency,
        PaymentDTO payment_method
) {
}