package br.com.fiap.postech.adjt.gateway.controller.checkout.request;

public record PaymentMethodFieldsRequest(
        String number,
        String expiration_month,
        String expiration_year,
        String cvv,
        String name
) { }
