package br.com.fiap.postech.adjt.cart.model.dto.request;

import java.util.UUID;

public record AddCartItemRequest(
        UUID consumerId,
        Long itemId,
        String quantity
) {
}
