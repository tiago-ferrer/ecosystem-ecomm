package br.com.fiap.postech.adjt.gateway.domain.product;

public record Product(
        Long id,
        String title,
        Double price,
        String description,
        String category,
        String image,
        ProductRating rating
) { }
