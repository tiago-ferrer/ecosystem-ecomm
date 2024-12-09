package br.com.fiap.postech.adjt.cart.controller.request;

public record AddItemRequest(
        String consumerId,
        String itemId,
        String quantity
) { }
