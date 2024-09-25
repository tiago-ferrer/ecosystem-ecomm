package br.com.fiap.postech.adjt.checkout.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentFieldsRequest {

    @NotBlank(message = "Card number cannot be blank")
    private String number;

    @NotBlank(message = "Expiration month cannot be blank")
    @JsonProperty("expiration_month")
    private String expirationMonth;

    @NotBlank(message = "Expiration year cannot be blank")
    @JsonProperty("expiration_year")
    private String expirationYear;

    @NotBlank(message = "CVV cannot be blank")
    private String cvv;

    @NotBlank(message = "Cardholder name cannot be blank")
    private String name;
}