package br.com.fiap.postech.adjt.checkout.domain.exception.model;

import br.com.fiap.postech.adjt.checkout.dataprovider.database.entity.OrderEntity;
import br.com.fiap.postech.adjt.checkout.domain.model.enums.PaymentStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class OrderTests {

    private OrderEntity order;

    @BeforeEach
    void setUp() {
        order = new OrderEntity();
    }

    @Test
    void testOrderCreation() {
        OrderEntity newOrder = new OrderEntity(UUID.fromString("281937e1-349e-4d3c-81e1-f88953f54c97"), PaymentStatus.approved, "customerId", "shippingAddress", 1050.0, new ArrayList<>());
        assertNotNull(newOrder);
        assertEquals("281937e1-349e-4d3c-81e1-f88953f54c97", newOrder.getOrderId().toString());
        assertEquals("customerId", newOrder.getConsumerId());
    }

    @Test
    void testOrderDefaultConstructor() {
        OrderEntity defaultOrder = new OrderEntity();
        assertNotNull(defaultOrder);
    }

    @Test
    void testSettersAndGetters() {
        order.setOrderId(UUID.fromString("281937e1-349e-4d3c-81e1-f88953f54c97"));
        assertEquals("281937e1-349e-4d3c-81e1-f88953f54c97", order.getOrderId().toString());

        order.setConsumerId("customerId");
        assertEquals("customerId", order.getConsumerId());

    }

    @Test
    void testOrderEquality() {
        // Testar a igualdade entre duas instâncias de ordens
        OrderEntity order1 = new OrderEntity(UUID.fromString("281937e1-349e-4d3c-81e1-f88953f54c97"), PaymentStatus.declined, "status", "shippingAddress", 1050.0, new ArrayList<>());
        OrderEntity order2 = new OrderEntity(UUID.fromString("281937e1-349e-4d3c-81e1-f88953f54c97"), PaymentStatus.declined, "status", "shippingAddress", 1050.0, new ArrayList<>());

        assertEquals(order1, order2);
    }

    @Test
    void testOrderHashCode() {
        // Testar o hashCode da ordem
        OrderEntity order1 = new OrderEntity(UUID.fromString("281937e1-349e-4d3c-81e1-f88953f54c97"), PaymentStatus.denied, "status", "shippingAddress", 1050.2, new ArrayList<>());
        OrderEntity order2 = new OrderEntity(UUID.fromString("281937e1-349e-4d3c-81e1-f88953f54c97"), PaymentStatus.denied, "status", "shippingAddress", 1050.2, new ArrayList<>());

        assertEquals(order1.hashCode(), order2.hashCode());
    }

    @Test
    void testOrderCreationValid() {
        // Testar a criação da ordem com dados válidos
        OrderEntity validOrder = new OrderEntity(UUID.fromString("281937e1-349e-4d3c-81e1-f88953f54c97"), PaymentStatus.denied, "status", "shippingAddress", 200.5, new ArrayList<>());
        assertNotNull(validOrder);
    }

}