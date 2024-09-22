package br.com.fiap.postech.adjt.cart.model;

import org.springframework.data.annotation.Id;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Item {
    private String productId;
    private int quantity;
}
