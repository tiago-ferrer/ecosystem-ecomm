package br.com.fiap.postech.adjt.checkout.entity.order;


import br.com.fiap.postech.adjt.checkout.entity.cart.Cart;
import br.com.fiap.postech.adjt.checkout.entity.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Entity
@Table(name = "tb_order")
@Data
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID orderId;
    private UUID consumerId;
    private String paymentType;
    @Column(name = "orderValue")
    private int value;
    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    @OneToOne(fetch = FetchType.LAZY)
    private Cart cart;


}
