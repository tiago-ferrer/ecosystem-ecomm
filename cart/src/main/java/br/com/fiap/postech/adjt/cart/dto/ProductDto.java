package br.com.fiap.postech.adjt.cart.dto;

import java.math.BigDecimal;

public record ProductDto(Long id, String title, BigDecimal price, String description, String category, String image) {
}
