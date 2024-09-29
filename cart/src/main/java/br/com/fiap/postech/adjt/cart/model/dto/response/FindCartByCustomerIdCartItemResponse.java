package br.com.fiap.postech.adjt.cart.model.dto.response;

public record FindCartByCustomerIdCartItemResponse(
        Long itemId,
        Integer qnt
) {
}
