package br.com.fiap.postech.adjt.checkout.service;

import java.util.List;
import java.util.UUID;

import br.com.fiap.postech.adjt.checkout.model.Order;
import br.com.fiap.postech.adjt.checkout.model.dto.request.PaymentMethodRequest;
import br.com.fiap.postech.adjt.checkout.model.dto.response.CheckoutResponse;
import br.com.fiap.postech.adjt.checkout.model.dto.response.OrderCheckoutsResponse;

public interface CheckoutService {

	CheckoutResponse processCheckout(UUID consumerId, int amount, String currency,
			PaymentMethodRequest paymentMethod);

	List<OrderCheckoutsResponse> getOrdersByConsumerId(UUID consumerId);

	OrderCheckoutsResponse getOrderById(UUID orderId);
}
