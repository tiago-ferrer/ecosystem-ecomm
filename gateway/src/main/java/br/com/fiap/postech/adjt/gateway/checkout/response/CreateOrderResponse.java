package br.com.fiap.postech.adjt.gateway.checkout.response;

public record CreateOrderResponse(
        String orderId,
        String status
) { }
