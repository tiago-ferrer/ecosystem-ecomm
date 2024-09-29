package br.com.fiap.postech.adjt.checkout.domain.gateway;

import br.com.fiap.postech.adjt.checkout.domain.model.payment.CheckoutModel;

import java.util.UUID;

public interface PaymentGateway {
    void processPayment(CheckoutModel checkoutModel, UUID orderId);
}
