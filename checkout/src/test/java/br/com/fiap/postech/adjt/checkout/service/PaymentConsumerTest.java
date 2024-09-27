package br.com.fiap.postech.adjt.checkout.service;

import br.com.fiap.postech.adjt.checkout.dto.CheckoutResponseDTO;
import br.com.fiap.postech.adjt.checkout.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PaymentConsumerTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private OrderService orderService;

    @Mock
    private CartService cartService;

    @InjectMocks
    private PaymentConsumer paymentConsumer;
    private Order order;
    private Currency currency;

    private String paymentServiceUrl = "http://mocked-payment-service-url.com";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        paymentConsumer = new PaymentConsumer(restTemplate, orderService, paymentServiceUrl, cartService);

        currency = Currency.BRL;

        order = new Order();
        order.setCurrency(currency);
    }


    @Test
    void consumePayment_shouldApprovePaymentAndClearCart() {
        UUID orderId = UUID.randomUUID();
        Order order = new Order();
        order.setOrderId(orderId);
        order.setTotalValue(100.0);
        order.setPaymentStatus(PaymentStatus.pending);
        order.setCurrency(Currency.BRL);

        Checkout checkout = new Checkout();
        checkout.setConsumerId(UUID.randomUUID());

        PaymentMessage paymentMessage = mock(PaymentMessage.class);
        when(paymentMessage.getOrder()).thenReturn(order);
        when(paymentMessage.getCheckout()).thenReturn(checkout);

        CheckoutResponseDTO responseDto = new CheckoutResponseDTO("Payment approved", PaymentStatus.approved);
        ResponseEntity<CheckoutResponseDTO> responseEntity = ResponseEntity.ok(responseDto);

        when(restTemplate.postForEntity(any(String.class), any(HttpEntity.class), eq(CheckoutResponseDTO.class)))
                .thenReturn(responseEntity);

        paymentConsumer.consumePayment(paymentMessage);

        assertEquals(PaymentStatus.approved, order.getPaymentStatus());
        verify(cartService).clearCart(checkout.getConsumerId());
        verify(orderService).updateOrder(order);
    }

    @Test
    void consumePayment_shouldDeclinePayment() {
        UUID orderId = UUID.randomUUID();
        Order order = new Order();
        order.setOrderId(orderId);
        order.setTotalValue(100.0);
        order.setPaymentStatus(PaymentStatus.pending);
        order.setCurrency(Currency.BRL);

        Checkout checkout = new Checkout();
        checkout.setConsumerId(UUID.randomUUID());

        PaymentMessage paymentMessage = mock(PaymentMessage.class);
        when(paymentMessage.getOrder()).thenReturn(order);
        when(paymentMessage.getCheckout()).thenReturn(checkout);

        CheckoutResponseDTO responseDto = new CheckoutResponseDTO("Payment declined", PaymentStatus.declined);
        ResponseEntity<CheckoutResponseDTO> responseEntity = ResponseEntity.ok(responseDto);

        when(restTemplate.postForEntity(any(String.class), any(HttpEntity.class), eq(CheckoutResponseDTO.class)))
                .thenReturn(responseEntity);

        paymentConsumer.consumePayment(paymentMessage);

        assertEquals(PaymentStatus.declined, order.getPaymentStatus());
        verify(cartService, never()).clearCart(any());
        verify(orderService).updateOrder(order);
    }

}