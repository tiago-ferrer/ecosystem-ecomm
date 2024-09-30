package br.com.fiap.postech.adjt.payment.infrastructure.dtos;

public record CheckoutResponse(
        String orderId,
        String status
) {
}
