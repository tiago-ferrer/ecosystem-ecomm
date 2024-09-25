package br.com.fiap.postech.adjt.cart.infrastructure.controller;

import java.util.List;

import br.com.fiap.postech.adjt.cart.domain.entity.ItemCart;

public record CartItemsResponse(List<ItemCart> items) {

}
