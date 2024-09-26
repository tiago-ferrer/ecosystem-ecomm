package br.com.fiap.postech.adjt.checkout.dto;

import java.util.UUID;

public record OrderItemDTO(UUID consumerId, Long itemId, int quantity) {}


