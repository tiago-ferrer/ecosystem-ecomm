package br.com.fiap.postech.adjt.checkout.controller.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import br.com.fiap.postech.adjt.checkout.model.dto.request.CheckoutRequest;
import br.com.fiap.postech.adjt.checkout.model.dto.request.PaymentMethodRequest;
import br.com.fiap.postech.adjt.checkout.model.dto.response.CheckoutResponse;
import br.com.fiap.postech.adjt.checkout.model.dto.response.OrderCheckoutsResponse;
import br.com.fiap.postech.adjt.checkout.service.CheckoutService;

public class CheckoutControllerImplTest {

    @Mock
    private CheckoutService checkoutService;

    @InjectMocks
    private CheckoutControllerImpl checkoutController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateCheckout() {
        CheckoutRequest checkoutRequest = new CheckoutRequest();
        checkoutRequest.setConsumerId(UUID.randomUUID().toString());
        checkoutRequest.setAmount(100);
        checkoutRequest.setCurrency("USD");

        // Create a PaymentMethodRequest object
        PaymentMethodRequest paymentMethodRequest = new PaymentMethodRequest();
        checkoutRequest.setPaymentMethod(paymentMethodRequest);

        CheckoutResponse checkoutResponse = new CheckoutResponse();
        when(checkoutService.processCheckout(any(UUID.class), anyInt(), anyString(), any(PaymentMethodRequest.class)))
                .thenReturn(checkoutResponse);

        ResponseEntity<CheckoutResponse> response = checkoutController.createCheckout(checkoutRequest);

        assertEquals(ResponseEntity.ok(checkoutResponse), response);
        verify(checkoutService, times(1)).processCheckout(any(UUID.class), anyInt(), anyString(), any(PaymentMethodRequest.class));
    }

    @Test
    public void testGetOrdersByConsumerId() {
        UUID consumerId = UUID.randomUUID();
        UUID orderId = UUID.randomUUID();
        OrderCheckoutsResponse order1 = new OrderCheckoutsResponse(orderId, null, "Credit Card", 100, "Paid");
        OrderCheckoutsResponse order2 = new OrderCheckoutsResponse(orderId, null, "Credit Card", 200, "Paid");

        List<OrderCheckoutsResponse> orders = Arrays.asList(order1, order2);
        when(checkoutService.getOrdersByConsumerId(consumerId)).thenReturn(orders);

        ResponseEntity<List<OrderCheckoutsResponse>> response = checkoutController.getOrdersByConsumerId(consumerId);

        assertEquals(ResponseEntity.ok(orders), response);
        verify(checkoutService, times(1)).getOrdersByConsumerId(consumerId);
    }

    @Test
    public void testGetOrderById() {
        UUID orderId = UUID.randomUUID();
        OrderCheckoutsResponse orderCheckoutsResponse = new OrderCheckoutsResponse(orderId, null, "Credit Card", 100, "Paid");
        when(checkoutService.getOrderById(orderId)).thenReturn(orderCheckoutsResponse);

        ResponseEntity<OrderCheckoutsResponse> response = checkoutController.getOrderById(orderId);

        assertEquals(ResponseEntity.ok(orderCheckoutsResponse), response);
        verify(checkoutService, times(1)).getOrderById(orderId);
    }
}