package br.com.fiap.postech.adjt.checkout.dto;

import java.util.List;
import java.util.UUID;

public record OrderResponseDTO(UUID orderId, List<OrderItemDTO> items, String paymentType, double value, br.com.fiap.postech.adjt.checkout.model.PaymentStatus paymentStatus) {
}