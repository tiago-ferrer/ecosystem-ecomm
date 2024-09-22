package br.com.fiap.postech.adjt.cart.model;

import org.springframework.data.annotation.Id;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Item {
    @Id
    private String id; // ID do item no MongoDB
    private String productId;
    private int quantity;
}
