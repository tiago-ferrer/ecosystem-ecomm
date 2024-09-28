package br.com.fiap.postech.adjt.checkout.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;

import br.com.fiap.postech.adjt.checkout.clients.PaymentClient;
import br.com.fiap.postech.adjt.checkout.model.Order;
import br.com.fiap.postech.adjt.checkout.model.dto.request.PaymentFieldsRequest;
import br.com.fiap.postech.adjt.checkout.model.dto.request.PaymentMethodRequest;
import br.com.fiap.postech.adjt.checkout.model.dto.request.PaymentRequest;
import br.com.fiap.postech.adjt.checkout.model.dto.response.CheckoutResponse;
import br.com.fiap.postech.adjt.checkout.repository.OrderRepository;

class PaymentServiceImplTest {

    @Mock
    private PaymentClient paymentClient;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private Logger logger;

    @InjectMocks
    private PaymentServiceImpl paymentService;

    private Order order;
    private PaymentRequest paymentRequest;
    private CheckoutResponse checkoutResponse;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        order = new Order();
        order.setOrderId(UUID.randomUUID());
        order.setPaymentStatus("pending");

        // Create and set up the PaymentMethodRequest
        PaymentMethodRequest paymentMethod = new PaymentMethodRequest();
        paymentMethod.setType("credit_card");

        PaymentFieldsRequest paymentFields = new PaymentFieldsRequest();
        paymentFields.setNumber("1234567890123456");
        paymentFields.setExpiration_month("12");
        paymentFields.setExpiration_year("2025");
        paymentFields.setCvv("123");
        paymentFields.setName("John Doe");

        paymentMethod.setFields(paymentFields);

        // Set the PaymentMethodRequest in the PaymentRequest
        paymentRequest = new PaymentRequest();
        paymentRequest.setAmount(100);
        paymentRequest.setCurrency("BRL");
        paymentRequest.setPayment_method(paymentMethod);

        // Mock the response from the PaymentClient
        checkoutResponse = new CheckoutResponse();
        checkoutResponse.setStatus("approved");
    }

    @Test
    void testProcessPaymentSuccess() {
        // Simulate the behavior of the payment client
        when(paymentClient.processPayment(anyString(), any(PaymentRequest.class))).thenReturn(checkoutResponse);

        // Execute the payment processing method
        paymentService.process(order, paymentRequest);

        // Verify that the payment status was updated correctly
        assertEquals("approved", order.getPaymentStatus());

        // Verify that the save method of the repository was called
        verify(orderRepository, times(1)).save(order);

        // Verify that the log was called with the correct message
        verify(logger, times(1)).info("Payment processing result in order {}: {}", order.getOrderId(), "approved");
    }

    @Test
    void testProcessPaymentFailure() {
        // Simulate an exception in the payment client
        when(paymentClient.processPayment(anyString(), any(PaymentRequest.class))).thenThrow(new RuntimeException("Payment failed"));

        // Execute the payment processing method
        paymentService.process(order, paymentRequest);

        // Verify that the payment status was updated to "declined"
        assertEquals("declined", order.getPaymentStatus());

        // Verify that the save method of the repository was called
        verify(orderRepository, times(1)).save(order);

        // Verify that the log was called with the correct message
        verify(logger, times(1)).info("Payment processing result in order {}: declined", order.getOrderId());
        verify(logger, times(1)).error("error: ", "Payment failed");
    }
}