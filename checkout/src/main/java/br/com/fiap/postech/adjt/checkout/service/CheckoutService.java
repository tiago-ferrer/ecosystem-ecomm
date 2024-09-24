package br.com.fiap.postech.adjt.checkout.service;

import java.util.List;
import java.util.UUID;

import br.com.fiap.postech.adjt.checkout.model.OrderEntity;
import br.com.fiap.postech.adjt.checkout.model.request.PaymentRequest;
import br.com.fiap.postech.adjt.checkout.model.response.PaymentResponse;

public interface CheckoutService {

	PaymentResponse processCheckout(UUID consumerId, double amount, String currency,
			PaymentRequest.PaymentMethod paymentMethod);

	OrderEntity getOrderById(UUID orderId);

	List<OrderEntity> getOrdersByConsumerId(UUID consumerId);

}
