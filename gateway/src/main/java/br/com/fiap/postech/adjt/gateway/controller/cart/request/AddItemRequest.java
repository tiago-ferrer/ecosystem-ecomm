package br.com.fiap.postech.adjt.gateway.controller.cart.request;

public record AddItemRequest(
        String consumerId,
        String itemId,
        String quantity
) { }
