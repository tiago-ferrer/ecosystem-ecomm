package br.com.fiap.postech.adjt.checkout.model;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class Cart {
    private UUID consumerId;
    private List<Item> items;
}
