package br.com.fiap.postech.adjt.checkout.dto;

public record PaymentMethodDTO(String type,
                               PaymentFieldsDTO fields) {
}
