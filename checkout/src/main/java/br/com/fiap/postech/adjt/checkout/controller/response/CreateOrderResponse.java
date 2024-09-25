package br.com.fiap.postech.adjt.checkout.controller.response;

public record CreateOrderResponse(
        String orderId,
        String status
) { }
