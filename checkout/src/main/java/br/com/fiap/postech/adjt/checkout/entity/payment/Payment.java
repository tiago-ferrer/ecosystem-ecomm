package br.com.fiap.postech.adjt.checkout.entity.payment;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "tb_payment")
@Data
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String type;
    @ManyToOne
    private Field field;
}
