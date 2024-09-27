package br.com.fiap.postech.adjt.checkout.model.dto.response;

public record CartItemResponse(
        Long itemId,
        Integer qnt
) {
}
