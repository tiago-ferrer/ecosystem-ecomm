package br.com.fiap.postech.adjt.checkout.controller.order;

import br.com.fiap.postech.adjt.checkout.domain.checkout.PaymentStatus;

import java.util.List;

public record OrderResponse(
    String orderId,
    List<ItemResponse> items,
    String paymentType,
    Long value,
    PaymentStatus paymentStatus
) { }
