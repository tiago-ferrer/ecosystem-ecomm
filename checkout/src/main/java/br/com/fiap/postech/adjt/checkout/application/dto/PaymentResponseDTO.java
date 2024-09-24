package br.com.fiap.postech.adjt.checkout.application.dto;

public record PaymentResponseDTO(
        String orderId,
        String status
) {
}
