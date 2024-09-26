package br.com.fiap.postech.adjt.cart.infrastructure.controller.dtos;

import java.util.List;

import br.com.fiap.postech.adjt.cart.domain.entity.ItemCart;

public record CartGetItemsResponseDto(List<ItemCart> items) {

}
