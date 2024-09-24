package br.com.fiap.postech.adjt.catalog.model;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Product {

    private Long id;
    private String title;
    private BigDecimal price;
    private String description;
    private String category;
    private String image;
    private Rating rating;

}
