package br.com.fiap.postech.adjt.checkout.infrastructure.dtos;


import br.com.fiap.postech.adjt.checkout.domain.entities.Item;

import java.util.List;

public record GetCartResponse(List<Item> items) {
}
