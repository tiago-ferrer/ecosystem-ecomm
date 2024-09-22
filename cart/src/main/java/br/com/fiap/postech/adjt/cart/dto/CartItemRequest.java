package br.com.fiap.postech.adjt.cart.dto;

import lombok.Data;
import java.util.UUID;

@Data
public class CartItemRequest {
    private UUID consumerId;
    private String itemId;
    private int quantity;
}
