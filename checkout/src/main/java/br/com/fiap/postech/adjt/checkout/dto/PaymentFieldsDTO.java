package br.com.fiap.postech.adjt.checkout.dto;

public record PaymentFieldsDTO(String number,
                               String expirationMonth,
                               String expirationYear,
                               String cvv,
                               String name) {
}
