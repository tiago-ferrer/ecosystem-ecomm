package br.com.fiap.postech.adjt.checkout.model;

import br.com.fiap.postech.adjt.checkout.dataprovider.database.entity.OrderItemEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class OrderItemEntityTests {

    private OrderItemEntity orderItem;

    @BeforeEach
    void setUp() {
        // Inicializar a instância de OrderItemEntity antes de cada teste
        orderItem = new OrderItemEntity();
    }

    @Test
    void testOrderItemEntityCreation() {
        // Testar a criação do item do pedido
        OrderItemEntity newOrderItem = new OrderItemEntity(UUID.fromString("281937e1-349e-4d3c-81e1-f88953f54c97"), "product", 0L, 2.0, null);
        assertNotNull(newOrderItem);
        assertEquals("product", newOrderItem.getCodItem());
        assertEquals(0L, newOrderItem.getQuantity());
        assertEquals(2.0, newOrderItem.getPrice());
    }

    @Test
    void testOrderItemEntityDefaultConstructor() {
        // Testar o construtor padrão
        OrderItemEntity defaultOrderItem = new OrderItemEntity();
        assertNotNull(defaultOrderItem);
    }

    @Test
    void testSettersAndGetters() {
        // Testar os métodos setters e getters
        orderItem.setCodItem("productId");
        assertEquals("productId", orderItem.getCodItem());

        orderItem.setQuantity(2L);
        assertEquals(2L, orderItem.getQuantity());

        orderItem.setPrice(100.0);
        assertEquals(100.0, orderItem.getPrice());

    }

    @Test
    void testOrderItemEntityEquality() {
        // Testar a igualdade entre duas instâncias de OrderItemEntity
        OrderItemEntity orderItem1 = new OrderItemEntity(UUID.fromString("281937e1-349e-4d3c-81e1-f88953f54c97"), "product", 0L, 2.0, null);
        OrderItemEntity orderItem2 = new OrderItemEntity(UUID.fromString("281937e1-349e-4d3c-81e1-f88953f54c97"), "product", 0L, 2.0, null);

        assertEquals(orderItem1, orderItem2);
    }

    @Test
    void testOrderItemEntityHashCode() {
        // Testar o hashCode de OrderItemEntity
        OrderItemEntity orderItem1 = new OrderItemEntity(UUID.fromString("281937e1-349e-4d3c-81e1-f88953f54c97"), "product", 0L, 2.0, null);
        OrderItemEntity orderItem2 = new OrderItemEntity(UUID.fromString("281937e1-349e-4d3c-81e1-f88953f54c97"), "product", 0L, 2.0, null);

        assertEquals(orderItem1.hashCode(), orderItem2.hashCode());
    }

    @Test
    void testOrderItemEntityCreationValid() {
        // Testar a criação de OrderItemEntity com dados válidos
        OrderItemEntity validOrderItem = new OrderItemEntity(UUID.fromString("281937e1-349e-4d3c-81e1-f88953f54c97"), "product", 0L, 2.0, null);
        assertNotNull(validOrderItem);
    }
}
