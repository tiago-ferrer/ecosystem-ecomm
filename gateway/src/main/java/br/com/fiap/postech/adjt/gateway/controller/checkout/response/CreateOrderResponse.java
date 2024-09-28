package br.com.fiap.postech.adjt.gateway.controller.checkout.response;

public record CreateOrderResponse(
        String orderId,
        String status
) { }
