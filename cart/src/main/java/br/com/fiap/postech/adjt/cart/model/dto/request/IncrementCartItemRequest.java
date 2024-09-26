package br.com.fiap.postech.adjt.cart.model.dto.request;

import java.util.UUID;

public record IncrementCartItemRequest(
        UUID consumerId,
        Long itemId
) {
}
