package br.com.fiap.postech.adjt.gateway.product;

public record Product(
        Long id,
        String title,
        Double price,
        String description,
        String category,
        String image,
        ProductRating rating
) { }
