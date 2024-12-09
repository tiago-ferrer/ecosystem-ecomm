package br.com.fiap.postech.adjt.checkout.domain.model.payment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FieldModel {
    private String number;
    private String expiration_month;
    private String expiration_year;
    private String cvv;
    private String name;
}
