package br.com.fiap.postech.adjt.cart.domain.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Cart {

    private UUID consumerId;

    private List<ItemCart> itemsCart = new ArrayList<>();
}
