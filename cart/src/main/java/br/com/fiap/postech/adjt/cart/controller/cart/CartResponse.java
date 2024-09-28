package br.com.fiap.postech.adjt.cart.controller.cart;

import br.com.fiap.postech.adjt.cart.controller.item.ItemResponse;

import java.util.List;

public record CartResponse(
        List<ItemResponse> items
) { }
