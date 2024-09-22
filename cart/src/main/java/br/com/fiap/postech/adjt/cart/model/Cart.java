package br.com.fiap.postech.adjt.cart.model;

import jakarta.persistence.*;

import java.util.UUID;
@Entity
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private UUID consumerId;
    private Long itemId;
    private int quantity;

    public Cart() {
    }
    public Cart(UUID consumerId, Long itemId, int quantity) {
        this.consumerId = consumerId;
        this.itemId = itemId;
        this.quantity = quantity;
    }

    public UUID getConsumerId() {
        return consumerId;
    }

    public void setConsumerId(UUID consumerId) {
        this.consumerId = consumerId;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

}
