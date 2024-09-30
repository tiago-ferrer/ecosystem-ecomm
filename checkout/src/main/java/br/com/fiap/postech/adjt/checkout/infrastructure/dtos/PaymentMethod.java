package br.com.fiap.postech.adjt.checkout.infrastructure.dtos;

public record PaymentMethod(
    String type,
    Fields fields
) {

}
