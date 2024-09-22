package br.com.fiap.postech.adjt.cart.domain.cart;

import java.util.Objects;
import java.util.UUID;

public record Consumer(
        String consumerId
) {

    public Consumer {
        if (Objects.isNull(consumerId)) {
            throw new IllegalArgumentException("EAN NAO PODE SER NULO OU MENOR E IGUAL A ZERO!");
        }
        try {
            UUID.fromString(consumerId).toString().equals(consumerId);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("EAN NAO PODE SER NULO OU MENOR E IGUAL A ZERO!");
        }
    }

}
