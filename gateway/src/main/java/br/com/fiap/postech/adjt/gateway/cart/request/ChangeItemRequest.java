package br.com.fiap.postech.adjt.gateway.cart.request;

public record ChangeItemRequest(
        String consumerId,
        Long itemId
) { }
