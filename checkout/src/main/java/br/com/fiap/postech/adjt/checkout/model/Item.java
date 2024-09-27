package br.com.fiap.postech.adjt.checkout.model;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Embeddable
@Builder
public class Item {

    private Long itemId;

    private int qnt;
}
