package br.com.fiap.postech.adjt.checkout.infrastructure.dtos;

public record CheckoutResponse(
        String orderId,
        String status
) {
}
