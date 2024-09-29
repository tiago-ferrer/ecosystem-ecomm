package br.com.fiap.postech.adjt.gateway.controller.checkout.request;

public record CreateOrderRequest(
        String consumerId,
        Double amount,
        String currency,
        PaymentMethodRequest payment_method
) { }
