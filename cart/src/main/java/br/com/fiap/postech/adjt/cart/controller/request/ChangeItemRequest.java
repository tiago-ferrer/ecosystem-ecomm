package br.com.fiap.postech.adjt.cart.controller.request;

public record ChangeItemRequest(
        String consumerId,
        Long itemId
) { }
