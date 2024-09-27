package br.com.fiap.postech.adjt.cart.domain.model;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ItemCart {

    private UUID consumerId;

    private String itemId;

    private Integer quantity;

}
