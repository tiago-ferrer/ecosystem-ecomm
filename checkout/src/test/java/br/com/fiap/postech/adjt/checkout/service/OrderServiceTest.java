package br.com.fiap.postech.adjt.checkout.service;

import br.com.fiap.postech.adjt.checkout.model.*;
import br.com.fiap.postech.adjt.checkout.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderService orderService;
    AutoCloseable openMocks;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createAndSaveOrderShouldCreateOrder() {

        Cart cart = Cart.builder().build();
        UUID consumerId = UUID.randomUUID();
        cart.setConsumerId(consumerId);
        cart.setItems(Arrays.asList(Item.builder().build()));

        Checkout checkout = new Checkout();
        checkout.setCurrency(Currency.BRL);
        checkout.setAmount(100);
        checkout.setStatus(PaymentStatus.approved);

        PaymentMethod paymentMethod = new PaymentMethod();
        paymentMethod.setType(PaymentMethodType.br_credit_card);
        checkout.setPaymentMethod(paymentMethod);

        Order expectedOrder = new Order();
        expectedOrder.setConsumerId(consumerId);
        expectedOrder.setItems(cart.getItems());
        expectedOrder.setCurrency(checkout.getCurrency());
        expectedOrder.setValue(checkout.getAmount());
        expectedOrder.setPaymentMethodType(paymentMethod.getType());
        expectedOrder.setPaymentStatus(checkout.getStatus());

        when(orderRepository.save(any(Order.class))).thenReturn(expectedOrder);

        Order actualOrder = orderService.createAndSaveOrder(cart, checkout);

        assertNotNull(actualOrder);
        assertEquals(expectedOrder.getValue(), actualOrder.getValue());
        assertEquals(expectedOrder.getCurrency(), actualOrder.getCurrency());
        assertEquals(expectedOrder.getPaymentMethodType(), actualOrder.getPaymentMethodType());
        assertEquals(expectedOrder.getPaymentStatus(), actualOrder.getPaymentStatus());

        verify(orderRepository).save(any(Order.class));
    }


    @Test
    void getOrderByOrderIdShouldReturnOrder() {

        UUID orderId = UUID.randomUUID();
        Order expectedOrder = new Order();
        when(orderRepository.findByOrderId(orderId)).thenReturn(expectedOrder);

        Order actualOrder = orderService.getOrderByOrderId(orderId.toString());

        assertNotNull(actualOrder);
        assertEquals(expectedOrder, actualOrder);
        verify(orderRepository).findByOrderId(orderId);
    }

    @Test
    void updateOrderShouldUpdateExistingOrder() {

        Order orderToUpdate = new Order();
        when(orderRepository.findById(orderToUpdate.getOrderId())).thenReturn(Optional.of(orderToUpdate));

        orderService.updateOrder(orderToUpdate);

        verify(orderRepository).save(orderToUpdate);
    }

    @Test
    void updateOrderOrderNotFoundShouldThrowException() {

        Order orderToUpdate = new Order();

        when(orderRepository.findById(orderToUpdate.getOrderId())).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> orderService.updateOrder(orderToUpdate));
    }
}
