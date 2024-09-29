package br.com.fiap.postech.adjt.cart.domain.cart;

import lombok.extern.slf4j.Slf4j;

import java.util.Objects;
import java.util.UUID;

@Slf4j
public record Consumer(
        String consumerId
) {

    public Consumer {
        if (Objects.isNull(consumerId)) {
            log.error("consumerId NAO PODE SER NULO");
            throw new IllegalArgumentException("consumerId NAO PODE SER NULO");
        }
        try {
            UUID.fromString(consumerId).toString().equals(consumerId);
        } catch (IllegalArgumentException e) {
            log.error("consumerId TEM QUE TER O FORMATO DE UUID");
            throw new IllegalArgumentException("consumerId TEM QUE TER O FORMATO DE UUID");
        }
    }

}
