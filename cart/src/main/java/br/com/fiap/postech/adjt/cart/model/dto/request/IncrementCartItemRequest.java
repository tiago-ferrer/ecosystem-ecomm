package br.com.fiap.postech.adjt.cart.model.dto.request;

public record IncrementCartItemRequest(
        String consumerId,
        Long itemId
) {
}
