package br.com.fiap.postech.adjt.checkout.domain.gateway;

import br.com.fiap.postech.adjt.checkout.domain.model.order.OrderStatusModel;
import br.com.fiap.postech.adjt.checkout.domain.model.payment.CheckoutModel;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface PaymentGateway {
    Mono<OrderStatusModel> processPayment(CheckoutModel checkoutModel, UUID orderId);
}
