package br.com.fiap.postech.adjt.checkout.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Entity
@Table(name = "item_table")
@Data
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    Long itemId;

    Long qnt;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    @JsonBackReference
    Order order;

    public Item() { }

}
