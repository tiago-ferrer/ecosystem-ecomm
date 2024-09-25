package br.com.fiap.postech.adjt.cart.domain.entity;

import java.util.List;
import java.util.UUID;

public record Cart(UUID consumerId, List<ItemCart> itemsCart) {

}
