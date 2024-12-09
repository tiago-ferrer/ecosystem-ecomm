package br.com.fiap.postech.adjt.checkout.application.dto;

import br.com.fiap.postech.adjt.checkout.domain.model.enums.PaymentStatus;

import java.util.List;

public record OrderDTO(
        String orderId,
        String paymentType,
        Double value,
        PaymentStatus paymentStatus,
        List<OrderItemDTO> items
) {
}