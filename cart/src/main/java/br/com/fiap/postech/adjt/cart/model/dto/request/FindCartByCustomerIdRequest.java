package br.com.fiap.postech.adjt.cart.model.dto.request;

import java.util.UUID;

public record FindCartByCustomerIdRequest(
        UUID consumerId
) {
}
