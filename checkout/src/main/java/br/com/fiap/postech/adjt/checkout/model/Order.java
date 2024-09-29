package br.com.fiap.postech.adjt.checkout.model;

import java.util.List;
import java.util.UUID;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID orderId;
    private UUID consumerId;
    private String paymentType;
    private int value;
    private String paymentStatus;

    @OneToMany(cascade = CascadeType.ALL)
    private List<CartItem> items;

    @OneToOne(cascade = CascadeType.ALL) // Use @OneToOne for a single card
    private Card card;

    public Order(UUID orderId, String paymentType, int value, String paymentStatus, List<CartItem> items) {
        this.orderId = orderId;
        this.paymentType = paymentType;
        this.value = value;
        this.paymentStatus = paymentStatus;
        this.items = items;
    }
}