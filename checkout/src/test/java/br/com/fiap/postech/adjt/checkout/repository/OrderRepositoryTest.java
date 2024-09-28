package br.com.fiap.postech.adjt.checkout.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import br.com.fiap.postech.adjt.checkout.model.Card;
import br.com.fiap.postech.adjt.checkout.model.CartItem;
import br.com.fiap.postech.adjt.checkout.model.Order;

@DataJpaTest
class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;

    private Order order;

    @BeforeEach
    void setUp() {
        // Criando uma ordem de exemplo para os testes
        UUID consumerId = UUID.randomUUID();
        CartItem cartItem = new CartItem(1L, 2);
        List<CartItem> items = List.of(cartItem);
        Card card = new Card(UUID.randomUUID(), consumerId, "1234567890123456", "12", "2025", "123", "John Doe");

        order = new Order();
        order.setConsumerId(consumerId);
        order.setPaymentType("Credit Card");
        order.setValue(100);
        order.setPaymentStatus("Pending");
        order.setItems(items);
        order.setCard(card);

        // Salvando a ordem no banco de dados
        orderRepository.save(order);
    }

    @Test
    void testFindByConsumerId() {
        // Testando o método findByConsumerId
        List<Order> orders = orderRepository.findByConsumerId(order.getConsumerId());
        assertFalse(orders.isEmpty(), "Orders should not be empty for the given consumerId");
        assertEquals(order.getConsumerId(), orders.get(0).getConsumerId(), "ConsumerId should match");
    }

    @Test
    void testFindByPaymentStatus() {
        // Testando o método findByPaymentStatus
        List<Order> orders = orderRepository.findByPaymentStatus("Pending");
        assertFalse(orders.isEmpty(), "Orders should not be empty for the given payment status");
        assertEquals("Pending", orders.get(0).getPaymentStatus(), "Payment status should match");
    }

    @Test
    void testSaveOrder() {
        // Testando se a ordem foi salva corretamente
        Order savedOrder = orderRepository.findById(order.getOrderId()).orElse(null);
        assertNotNull(savedOrder, "Order should be saved and not null");
        assertEquals(order.getOrderId(), savedOrder.getOrderId(), "Order ID should match");
    }
}