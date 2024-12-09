package br.com.fiap.postech.adjt.gateway.checkout.response;

import java.util.List;

public record OrderResponse(
        String orderId,
        List<ItemResponse> items,
        String paymentType,
        Long value,
        String paymentStatus
) { }
