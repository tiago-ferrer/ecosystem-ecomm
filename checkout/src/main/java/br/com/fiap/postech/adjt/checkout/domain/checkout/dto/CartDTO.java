package br.com.fiap.postech.adjt.checkout.domain.checkout.dto;

import br.com.fiap.postech.adjt.checkout.domain.order.dto.ItemDTO;

import java.util.List;

public record CartDTO(
    List<ItemDTO> items
) { }
