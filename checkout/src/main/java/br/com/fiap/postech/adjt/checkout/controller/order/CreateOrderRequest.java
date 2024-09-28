package br.com.fiap.postech.adjt.checkout.controller.order;

import br.com.fiap.postech.adjt.checkout.controller.checkout.PaymentMethodRequest;

public record CreateOrderRequest(
        String consumerId,
        Long amount,
        String currency,
        PaymentMethodRequest payment_method
) { }
