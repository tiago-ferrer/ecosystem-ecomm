package br.com.fiap.postech.adjt.checkout.model;

import br.com.fiap.postech.adjt.checkout.dto.OrderItemDTO;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    @Column(unique = true, nullable = false)
    private UUID orderId;

    @Column(nullable = false)
    private UUID consumerId;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    private String paymentType;
    private double value;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items = new ArrayList<>();

    public Order(UUID orderId, UUID consumerId, List<OrderItemDTO> orderItems, String paymentType, double value, PaymentStatus paymentStatus) {
        this.orderId = orderId;
        this.consumerId = consumerId;
        this.paymentType = paymentType;
        this.value = value;
        this.paymentStatus = paymentStatus;
        this.status = OrderStatus.PENDING;

        for (OrderItemDTO item : orderItems) {
            this.items.add(new OrderItem(item.itemId(), item.quantity(), this));
        }
    }

    public Order(UUID orderId, List<OrderItemDTO> orderItems, String paymentType, double value, PaymentStatus paymentStatus) {
    }
}
