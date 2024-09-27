package br.com.fiap.postech.adjt.payment.infrastructure.dtos;

import br.com.fiap.postech.adjt.payment.domain.entities.enums.OrderStatus;

import java.util.UUID;

public record PaymentResponse(UUID orderId, OrderStatus status) {
}
