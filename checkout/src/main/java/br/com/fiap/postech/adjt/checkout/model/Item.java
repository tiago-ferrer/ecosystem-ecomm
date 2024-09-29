package br.com.fiap.postech.adjt.checkout.model;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Embeddable
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Item {

    private Long itemId;

    private int qnt;
}
