package br.com.fiap.postech.adjt.checkout.service;

import java.util.List;
import java.util.UUID;

import br.com.fiap.postech.adjt.checkout.model.OrderEntity;
import br.com.fiap.postech.adjt.checkout.model.request.PaymentMethodRequest;
import br.com.fiap.postech.adjt.checkout.model.response.CheckoutResponse;
import br.com.fiap.postech.adjt.checkout.model.response.OrderCheckoutsResponse;

public interface CheckoutService {

	CheckoutResponse processCheckout(UUID consumerId, int amount, String currency,
			PaymentMethodRequest paymentMethod);

	List<OrderCheckoutsResponse> getOrdersByConsumerId(UUID consumerId);

	OrderEntity getOrderById(UUID orderId);

}
