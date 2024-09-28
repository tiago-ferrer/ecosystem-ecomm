package br.com.fiap.postech.adjt.checkout.service.impl;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.ArgumentMatchers.eq;
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
import org.slf4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.fiap.postech.adjt.checkout.model.Card;
import br.com.fiap.postech.adjt.checkout.model.Order;
import br.com.fiap.postech.adjt.checkout.model.dto.request.PaymentRequest;
import br.com.fiap.postech.adjt.checkout.repository.OrderRepository;
import br.com.fiap.postech.adjt.checkout.service.PaymentService;

class DataInitializerTest {

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private PaymentService paymentService;

    @Mock
    private Logger logger;

    @InjectMocks
    private DataInitializer dataInitializer;

    private Order order;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Criando uma ordem de exemplo
        order = new Order();
        order.setOrderId(UUID.randomUUID());
        order.setValue(100);	
        order.setPaymentStatus("pending");

        // Configurando o cartão de crédito
        order.setCard(new Card(UUID.randomUUID(), UUID.randomUUID(), "1234567890123456", "12", "2025", "123", "John Doe"));
    }

    @Test
    void shouldProcessPendingOrders_WhenOrdersExist() throws Exception {
        // Arrange
        when(orderRepository.findByPaymentStatus("pending")).thenReturn(List.of(order));
        when(objectMapper.writeValueAsString(any(PaymentRequest.class))).thenReturn("{\"orderId\":\"123\"}");

        // Act
        dataInitializer.init();

        // Assert
        verify(orderRepository, times(1)).findByPaymentStatus("pending");
        verify(paymentService, times(1)).process(eq(order), any(PaymentRequest.class));
        verify(logger, times(1)).info(contains("PaymentRequest Payload (JSON):"), anyString());
    }
    
    @Test
    void shouldLogError_WhenSerializationFails() throws Exception {
        // Arrange
        when(orderRepository.findByPaymentStatus("pending")).thenReturn(List.of(order));
        when(objectMapper.writeValueAsString(any(PaymentRequest.class))).thenThrow(new RuntimeException("Serialization error"));

        // Act
        dataInitializer.init();

        // Assert
        verify(orderRepository, times(1)).findByPaymentStatus("pending");
        verify(paymentService, times(1)).process(eq(order), any(PaymentRequest.class));
        verify(logger, times(1)).error(contains("Failed to serialize PaymentRequest"), anyString());
    }

    @Test
    void shouldNotProcessOrders_WhenNoPendingOrdersExist() {
        // Arrange
        when(orderRepository.findByPaymentStatus("pending")).thenReturn(List.of());

        // Act
        dataInitializer.init();

        // Assert
        verify(orderRepository, times(1)).findByPaymentStatus("pending");
        verify(paymentService, never()).process(any(Order.class), any(PaymentRequest.class));
        verify(logger, never()).info(anyString(), anyString());
    }
}