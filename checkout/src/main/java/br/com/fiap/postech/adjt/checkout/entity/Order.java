package br.com.fiap.postech.adjt.checkout.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "order_table")
@Data
public class Order {

    @Id
    UUID orderId;

    UUID consumerId;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JsonManagedReference
    List<Item> items;

    String paymentType;

    Long value;

    @Enumerated(EnumType.STRING)
    PaymentStatus status;

    public Order() { }

}
