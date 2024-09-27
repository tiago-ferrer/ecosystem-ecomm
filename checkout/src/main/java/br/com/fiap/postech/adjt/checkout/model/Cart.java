package br.com.fiap.postech.adjt.checkout.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@Builder
public class Cart {
    private UUID consumerId;
    private List<Item> itemList;
}
