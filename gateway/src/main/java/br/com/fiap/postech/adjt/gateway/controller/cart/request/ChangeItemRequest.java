package br.com.fiap.postech.adjt.gateway.controller.cart.request;

public record ChangeItemRequest(
        String consumerId,
        Long itemId
) { }
