package br.com.fiap.postech.adjt.gateway.checkout.request;

public record PaymentMethodRequest(
        String type,
        PaymentMethodFieldsRequest fields
) { }
