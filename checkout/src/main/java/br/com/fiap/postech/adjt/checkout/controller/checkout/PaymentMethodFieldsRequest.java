package br.com.fiap.postech.adjt.checkout.controller.checkout;

public record PaymentMethodFieldsRequest(
        String number,
        String expiration_month,
        String expiration_year,
        String cvv,
        String name
) { }
