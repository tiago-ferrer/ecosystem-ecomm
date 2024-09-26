package br.com.fiap.postech.adjt.checkout.infrastructure.gateways;

import br.com.fiap.postech.adjt.checkout.infrastructure.dtos.CheckoutRequest;

public interface CheckoutEventGateway {
    default void sendCheckoutEvent(CheckoutRequest checkoutRequest) {

    }
}
