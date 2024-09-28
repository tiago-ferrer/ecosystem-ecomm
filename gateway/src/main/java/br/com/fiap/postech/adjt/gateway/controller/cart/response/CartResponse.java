package br.com.fiap.postech.adjt.gateway.controller.cart.response;

import java.util.List;

public record CartResponse(
        List<ItemResponse> items
) { }
