package br.com.fiap.postech.adjt.checkout.dto;

import java.util.UUID;

public record CartItemDTO(UUID consumerId, Long itemId, int quantity) {}


