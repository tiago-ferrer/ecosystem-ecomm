package br.com.fiap.postech.adjt.cart.domain.Item;

import br.com.fiap.postech.adjt.cart.domain.cart.Cart;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Entity
@Table(name = "item_table")
@Data
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    UUID id;

    Long itemId;

    Long qnt;

    @ManyToOne
    @JoinColumn(name = "consumer_id", nullable = false)
    @JsonBackReference
    private Cart cart;

    public Item() { }

}
