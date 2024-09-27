package br.com.fiap.postech.adjt.checkout.domain.exception.model.order;

import br.com.fiap.postech.adjt.checkout.domain.model.order.OrderItemModel;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class OrderItemModelTest {

    @Test
    void constructorAndGetters_ShouldInitializeFields() {
        // Arrange
        UUID id = UUID.randomUUID();
        Long quantity = 10L;
        String codItem = "ITEM123";

        // Act
        OrderItemModel orderItem = new OrderItemModel(id, quantity, codItem);

        // Assert
        assertEquals(id, orderItem.getId());
        assertEquals(quantity, orderItem.getQuantity());
        assertEquals(codItem, orderItem.getCodItem());
    }

    @Test
    void setters_ShouldSetFields() {
        // Arrange
        OrderItemModel orderItem = new OrderItemModel();
        UUID id = UUID.randomUUID();
        Long quantity = 5L;
        String codItem = "ITEM456";

        // Act
        orderItem.setId(id);
        orderItem.setQuantity(quantity);
        orderItem.setCodItem(codItem);

        // Assert
        assertEquals(id, orderItem.getId());
        assertEquals(quantity, orderItem.getQuantity());
        assertEquals(codItem, orderItem.getCodItem());
    }

    @Test
    void builder_ShouldBuildOrderItemModel() {
        // Arrange
        UUID id = UUID.randomUUID();
        Long quantity = 20L;
        String codItem = "ITEM789";

        // Act
        OrderItemModel orderItem = OrderItemModel.builder()
                .id(id)
                .quantity(quantity)
                .codItem(codItem)
                .build();

        // Assert
        assertEquals(id, orderItem.getId());
        assertEquals(quantity, orderItem.getQuantity());
        assertEquals(codItem, orderItem.getCodItem());
    }

    @Test
    void noArgsConstructor_ShouldInitializeDefaultValues() {
        // Act
        OrderItemModel orderItem = new OrderItemModel();

        // Assert
        assertNull(orderItem.getId());
        assertNull(orderItem.getQuantity());
        assertNull(orderItem.getCodItem());
    }
}
