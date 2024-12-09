package br.com.fiap.postech.adjt.checkout.domain.gateway;

import br.com.fiap.postech.adjt.checkout.domain.model.order.OrderModel;

public interface PaymentGateway {
    void sendPayment(OrderModel orderModel);
}
