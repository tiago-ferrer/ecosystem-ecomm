package br.com.fiap.postech.adjt.checkout.model;

import br.com.fiap.postech.adjt.checkout.dto.OrderItemDTO;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class OrderTest {
    @Test
    public void testOrderCreation() {
        // Arrange
<<<<<<< HEAD
        UUID orderId = UUID.randomUUID();
        List<OrderItemDTO> orderItems = List.of(
                new OrderItemDTO(orderId, 1L, 2)
        );
        String paymentType = "CREDIT_CARD";
        double value = 100.0;
        PaymentStatus paymentStatus = PaymentStatus.PENDING;
=======
        UUID orderId = UUID.randomUUID(); // Gerando um novo UUID para o pedido
        List<OrderItemDTO> orderItems = List.of(
                new OrderItemDTO(orderId, 1L, 2) // itemId = 1L, quantidade = 2, desconto = 4
        );
        String paymentType = "CREDIT_CARD";
        double value = 100.0;
        PaymentStatus paymentStatus = PaymentStatus.PENDING; // Usando o enum adequado
>>>>>>> 79b1335a998168ed3b4359d3ce24c4ea73b61784

        // Act
        Order order = new Order(orderId, orderItems, paymentType, value, paymentStatus);

        // Assert
        assertNotNull(order.getOrderId());
        assertEquals(paymentType, order.getPaymentType());
<<<<<<< HEAD
        assertEquals(value, order.getValue(), 0.01);
=======
        assertEquals(value, order.getValue(), 0.01); // Usar tolerância para valores double
>>>>>>> 79b1335a998168ed3b4359d3ce24c4ea73b61784
        assertEquals(paymentStatus, order.getPaymentStatus());
        assertEquals(2, order.getItems().size());
    }
}
