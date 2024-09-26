package br.com.fiap.postech.adjt.cart.model.dto.request;

public record AddCartItemRequest(
        String consumerId,
        Long itemId,
        String quantity
) {
}
