package br.com.fiap.postech.adjt.checkout.dto;

import br.com.fiap.postech.adjt.checkout.model.PaymentMethodType;
import com.fasterxml.jackson.annotation.JsonProperty;

public record PaymentMethodRequestDTO(
        @JsonProperty("type") PaymentMethodType type,
        @JsonProperty("fields") PaymentMethodFieldsRequestDTO fields
) {
}