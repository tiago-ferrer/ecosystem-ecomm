package br.com.fiap.postech.adjt.checkout.application.dto;

public record PaymentDTO(
        String type,
        FieldDTO fields
) {
}