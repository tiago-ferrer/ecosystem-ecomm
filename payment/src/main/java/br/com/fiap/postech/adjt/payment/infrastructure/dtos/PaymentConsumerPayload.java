package br.com.fiap.postech.adjt.payment.infrastructure.dtos;


import java.util.UUID;

public record PaymentConsumerPayload(

        UUID consumerId,
        UUID orderId
) {
}