package br.com.fiap.postech.adjt.products.service;

import br.com.fiap.postech.adjt.products.dto.ProductDto;
import br.com.fiap.postech.adjt.products.dto.RatingDto;

import java.math.BigDecimal;

public class TestUtils {

    public static ProductDto buildProductDto() {
        ProductDto productDto = new ProductDto();
        productDto.setId(1L);
        productDto.setTitle("Fjallraven");
        productDto.setPrice(BigDecimal.valueOf(109.95));
        productDto.setDescription("Your perfect pack");
        productDto.setCategory("men's clothing");
        productDto.setImage("https://fakestoreapi.com/img/img_product_1.jpg");
        productDto.setRating(new RatingDto(BigDecimal.valueOf(3.9), 120L));
        return productDto;
    }

}
