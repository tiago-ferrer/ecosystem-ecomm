package br.com.fiap.postech.adjt.checkout.controller;

import br.com.fiap.postech.adjt.checkout.application.controller.OrderController;
import br.com.fiap.postech.adjt.checkout.application.controller.facade.OrderFacade;
import br.com.fiap.postech.adjt.checkout.application.dto.*;
import br.com.fiap.postech.adjt.checkout.domain.exception.AppException;
import br.com.fiap.postech.adjt.checkout.domain.model.enums.PaymentStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class OrderControllerTests {

    @InjectMocks
    private OrderController orderController;

    @Mock
    private OrderFacade orderFacade;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateOrder() throws AppException {
        CheckoutDTO checkoutDTO = new CheckoutDTO(
                "ec6f3e6b-cf54-49fb-915b-8079f651b161",
                1050.0,
                "BRL",
                new PaymentDTO(
                        "br_credit_card",
                        new FieldDTO(
                                "4111111111111111",
                                "12",
                                "25",
                                "255",
                                "John Doe"
                        )
                )
        );
        CheckoutResponseDTO checkoutResponseDTO = new CheckoutResponseDTO(
                "ec6f3e6b-cf54-49fb-915b-8079f651b161",
                "aproved"
        );

        when(orderFacade.createOrder(any(CheckoutDTO.class))).thenReturn(checkoutResponseDTO);

        CheckoutResponseDTO response = orderController.createOrder(checkoutDTO);

        assertEquals(checkoutResponseDTO, response);
    }

    @Test
    void testGetOrder() throws AppException {
        String orderId = "ec6f3e6b-cf54-49fb-915b-8079f651b161";
        OrderDTO orderDTO = new OrderDTO(
                "ec6f3e6b-cf54-49fb-915b-8079f651b161",
                "Credit_Card",
                1050.0,
                PaymentStatus.approved,
                List.of(new OrderItemDTO(
                        "123",
                        100L
                ))
        );
        when(orderFacade.getOrderById(orderId)).thenReturn(orderDTO);

        OrderDTO response = orderController.getOrder(orderId);

        assertEquals(orderDTO, response);
    }

    @Test
    void testGetOrderByCustomerId() throws AppException {
        String customerId = "456";
        OrderDTO order01 = new OrderDTO(
                "ec6f3e6b-cf54-49fb-915b-8079f651b161",
                "Credit_Card",
                1050.0,
                PaymentStatus.approved,
                List.of(new OrderItemDTO(
                        "123",
                        100L
                ))
        );

        OrderDTO order02 = new OrderDTO(
                "281937e1-349e-4d3c-81e1-f88953f54c97",
                "Credit_Card",
                1060.0,
                PaymentStatus.approved,
                List.of(new OrderItemDTO(
                        "123",
                        100L
                ))
        );
        List<OrderDTO> orders = Arrays.asList(order01, order02); // Preencha os campos necessários

        when(orderFacade.getOrderByCustomerId(customerId)).thenReturn(orders);

        List<OrderDTO> response = orderController.getOrderByCustomerId(customerId);

        assertEquals(orders, response);
    }
}