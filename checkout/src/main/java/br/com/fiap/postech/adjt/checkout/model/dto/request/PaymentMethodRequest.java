package br.com.fiap.postech.adjt.checkout.model.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentMethodRequest {

    @NotBlank(message = "Payment type cannot be blank")
    private String type;

    @NotNull(message = "Payment fields cannot be null")
    @JsonProperty("fields")
    private PaymentFieldsRequest fields;
}