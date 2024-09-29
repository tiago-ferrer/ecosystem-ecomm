package br.com.fiap.postech.adjt.checkout.controller.checkout;

import br.com.fiap.postech.adjt.checkout.controller.checkout.PaymentMethodFieldsRequest;

public record PaymentMethodRequest(
        String type,
        PaymentMethodFieldsRequest fields
) { }
