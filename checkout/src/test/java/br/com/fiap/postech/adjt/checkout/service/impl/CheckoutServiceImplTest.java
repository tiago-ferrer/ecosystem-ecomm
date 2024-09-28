package br.com.fiap.postech.adjt.checkout.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import br.com.fiap.postech.adjt.checkout.clients.CartClient;
import br.com.fiap.postech.adjt.checkout.controller.exception.NotFoundException;
import br.com.fiap.postech.adjt.checkout.model.Order;
import br.com.fiap.postech.adjt.checkout.model.dto.request.ClearCartRequest;
import br.com.fiap.postech.adjt.checkout.model.dto.request.FindCartByCustomerIdRequest;
import br.com.fiap.postech.adjt.checkout.model.dto.request.PaymentMethodRequest;
import br.com.fiap.postech.adjt.checkout.model.dto.response.CartResponse;
import br.com.fiap.postech.adjt.checkout.model.dto.response.CheckoutResponse;
import br.com.fiap.postech.adjt.checkout.repository.OrderRepository;
import br.com.fiap.postech.adjt.checkout.service.PaymentService;

class CheckoutServiceImplTest {

	@Mock
	private OrderRepository orderRepository;

	@Mock
	private PaymentService paymentService;

	@Mock
	private CartClient cartClient;

	@InjectMocks
	private CheckoutServiceImpl checkoutService;

	private UUID consumerId;
	private PaymentMethodRequest paymentMethodRequest;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		consumerId = UUID.randomUUID();
		paymentMethodRequest = new PaymentMethodRequest();
		paymentMethodRequest.setType("credit_card");
		// Set other fields for paymentMethodRequest as needed
	}

	@Test
	void processCheckout_ShouldReturnCheckoutResponse_WhenOrderIsCreatedSuccessfully() {
		// Arrange
		Order order = new Order();
		order.setOrderId(UUID.randomUUID());
		order.setConsumerId(consumerId);
		order.setPaymentStatus("pending");

		when(orderRepository.save(any(Order.class))).thenReturn(order);
		when(cartClient.consult(any(FindCartByCustomerIdRequest.class))).thenReturn(new CartResponse(List.of()));

		// Act
		CheckoutResponse response = checkoutService.processCheckout(consumerId, 100, "USD", paymentMethodRequest);

		// Assert
		assertNotNull(response);
		assertEquals(order.getOrderId(), response.getOrderId());
		assertEquals("pending", response.getStatus());
		verify(cartClient, times(1)).clear(any(ClearCartRequest.class));
		verify(paymentService, times(1)).process(any(Order.class), any());
	}

	@Test
	void processCheckout_ShouldThrowNotFoundException_WhenCartItemsNotFound() {
		// Arrange
		when(cartClient.consult(any(FindCartByCustomerIdRequest.class)))
				.thenThrow(new NotFoundException("Cart items not found"));

		// Act & Assert
		assertThrows(NotFoundException.class,
				() -> checkoutService.processCheckout(consumerId, 100, "USD", paymentMethodRequest));
		verify(cartClient, never()).clear(any(ClearCartRequest.class));
		verify(paymentService, never()).process(any(Order.class), any());
	}

	@Test
	void getOrdersByConsumerId_ShouldReturnOrderCheckoutsResponseList_WhenOrdersExist() {
		// Arrange
		Order order = new Order();
		order.setOrderId(UUID.randomUUID());
		order.setConsumerId(consumerId);

		when(orderRepository.findByConsumerId(consumerId)).thenReturn(List.of(order));

		// Act
		var response = checkoutService.getOrdersByConsumerId(consumerId);

		// Assert
		assertNotNull(response);
		assertEquals(1, response.size());
		assertEquals(order.getOrderId(), response.get(0).orderId());
	}

	@Test
	void getOrderById_ShouldReturnOrderCheckoutsResponse_WhenOrderExists() {
		// Arrange
		UUID orderId = UUID.randomUUID();
		Order order = new Order();
		order.setOrderId(orderId);
		order.setConsumerId(consumerId);

		when(orderRepository.findById(orderId)).thenReturn(java.util.Optional.of(order));

		// Act
		var response = checkoutService.getOrderById(orderId);

		// Assert
		assertNotNull(response);
		assertEquals(orderId, response.orderId());
	}

	@Test
	void getOrderById_ShouldThrowNotFoundException_WhenOrderDoesNotExist() {
		// Arrange
		UUID orderId = UUID.randomUUID();
		when(orderRepository.findById(orderId)).thenReturn(java.util.Optional.empty());

		// Act & Assert
		assertThrows(NotFoundException.class, () -> checkoutService.getOrderById(orderId));
	}

	@Test
	void processCheckout_ShouldCallCartClientClear_WhenCartExists() {
		// Arrange
		UUID consumerId = UUID.randomUUID();
		PaymentMethodRequest paymentMethod = new PaymentMethodRequest();
		// Set up other necessary mocks and data

		// Act
		checkoutService.processCheckout(consumerId, 100, "USD", paymentMethod);

		// Assert
		verify(cartClient, times(1)).clear(any(ClearCartRequest.class));
	}

	@Test
	void processCheckout_ShouldThrowNotFoundException_WhenCartClientThrowsException() {
		// Arrange
		UUID consumerId = UUID.randomUUID();
		PaymentMethodRequest paymentMethod = new PaymentMethodRequest();
		doThrow(new NotFoundException("Empty cart")).when(cartClient).clear(any(ClearCartRequest.class));

		// Act & Assert
		assertThrows(NotFoundException.class,
				() -> checkoutService.processCheckout(consumerId, 100, "USD", paymentMethod));
	}
}