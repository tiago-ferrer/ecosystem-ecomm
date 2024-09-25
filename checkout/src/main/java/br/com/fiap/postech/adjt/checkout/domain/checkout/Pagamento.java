package br.com.fiap.postech.adjt.checkout.domain.checkout;

import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

@Slf4j
public record Pagamento(
        String consumerId,
        Long amount,
        String currency

) {

    public Pagamento {
        if (Objects.isNull(amount) || amount <= 0) {
            log.error("amount NAO PODE SER NULO OU MENOR OU IGUAL A ZERO");
            throw new IllegalArgumentException("amount NAO PODE SER NULO OU MENOR OU IGUAL A ZERO");
        }

        if (Objects.isNull(currency) || currency.isBlank()) {
            log.error("currency NAO PODE SER NULO");
            throw new IllegalArgumentException("currency NAO PODE SER NULO");
        }

    }

}
