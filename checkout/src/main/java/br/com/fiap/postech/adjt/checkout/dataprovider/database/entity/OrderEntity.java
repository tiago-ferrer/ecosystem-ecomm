package br.com.fiap.postech.adjt.checkout.dataprovider.database.entity;

import br.com.fiap.postech.adjt.checkout.domain.model.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class OrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "order_id", nullable = false)
    private UUID orderId;

    @Column(name = "paymentStatus", nullable = false)
    private PaymentStatus paymentStatus;

    @Column(name = "consumer_id", nullable = false)
    private String consumerId;

    @Column(name = "payment_type", nullable = false)
    private String paymentType;

    @Column(name = "value", nullable = false)
    private Double value;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItemEntity> items;

    // Add methods to add and remove children
    public void addItems(OrderItemEntity child) {
        items.add(child);
        child.setOrder(this);
    }

    public void removeItems(OrderItemEntity child) {
        items.remove(child);
        child.setOrder(null);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OrderEntity that)) return false;
        return Objects.equals(orderId, that.orderId) && paymentStatus == that.paymentStatus && Objects.equals(consumerId, that.consumerId) && Objects.equals(paymentType, that.paymentType) && Objects.equals(value, that.value) && Objects.equals(items, that.items);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderId, paymentStatus, consumerId, paymentType, value, items);
    }
}