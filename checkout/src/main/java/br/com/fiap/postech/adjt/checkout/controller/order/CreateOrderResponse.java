package br.com.fiap.postech.adjt.checkout.controller.order;

public record CreateOrderResponse(
        String orderId,
        String status
) { }
