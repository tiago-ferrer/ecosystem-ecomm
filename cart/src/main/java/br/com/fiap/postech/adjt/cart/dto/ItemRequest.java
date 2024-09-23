package br.com.fiap.postech.adjt.cart.dto;

public record ItemRequest(String consumerId, Long itemId, int quantity) {
}
