package br.com.fiap.postech.adjt.checkout.service.impl;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import br.com.fiap.postech.adjt.checkout.model.dto.request.ClearCartRequest;
import br.com.fiap.postech.adjt.checkout.model.dto.request.FindCartByCustomerIdRequest;
import org.springframework.stereotype.Service;

import br.com.fiap.postech.adjt.checkout.clients.CartClient;
import br.com.fiap.postech.adjt.checkout.clients.PaymentClient;
import br.com.fiap.postech.adjt.checkout.controller.exception.NotFoundException;
import br.com.fiap.postech.adjt.checkout.mapper.OrderMapper;
import br.com.fiap.postech.adjt.checkout.model.CardEntity;
import br.com.fiap.postech.adjt.checkout.model.CartItemEntity;
import br.com.fiap.postech.adjt.checkout.model.OrderEntity;
import br.com.fiap.postech.adjt.checkout.model.dto.request.ClearCartRequest;
import br.com.fiap.postech.adjt.checkout.model.dto.request.FindCartByCustomerIdRequest;
import br.com.fiap.postech.adjt.checkout.model.dto.response.CartResponse;
import br.com.fiap.postech.adjt.checkout.model.request.PaymentMethodRequest;
import br.com.fiap.postech.adjt.checkout.model.request.PaymentRequest;
import br.com.fiap.postech.adjt.checkout.model.response.CheckoutResponse;
import br.com.fiap.postech.adjt.checkout.model.response.OrderCheckoutsResponse;
import br.com.fiap.postech.adjt.checkout.repository.OrderRepository;
import br.com.fiap.postech.adjt.checkout.service.CheckoutService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CheckoutServiceImpl implements CheckoutService {

	private final OrderRepository orderRepository;
	private final PaymentClient paymentClient;
	private final CartClient cartClient;

    private final String apiKey = System.getenv("API_KEY");

	@Transactional
	public CheckoutResponse processCheckout(UUID consumerId, int amount, String currency,
			PaymentMethodRequest paymentMethod) {

		OrderEntity order = createPendingOrder(consumerId, amount, paymentMethod);
		PaymentRequest paymentRequest = new PaymentRequest(order.getOrderId().toString(), amount, currency,
				paymentMethod);
		CompletableFuture.runAsync(() -> processPaymentAsync(order, paymentRequest));
//		clearCart(consumerId);
		return new CheckoutResponse(order.getConsumerId().toString(), order.getPaymentStatus());
	}

	private OrderEntity createPendingOrder(UUID consumerId, int amount, PaymentMethodRequest paymentMethod) {
		OrderEntity order = new OrderEntity();
		CardEntity cardEntity = new CardEntity();

		order.setConsumerId(consumerId);
		List<CartItemEntity> cartItems = fetchCartItems(consumerId);
		order.setItems(cartItems);
		order.setPaymentType(paymentMethod.getType());
		order.setValue(amount);
		order.setPaymentStatus("pending");

		cardEntity.setConsumerId(consumerId);
		cardEntity.setNumber(paymentMethod.getFields().getNumber());
		cardEntity.setExpiration_month(paymentMethod.getFields().getExpiration_month());
		cardEntity.setExpiration_year(paymentMethod.getFields().getExpiration_year());
		cardEntity.setCvv(paymentMethod.getFields().getCvv());
		cardEntity.setName(paymentMethod.getFields().getName());

		order.setCard(cardEntity);
		return orderRepository.save(order);
	}

	private void processPaymentAsync(OrderEntity order, PaymentRequest paymentRequest) {
		try {
			CheckoutResponse paymentResponse = paymentClient.processPayment(apiKey, paymentRequest);
			order.setPaymentStatus(paymentResponse.getStatus());
			System.out.println(paymentResponse.getStatus());
		} catch (Exception e) {
			order.setPaymentStatus("declined");
		} finally {
			orderRepository.save(order);
		}
	}

	private void clearCart(UUID consumerId) {
		try {
			cartClient.clear(new ClearCartRequest(consumerId.toString()));
			System.out.println("Carrinho limpo");
		} catch (Exception e) {
			throw new NotFoundException("Empty cart: " + consumerId);
		}
	}

	private List<CartItemEntity> fetchCartItems(UUID consumerId) {
		try {
			FindCartByCustomerIdRequest findCartByCustomerIdRequest = new FindCartByCustomerIdRequest(consumerId.toString());
			CartResponse cartResponse = cartClient.consult(findCartByCustomerIdRequest);

			if (cartResponse != null && cartResponse.items() != null) {
				return cartResponse.items().stream()
						.map(item -> new CartItemEntity(item.itemId(), item.qnt())).toList();
			} else {
				return List.of();
			}
		} catch (Exception e) {
			throw new NotFoundException("Cart items not found for consumerId: " + consumerId);
		}
	}

	@Override
	public List<OrderCheckoutsResponse> getOrdersByConsumerId(UUID consumerId) {
		List<OrderEntity> orderEntities2 = orderRepository.findByConsumerId(consumerId);
		return OrderMapper.toResponseList(orderEntities2);
	}

	@Override
	public OrderEntity getOrderById(UUID orderId) {
		return orderRepository.findById(orderId)
				.orElseThrow(() -> new NotFoundException("Order not found for orderId: " + orderId));
	}
}