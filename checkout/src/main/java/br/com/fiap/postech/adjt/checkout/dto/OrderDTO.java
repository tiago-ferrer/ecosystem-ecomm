package br.com.fiap.postech.adjt.checkout.dto;

import br.com.fiap.postech.adjt.checkout.model.Currency;
import br.com.fiap.postech.adjt.checkout.model.Order;
import br.com.fiap.postech.adjt.checkout.model.PaymentMethodType;
import br.com.fiap.postech.adjt.checkout.model.PaymentStatus;

import java.util.List;
import java.util.UUID;

public record OrderDTO(
        UUID orderId,
        String customerId,
        List<ItemDTO> items,
        String currency,
        PaymentMethodType  paymentMethodType,
        Integer value,
        PaymentStatus paymentStatus

) {
}
