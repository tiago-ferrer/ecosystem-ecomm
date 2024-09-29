package br.com.fiap.postech.adjt.checkout.controller;

import br.com.fiap.postech.adjt.checkout.dto.*;
import br.com.fiap.postech.adjt.checkout.model.PaymentMethodType;
import br.com.fiap.postech.adjt.checkout.model.PaymentStatus;
import br.com.fiap.postech.adjt.checkout.service.CheckoutService;
import br.com.fiap.postech.adjt.checkout.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class CheckoutControllerTest {

    @InjectMocks
    private CheckoutController checkoutController;

    @Mock
    private CheckoutService checkoutService;

    @Mock
    private OrderService orderService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testProcessPayment() {

        PaymentMethodFieldsRequestDTO fields = PaymentMethodFieldsRequestDTO.builder()
                .number("4111111111111111")
                .expiration_month("12")
                .expiration_year("2025")
                .cvv("123")
                .name("John Doe")
                .build();

        PaymentMethodRequestDTO paymentMethod = PaymentMethodRequestDTO.builder()
                .type(PaymentMethodType.br_credit_card)
                .fields(fields)
                .build();

        CheckoutRequestDTO request = CheckoutRequestDTO.builder()
                .consumerId("consumer-uuid")
                .amount(100)
                .currency("USD")
                .paymentMethod(paymentMethod)
                .build();

        CheckoutResponseDTO response = CheckoutResponseDTO.builder()
                .orderId("order-123")
                .status(PaymentStatus.approved)
                .build();

        when(checkoutService.processPayment(any(CheckoutRequestDTO.class))).thenReturn(response);

        CheckoutResponseDTO result = checkoutController.processPayment(request);

        verify(checkoutService).processPayment(request);
        assertEquals(response, result);
    }

    @Test
    public void testSearchPaymentByOrderId() {

        UUID orderId = UUID.randomUUID();
        String customerId = "customer-uuid";

        ItemDTO item1 = new ItemDTO(1L, 2);
        ItemDTO item2 = new ItemDTO(2L, 3);
        List<ItemDTO> items = Arrays.asList(item1, item2);

        String currency = "USD";
        PaymentMethodType paymentMethodType = PaymentMethodType.br_credit_card;
        Integer value = 100;
        PaymentStatus paymentStatus = PaymentStatus.approved;

        OrderDTO order = new OrderDTO(
                orderId,
                customerId,
                items,
                currency,
                paymentMethodType,
                value,
                paymentStatus
        );

        when(orderService.findByOrderId(orderId.toString())).thenReturn(order);

        OrderDTO result = checkoutController.searchPaymentByOrderId(orderId.toString());

        verify(orderService).findByOrderId(orderId.toString());
        assertEquals(order, result);
    }
    @Test
    public void testSearchPaymentByConsumer() {

        String consumerId = "consumer123";

        UUID orderId1 = UUID.randomUUID();
        OrderDTO order1 = new OrderDTO(
                orderId1,
                consumerId,
                Arrays.asList(new ItemDTO(1L, 2)),
                "USD",
                PaymentMethodType.br_credit_card,
                100,
                PaymentStatus.approved
        );

        UUID orderId2 = UUID.randomUUID();
        OrderDTO order2 = new OrderDTO(
                orderId2,
                consumerId,
                Arrays.asList(new ItemDTO(2L, 3)),
                "USD",
                PaymentMethodType.br_credit_card,
                200,
                PaymentStatus.pending
        );

        List<OrderDTO> expectedOrders = Arrays.asList(order1, order2);

        when(orderService.findPaymentByConsumer(consumerId)).thenReturn(expectedOrders);

        List<OrderDTO> result = checkoutController.searchPaymentByConsumer(consumerId);

        verify(orderService).findPaymentByConsumer(consumerId);
        assertEquals(expectedOrders, result);
    }
}