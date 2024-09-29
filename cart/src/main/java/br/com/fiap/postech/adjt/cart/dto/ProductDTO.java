package br.com.fiap.postech.adjt.cart.dto;

import java.math.BigDecimal;

public record ProductDTO(String id, String name, BigDecimal price, int stock) {}

