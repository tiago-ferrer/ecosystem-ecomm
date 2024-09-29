package br.com.fiap.postech.adjt.checkout.domain.checkout;

import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

@Slf4j
public record MetodoPagamento(
        String type
) {

    public MetodoPagamento {
        if (Objects.isNull(type) || type.isBlank()) {
            log.error("type NAO PODE SER NULO");
            throw new IllegalArgumentException("type NAO PODE SER NULO");
        }
    }

}
