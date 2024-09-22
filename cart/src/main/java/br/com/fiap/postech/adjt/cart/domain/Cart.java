package br.com.fiap.postech.adjt.cart.domain;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "carts")
public class Cart {
    private UUID consumerId;
    private List<Item> items = new ArrayList<>();

    // Construtor adicional se necessário
    public Cart(UUID consumerId) {
        this.consumerId = consumerId;
        this.items = new ArrayList<>();
    }
}