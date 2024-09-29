package br.com.fiap.postech.adjt.checkout.domain.checkout;

import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

@Slf4j
public record CamposMetodoPagamento(
        String number,
        String expirationMonth,
        String expirationYear,
        String cvv,
        String name
) {

    public CamposMetodoPagamento {
        if (Objects.isNull(number) || number.isBlank()) {
            log.error("number NAO PODE SER NULO");
            throw new IllegalArgumentException("number NAO PODE SER NULO");
        }

        if (Objects.isNull(expirationMonth) || expirationMonth.isBlank() || expirationMonth.length() != 2) {
            log.error("expirationMonth NAO PODE SER NULO");
            throw new IllegalArgumentException("expirationMonth NAO PODE SER NULO");
        }

        if (Objects.isNull(expirationYear) || expirationYear.isBlank() || expirationYear.length() != 2) {
            log.error("expirationYear NAO PODE SER NULO");
            throw new IllegalArgumentException("expirationYear NAO PODE SER NULO");
        }

        if (Objects.isNull(cvv) || cvv.isBlank() || cvv.length() != 3) {
            log.error("cvv NAO PODE SER NULO");
            throw new IllegalArgumentException("cvv NAO PODE SER NULO");
        }

        if (Objects.isNull(name) || name.isBlank()) {
            log.error("name NAO PODE SER NULO");
            throw new IllegalArgumentException("name NAO PODE SER NULO");
        }

    }

}
