package br.com.fiap.postech.adjt.gateway.cart.request;

public record AddItemRequest(
        String consumerId,
        String itemId,
        String quantity
) { }
