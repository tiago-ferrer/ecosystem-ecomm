package br.com.fiap.postech.adjt.checkout.infrastructure.gateways;

import br.com.fiap.postech.adjt.checkout.infrastructure.dtos.PaymentConsumerPayload;

public interface CheckoutEventGateway {
    default void sendCheckoutEvent(PaymentConsumerPayload paymentConsumerPayload) {

    }
}
