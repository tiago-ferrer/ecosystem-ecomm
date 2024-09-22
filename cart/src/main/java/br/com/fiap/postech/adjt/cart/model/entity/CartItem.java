package br.com.fiap.postech.adjt.cart.model.entity;

import jakarta.persistence.*;

@Entity(name = "cart_items")
class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long itemId;

    private Integer quantity;

    @ManyToOne
    @JoinColumn(name = "cart_id")
    private Cart cart;

    public CartItem() {
    }
}
