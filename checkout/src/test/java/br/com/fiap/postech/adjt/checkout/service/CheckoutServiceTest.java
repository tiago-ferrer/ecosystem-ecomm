package br.com.fiap.postech.adjt.checkout.service;

import br.com.fiap.postech.adjt.checkout.entity.Order;
import br.com.fiap.postech.adjt.checkout.repository.OrderRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class CheckoutServiceTest {

    private CheckoutService checkoutService;

    @Mock
    private OrderRepository orderRepository;

    AutoCloseable mock;

    @BeforeEach
    void setUp() {
        this.mock = MockitoAnnotations.openMocks(this);
        this.checkoutService = new CheckoutService(orderRepository);
    }

    @AfterEach
    void tearDown() throws Exception {
        this.mock.close();
    }

    @Test
    void shouldFindOrdersByConsumerId() {
        Order order = new Order();
        List<Order> orders = new ArrayList<>();
        orders.add(order);
        when(orderRepository.findByConsumerId(any(UUID.class))).thenReturn(orders);
    }

    @Test
    void shouldFindOrderById() {
        when(orderRepository.findByOrderId(any(UUID.class))).thenReturn(new Order());
        UUID orderId = UUID.fromString("8a54cd2f-19f1-4c91-8900-fb8f8f5a9b4d");
        Order order = checkoutService.findOrderById(orderId);
        assertThat(order).isNotNull().isInstanceOf(Order.class);
    }

}
