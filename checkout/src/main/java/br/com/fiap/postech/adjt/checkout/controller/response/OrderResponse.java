package br.com.fiap.postech.adjt.checkout.controller.response;

import br.com.fiap.postech.adjt.checkout.entity.PaymentStatus;

import java.util.List;

public record OrderResponse(
    String orderId,
    List<ItemResponse> items,
    String paymentType,
    Long value,
    PaymentStatus paymentStatus
) { }
