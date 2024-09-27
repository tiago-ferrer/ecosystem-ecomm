package br.com.fiap.postech.adjt.products.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RatingDto {

    private BigDecimal rate;
    private Long count;

}
