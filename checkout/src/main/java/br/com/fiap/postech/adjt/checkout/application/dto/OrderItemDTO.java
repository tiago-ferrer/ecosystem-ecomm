package br.com.fiap.postech.adjt.checkout.application.dto;

public record OrderItemDTO(
        String itemId,
        Long quantity) {
}
