package br.com.fiap.postech.adjt.checkout.dto;

import br.com.fiap.postech.adjt.checkout.controller.request.PaymentMethodRequest;

public record ProcessPaymentDTO(
        String orderId,
        Long amount,
        String currency,
        PaymentMethodRequest payment_method
) { }
