package br.com.fiap.postech.adjt.checkout.model;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Embeddable
public class Item {

    private Long itemId;
    private int quantity;
    private Double price;
}
