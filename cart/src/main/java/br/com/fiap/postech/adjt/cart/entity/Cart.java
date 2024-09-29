package br.com.fiap.postech.adjt.cart.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

import java.util.*;

@Entity
@Table(name = "cart_table")
@Data
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID consumerId;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JsonManagedReference
    private List<Item> items;

    public Cart() { }

}
