package br.com.fiap.postech.adjt.payment.infrastructure.dtos;

public record PaymentMethod(
    String type,
    Fields fields
) {

}
