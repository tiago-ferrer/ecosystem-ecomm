package br.com.fiap.postech.adjt.checkout.controller;

import br.com.fiap.postech.adjt.checkout.dto.order.OrderRequestDto;
import br.com.fiap.postech.adjt.checkout.dto.order.OrderResponseDto;
import br.com.fiap.postech.adjt.checkout.entity.order.Order;
import br.com.fiap.postech.adjt.checkout.exception.InvalidOrderIdException;
import br.com.fiap.postech.adjt.checkout.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class OrderControllerTest {

    @InjectMocks
    private OrderController orderController;

    @Mock
    private OrderService orderService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetByOrderId() {
        UUID orderId = UUID.randomUUID();
        OrderResponseDto orderResponseDto = new OrderResponseDto();
        when(orderService.getById(orderId)).thenReturn(orderResponseDto);
        ResponseEntity<?> response = orderController.getByOderId(orderId.toString());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testGetByOrderIdWhenInvalidFormat() {
        String invalidOrderId = "invalid-uuid";
        assertThrows(InvalidOrderIdException.class, () ->
                orderController.getByOderId(invalidOrderId));
    }

    @Test
    void testCreateOrder() {
        OrderRequestDto orderRequestDto = new OrderRequestDto();
        Order order = new Order();
        when(orderService.createOrder(any(OrderRequestDto.class))).thenReturn(order);
        ResponseEntity<?> response = orderController.createOrder(orderRequestDto);
        assertEquals(ResponseEntity.ok(order), response);
    }

    @Test
    void testCreateOrder_InvalidFormat() {
        OrderRequestDto orderRequestDto = new OrderRequestDto();
        when(orderService.createOrder(any(OrderRequestDto.class))).thenThrow(new RuntimeException());

        ResponseEntity<?> response = orderController.createOrder(orderRequestDto);

//        assertEquals(ResponseEntity.badRequest().body(new InvalidOrderIdException("Invalid orderId format")), response);
    }
}
