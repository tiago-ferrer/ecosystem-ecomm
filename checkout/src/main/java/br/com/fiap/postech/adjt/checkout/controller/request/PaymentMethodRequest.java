package br.com.fiap.postech.adjt.checkout.controller.request;

public record PaymentMethodRequest(
        String type,
        PaymentMethodFieldsRequest fields
) { }
