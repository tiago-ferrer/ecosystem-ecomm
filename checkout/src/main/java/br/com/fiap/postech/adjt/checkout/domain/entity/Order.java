package br.com.fiap.postech.adjt.checkout.domain.entity;

import br.com.fiap.postech.adjt.checkout.domain.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Entity(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "order_id", nullable = false)
    private UUID orderId;

    @Column(name = "paymentStatus", nullable = false)
    private PaymentStatus paymentStatus;

    @Column(name = "customer_id", nullable = false)
    private String customerId;

    @Column(name = "payment_type", nullable = false)
    private String paymentType;

    @Column(name = "value", nullable = false)
    private Double value;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<OrderItem> items;
}