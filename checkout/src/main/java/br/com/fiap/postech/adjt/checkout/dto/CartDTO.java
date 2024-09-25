package br.com.fiap.postech.adjt.checkout.dto;

import java.util.List;

public record CartDTO(
    List<ItemDTO> items
) { }
