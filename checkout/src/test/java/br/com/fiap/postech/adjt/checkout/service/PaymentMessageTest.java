package br.com.fiap.postech.adjt.checkout.service;

import br.com.fiap.postech.adjt.checkout.model.Checkout;
import br.com.fiap.postech.adjt.checkout.model.Order;
import br.com.fiap.postech.adjt.checkout.model.PaymentMessage;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PaymentMessageTest {

    @Test
    void testPaymentMessageGettersAndSetters() {

        Order order = new Order();
        Checkout checkout = new Checkout();

        order.setValue(100);
        checkout.setConsumerId(UUID.randomUUID());

        PaymentMessage paymentMessage = new PaymentMessage();

        paymentMessage.setOrder(order);
        paymentMessage.setCheckout(checkout);

        assertEquals(order, paymentMessage.getOrder());
        assertEquals(checkout, paymentMessage.getCheckout());
    }
}