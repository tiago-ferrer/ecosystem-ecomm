package br.com.fiap.postech.adjt.checkout.infrastructure.dtos;


import java.util.UUID;

public record PaymentConsumerPayload(
        UUID consumerId,
        UUID orderId
) {
}
