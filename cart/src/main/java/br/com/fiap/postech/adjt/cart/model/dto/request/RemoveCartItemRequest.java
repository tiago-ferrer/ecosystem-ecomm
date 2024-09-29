package br.com.fiap.postech.adjt.cart.model.dto.request;

public record RemoveCartItemRequest(
        String consumerId,
        Long itemId
) {
}
