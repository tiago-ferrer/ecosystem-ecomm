package br.com.fiap.postech.adjt.checkout.domain.model.payment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PaymentMethodModel {
    private String type;
    private FieldModel fields;
}
