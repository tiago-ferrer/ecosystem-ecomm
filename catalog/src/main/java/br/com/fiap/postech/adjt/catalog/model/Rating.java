package br.com.fiap.postech.adjt.catalog.model;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Rating {

    private BigDecimal rate;
    private Integer count;

}
