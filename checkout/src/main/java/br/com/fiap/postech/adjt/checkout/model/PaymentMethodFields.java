package br.com.fiap.postech.adjt.checkout.model;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class PaymentMethodFields {

    private String number;

    private String expiration_month;

    private String expiration_year;

    private String cvv;

    private String name;

}