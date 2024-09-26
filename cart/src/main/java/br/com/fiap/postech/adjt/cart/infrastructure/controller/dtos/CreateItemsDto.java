package br.com.fiap.postech.adjt.cart.infrastructure.controller.dtos;

import java.util.UUID;

public record CreateItemsDto(UUID consumerId, String itemId, Integer quantity) {

}
