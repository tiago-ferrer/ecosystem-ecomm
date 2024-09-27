package br.com.fiap.postech.adjt.checkout.infrastructure.dtos;

import br.com.fiap.postech.adjt.checkout.domain.entities.Item;
import br.com.fiap.postech.adjt.checkout.domain.entities.enums.OrderStatus;

import java.util.List;
import java.util.UUID;

public record OrderInfo(
        UUID orderId,
        List<Item> items,
        String paymentType,
        Integer value,
        OrderStatus paymentStatus
) {
}
