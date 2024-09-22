package br.com.fiap.postech.adjt.cart.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cart {

    private UUID orderId;
    private UUID consumerId;
    private List<Item> items;
    private int quantity;

}
