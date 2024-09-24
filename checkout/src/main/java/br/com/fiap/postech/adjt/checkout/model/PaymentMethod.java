package br.com.fiap.postech.adjt.checkout.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class PaymentMethod {

    @Id
    private Long id;

    @OneToOne
    private Checkout checkout;
    @JsonProperty("type")
    private PaymentMethodType type;

    @OneToOne
    @JsonProperty("fields")
    private PaymentMethodFields fields;
}
