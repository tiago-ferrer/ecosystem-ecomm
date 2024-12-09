package br.com.fiap.postech.adjt.checkout.application.dto;

public record CheckoutResponseDTO(
        String orderId,
        String status
) {
}