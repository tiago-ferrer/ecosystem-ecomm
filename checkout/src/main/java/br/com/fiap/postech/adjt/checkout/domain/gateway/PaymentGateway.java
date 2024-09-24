package br.com.fiap.postech.adjt.checkout.domain.gateway;

import br.com.fiap.postech.adjt.checkout.domain.model.order.OrderModel;
import br.com.fiap.postech.adjt.checkout.domain.model.payment.CheckoutModel;

public interface PaymentGateway {
    void getPayment(CheckoutModel checkoutModel, String orderId);
}
