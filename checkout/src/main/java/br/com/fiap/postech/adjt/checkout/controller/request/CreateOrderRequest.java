package br.com.fiap.postech.adjt.checkout.controller.request;

public record CreateOrderRequest(
        String consumerId,
        Long amount,
        String currency,
        PaymentMethodRequest payment_method
) { }
