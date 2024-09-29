package br.com.fiap.postech.adjt.cart.dto;

import java.util.List;

public record CartResponse(List<ItemResponse> items) {
}
