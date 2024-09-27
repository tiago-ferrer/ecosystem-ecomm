package br.com.fiap.postech.adjt.checkout.dto;

import java.util.List;
import java.util.UUID;

public record CartDTO(UUID consumerId, List<CartItemDTO> items) {
}
