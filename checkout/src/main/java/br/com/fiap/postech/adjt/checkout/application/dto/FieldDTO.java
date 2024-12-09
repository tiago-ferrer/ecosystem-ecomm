package br.com.fiap.postech.adjt.checkout.application.dto;

public record FieldDTO(
        String number,
        String expiration_month,
        String expiration_year,
        String cvv,
        String name
) {
}