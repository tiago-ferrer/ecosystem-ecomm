package br.com.fiap.postech.adjt.checkout.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public record PaymentMethodFieldsRequestDTO(
        @JsonProperty("number") String number,
        @JsonProperty("expiration_month") String expiration_month,
        @JsonProperty("expiration_year") String expiration_year,
        @JsonProperty("cvv") String cvv,
        @JsonProperty("name") String name
) {
}