package br.com.fiap.postech.adjt.checkout.domain.entities;


import br.com.fiap.postech.adjt.checkout.domain.entities.enums.OrderStatus;

import java.util.List;

public record Order(
        List<Item> items,
        String paymentType,
        OrderStatus paymentStatus,
        Integer value,
        String fieldsNumber,
        String fieldsExpirationMonth,
        String fieldsExpirationYear,
        String cvv,
        String name
) {
}
