package br.com.fiap.postech.adjt.checkout.entity.payment;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "tb_field")
@Data
public class Field {
    @Id
    private String number;
    private String expiration_month;
    private String expiration_year;
    private String cvv;
    private String name;
}
