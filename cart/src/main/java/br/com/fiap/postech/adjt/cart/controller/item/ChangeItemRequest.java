package br.com.fiap.postech.adjt.cart.controller.item;

public record ChangeItemRequest(
        String consumerId,
        Long itemId
) { }
