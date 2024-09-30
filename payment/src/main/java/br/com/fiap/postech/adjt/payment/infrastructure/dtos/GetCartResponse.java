package br.com.fiap.postech.adjt.payment.infrastructure.dtos;

import br.com.fiap.postech.adjt.payment.domain.entities.Item;

import java.util.List;

public record GetCartResponse(List<Item> items) {
}
