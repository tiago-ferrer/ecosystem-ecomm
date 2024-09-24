package br.com.fiap.postech.adjt.cart.model.dto.response;

import java.math.BigDecimal;

public record ItemResponse(
        Long id,
        String title,
        BigDecimal price,
        String description,
        String category,
        String image
) {
}
