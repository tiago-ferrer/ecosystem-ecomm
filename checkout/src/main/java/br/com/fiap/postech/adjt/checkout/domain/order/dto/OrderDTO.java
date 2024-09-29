package br.com.fiap.postech.adjt.checkout.domain.order.dto;

import br.com.fiap.postech.adjt.checkout.domain.checkout.PaymentStatus;

import java.util.List;

public record OrderDTO(
    String orderId,
    List<ItemDTO> items,
    String paymentType,
    Long value,
    PaymentStatus paymentStatus
) { }
