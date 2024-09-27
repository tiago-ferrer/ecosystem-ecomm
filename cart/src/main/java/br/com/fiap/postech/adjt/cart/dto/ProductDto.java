package br.com.fiap.postech.adjt.cart.dto;

import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;

@Data
@ToString
public class ProductDto {

    private Long id;
    private String title;
    private BigDecimal price;
    private String description;
    private String category;
    private String image;
    private RatingDto rating;

}
