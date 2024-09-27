package br.com.fiap.postech.adjt.checkout.model;

import jakarta.persistence.Embeddable;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Embeddable
public class Item {

    private Long itemId;

    private int qnt;
}
