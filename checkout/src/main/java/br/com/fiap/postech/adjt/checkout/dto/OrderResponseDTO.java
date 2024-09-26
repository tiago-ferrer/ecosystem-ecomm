package br.com.fiap.postech.adjt.checkout.dto;

import br.com.fiap.postech.adjt.checkout.model.Order;

public record OrderResponseDTO(
        Order order
) {
}
