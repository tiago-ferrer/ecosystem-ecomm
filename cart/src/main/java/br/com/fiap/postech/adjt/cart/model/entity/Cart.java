package br.com.fiap.postech.adjt.cart.model.entity;

import jakarta.persistence.*;

import java.util.List;
import java.util.UUID;

@Entity(name = "carts")
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private UUID customerId;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> items;

    public Cart() {
    }
}
