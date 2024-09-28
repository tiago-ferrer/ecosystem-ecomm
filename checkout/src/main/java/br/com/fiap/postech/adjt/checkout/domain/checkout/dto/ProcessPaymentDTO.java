package br.com.fiap.postech.adjt.checkout.domain.checkout.dto;

import br.com.fiap.postech.adjt.checkout.controller.checkout.PaymentMethodRequest;

public record ProcessPaymentDTO(
        String orderId,
        Long amount,
        String currency,
        PaymentMethodRequest payment_method
) { }
