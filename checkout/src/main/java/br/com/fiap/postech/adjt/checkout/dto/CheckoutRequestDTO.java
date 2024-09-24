package br.com.fiap.postech.adjt.checkout.dto;

import br.com.fiap.postech.adjt.checkout.model.PaymentMethod;

public record CheckoutRequestDTO(
        String consumerId,
        Double amount,
        String currency,
        PaymentMethod paymentMethod
) {
}
