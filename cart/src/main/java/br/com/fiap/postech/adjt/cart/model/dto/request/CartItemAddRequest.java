package br.com.fiap.postech.adjt.cart.model.dto.request;

public record CartItemAddRequest(
        Integer itemId,
        String quantity
) {
}
