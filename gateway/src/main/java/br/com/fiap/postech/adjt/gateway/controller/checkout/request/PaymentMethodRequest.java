package br.com.fiap.postech.adjt.gateway.controller.checkout.request;

public record PaymentMethodRequest(
        String type,
        PaymentMethodFieldsRequest fields
) { }
