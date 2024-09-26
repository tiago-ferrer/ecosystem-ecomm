package br.com.fiap.postech.adjt.checkout.dto;

import br.com.fiap.postech.adjt.checkout.model.PaymentMethod;
import br.com.fiap.postech.adjt.checkout.validation.ValidUUID;
import com.fasterxml.jackson.annotation.JsonProperty;

public record ExternalPaymentRequestDTO(
        @ValidUUID String orderId,
        Double amount,
        String currency,
        PaymentMethod paymentMethod
) {
}