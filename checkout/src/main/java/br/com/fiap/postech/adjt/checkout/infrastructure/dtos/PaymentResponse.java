package br.com.fiap.postech.adjt.checkout.infrastructure.dtos;

import br.com.fiap.postech.adjt.checkout.domain.entities.enums.OrderStatus;

import java.util.UUID;

public record PaymentResponse(UUID orderId, OrderStatus status) {
}
