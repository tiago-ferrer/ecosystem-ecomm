package br.com.fiap.postech.adjt.payment.infrastructure.dtos;

public record Fields(
        String number,
        String expiration_month,
        String expiration_year,
        String cvv,
        String name
) {

}
