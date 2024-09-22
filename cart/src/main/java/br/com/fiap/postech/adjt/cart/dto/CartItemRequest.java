package br.com.fiap.postech.adjt.cart.dto;

import lombok.Data;

@Data
public class CartItemRequest {
    private String consumerId;
    private String itemId;
    private int quantity;
}
