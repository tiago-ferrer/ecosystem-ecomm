package br.com.fiap.postech.adjt.checkout.model.dto.response;

import java.util.List;

public record CartResponse(
        List<CartItemResponse> items
) {
}
