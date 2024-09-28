package br.com.fiap.postech.adjt.cart.controller.item;

public record AddItemRequest(
        String consumerId,
        String itemId,
        String quantity
) { }
