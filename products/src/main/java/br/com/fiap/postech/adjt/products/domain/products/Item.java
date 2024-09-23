package br.com.fiap.postech.adjt.products.domain.products;

import lombok.extern.slf4j.Slf4j;

import java.util.Objects;
import java.util.UUID;

@Slf4j
public record Item(
        Long quantity
) {

    public Item {
        if (Objects.isNull(quantity) || quantity <= 0) {
            log.error("quantity NAO PODE SER NULO OU MENOR OU IGUAL QUE ZERO");
            throw new IllegalArgumentException("quantity não é um numero valido");
        }

    }

}
