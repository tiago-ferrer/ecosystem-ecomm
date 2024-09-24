package br.com.fiap.postech.adjt.checkout.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class PaymentMethod {

    @JsonProperty("type")
    private PaymentMethodType type;

    @Embedded
    @JsonProperty("fields")
    private PaymentMethodFields fields;

}