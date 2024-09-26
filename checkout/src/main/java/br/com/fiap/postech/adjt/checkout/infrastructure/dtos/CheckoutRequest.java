package br.com.fiap.postech.adjt.checkout.infrastructure.dtos;


public record CheckoutRequest(
        String consumerId,
        Integer amount,
        String currency,
        PaymentMethod payment_method
) {
}
