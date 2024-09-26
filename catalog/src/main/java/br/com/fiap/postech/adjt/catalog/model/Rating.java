package br.com.fiap.postech.adjt.catalog.model;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@RequiredArgsConstructor
@Builder
public class Rating {

    private final BigDecimal rate;
    private final Integer count;

}
