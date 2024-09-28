package br.com.fiap.postech.adjt.checkout.model;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OrderTest {

    private Order order;
    private UUID orderId;
    private UUID consumerId;
    private String paymentType;
    private int value;
    private String paymentStatus;
    private List<CartItem> items;
    private Card card;

    @BeforeEach
    void setUp() {
        // Inicializando os valores para o teste
        orderId = UUID.randomUUID();
        consumerId = UUID.randomUUID();
        paymentType = "Credit Card";
        value = 100;
        paymentStatus = "Pending";

        // Criando um item de carrinho para o teste
        CartItem cartItem = new CartItem(1L, 2);
        items = List.of(cartItem);

        // Criando um cartão para o teste
        card = new Card(UUID.randomUUID(), consumerId, "1234567890123456", "12", "2025", "123", "John Doe");

        // Criando a ordem
        order = new Order(orderId, paymentType, value, paymentStatus, items);
        order.setConsumerId(consumerId);
        order.setCard(card);
    }

    @Test
    void testOrderCreation() {
        // Verifica se a ordem foi criada corretamente
        assertNotNull(order);
        assertEquals(orderId, order.getOrderId());
        assertEquals(consumerId, order.getConsumerId());
        assertEquals(paymentType, order.getPaymentType());
        assertEquals(value, order.getValue());
        assertEquals(paymentStatus, order.getPaymentStatus());
        assertEquals(items, order.getItems());
        assertEquals(card, order.getCard());
    }

    @Test
    void testOrderIdShouldNotBeNull() {
        // Verifica se o ID da ordem não é nulo
        assertNotNull(order.getOrderId(), "Order ID should not be null");
    }

    @Test
    void testOrderItemsShouldNotBeEmpty() {
        // Verifica se os itens da ordem não estão vazios
        assertFalse(order.getItems().isEmpty(), "Order items should not be empty");
    }

    @Test
    void testOrderCardShouldNotBeNull() {
        // Verifica se o cartão associado à ordem não é nulo
        assertNotNull(order.getCard(), "Order card should not be null");
    }

    @Test
    void testOrderPaymentStatus() {
        // Verifica se o status de pagamento está correto
        assertEquals("Pending", order.getPaymentStatus(), "Payment status should be 'Pending'");
    }
}