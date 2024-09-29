package br.com.fiap.postech.adjt.products.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class ProductDto {

    private Long id;
    private String title;
    private BigDecimal price;
    private String description;
    private String category;
    private String image;
    private RatingDto rating;

}
