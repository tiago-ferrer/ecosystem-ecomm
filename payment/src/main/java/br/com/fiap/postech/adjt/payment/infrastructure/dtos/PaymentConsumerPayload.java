package br.com.fiap.postech.adjt.payment.infrastructure.dtos;


public record PaymentConsumerPayload(

        String consumerId,
        String orderId
) {
}
