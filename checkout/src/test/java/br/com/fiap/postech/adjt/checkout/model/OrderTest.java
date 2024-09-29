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
                UUID orderId = UUID.randomUUID();
                List<OrderItemDTO> orderItems = List.of(
                        new OrderItemDTO(orderId, 1L, 2)
                );
                String paymentType = "CREDIT_CARD";
                double value = 100.0;
                PaymentStatus paymentStatus = PaymentStatus.PENDING;

                Order order = new Order(orderId, orderItems, paymentType, value, paymentStatus);

                assertNotNull(order.getOrderId());
                assertEquals(paymentType, order.getPaymentType());
                assertEquals(value, order.getValue(), 0.01);
                assertEquals(paymentStatus, order.getPaymentStatus());
                assertEquals(2, order.getItems().size());
        }
}
