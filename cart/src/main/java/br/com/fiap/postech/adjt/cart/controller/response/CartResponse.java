package br.com.fiap.postech.adjt.cart.controller.response;

import java.util.List;

public record CartResponse(
        List<ItemResponse> items
) { }
