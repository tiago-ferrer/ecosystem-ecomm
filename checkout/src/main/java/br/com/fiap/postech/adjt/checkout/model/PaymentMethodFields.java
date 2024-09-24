package br.com.fiap.postech.adjt.checkout.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class PaymentMethodFields {

    @Id
    private Long id;
    private int number;
    private int expiration_month;
    private int expiration_year;
    private int cvv;
    private String name;
}
