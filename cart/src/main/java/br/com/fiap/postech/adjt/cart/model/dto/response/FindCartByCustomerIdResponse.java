package br.com.fiap.postech.adjt.cart.model.dto.response;

import java.util.List;

public record FindCartByCustomerIdResponse(
        List<FindCartByCustomerIdCartItemResponse> items
) {
}
