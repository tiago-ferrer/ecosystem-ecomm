package br.com.fiap.postech.adjt.cart.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RatingDto {

    private BigDecimal rate;
    private Long count;

}
