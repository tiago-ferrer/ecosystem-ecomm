package br.com.fiap.postech.adjt.checkout.entity.checkout;

import br.com.fiap.postech.adjt.checkout.entity.enums.PaymentStatus;
import br.com.fiap.postech.adjt.checkout.entity.payment.Payment;
import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Entity
@Table(name = "tb_Checkout")
@Data
public class Checkout {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID checkoutId;
    private int amount;
    private String currency;
    @OneToOne
    private Payment paymentMethod;

    private UUID consumerId;
    private String paymentType;
    private int value;
    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

}
