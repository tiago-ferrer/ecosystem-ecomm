package br.com.fiap.postech.adjt.cart.infrastructure.controller;

import java.util.UUID;

public record CartItemsRequest(UUID consumerId, String itemId, Integer quantity) {

}
