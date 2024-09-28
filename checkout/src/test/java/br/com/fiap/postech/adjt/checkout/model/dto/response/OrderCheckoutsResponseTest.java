package br.com.fiap.postech.adjt.checkout.model.dto.response;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class OrderCheckoutsResponseTest {

    @Test
    void shouldCreateOrderCheckoutsResponse_WithCorrectValues() {
        // Arrange
        UUID expectedOrderId = UUID.randomUUID();
        CartItemResponse item1 = new CartItemResponse(1L, 2);
        CartItemResponse item2 = new CartItemResponse(2L, 3);
        List<CartItemResponse> expectedItems = List.of(item1, item2);
        String expectedPaymentType = "credit";
        int expectedValue = 100;
        String expectedPaymentStatus = "pending";

        // Act
        OrderCheckoutsResponse response = new OrderCheckoutsResponse(
                expectedOrderId, expectedItems, expectedPaymentType, expectedValue, expectedPaymentStatus
        );

        // Assert
        assertEquals(expectedOrderId, response.orderId(), "OrderId should match the expected value");
        assertEquals(expectedItems, response.items(), "Items should match the expected list");
        assertEquals(expectedPaymentType, response.paymentType(), "PaymentType should match the expected value");
        assertEquals(expectedValue, response.value(), "Value should match the expected value");
        assertEquals(expectedPaymentStatus, response.paymentStatus(), "PaymentStatus should match the expected value");
    }

    @Test
    void shouldReturnTrue_WhenComparingEqualOrderCheckoutsResponses() {
        // Arrange
        UUID orderId = UUID.randomUUID();
        CartItemResponse item1 = new CartItemResponse(1L, 2);
        CartItemResponse item2 = new CartItemResponse(2L, 3);
        List<CartItemResponse> items = List.of(item1, item2);
        String paymentType = "credit";
        int value = 100;
        String paymentStatus = "pending";

        OrderCheckoutsResponse response1 = new OrderCheckoutsResponse(orderId, items, paymentType, value, paymentStatus);
        OrderCheckoutsResponse response2 = new OrderCheckoutsResponse(orderId, items, paymentType, value, paymentStatus);

        // Act & Assert
        assertEquals(response1, response2, "OrderCheckoutsResponses with the same values should be equal");
    }

    @Test
    void shouldReturnFalse_WhenComparingDifferentOrderCheckoutsResponses() {
        // Arrange
        UUID orderId1 = UUID.randomUUID();
        UUID orderId2 = UUID.randomUUID();
        CartItemResponse item1 = new CartItemResponse(1L, 2);
        CartItemResponse item2 = new CartItemResponse(2L, 3);
        List<CartItemResponse> items = List.of(item1, item2);

        OrderCheckoutsResponse response1 = new OrderCheckoutsResponse(orderId1, items, "credit", 100, "pending");
        OrderCheckoutsResponse response2 = new OrderCheckoutsResponse(orderId2, items, "debit", 200, "completed");

        // Act & Assert
        assertNotEquals(response1, response2, "OrderCheckoutsResponses with different values should not be equal");
    }

    @Test
    void shouldGenerateCorrectHashCode() {
        // Arrange
        UUID orderId = UUID.randomUUID();
        CartItemResponse item1 = new CartItemResponse(1L, 2);
        CartItemResponse item2 = new CartItemResponse(2L, 3);
        List<CartItemResponse> items = List.of(item1, item2);
        String paymentType = "credit";
        int value = 100;
        String paymentStatus = "pending";

        OrderCheckoutsResponse response1 = new OrderCheckoutsResponse(orderId, items, paymentType, value, paymentStatus);
        OrderCheckoutsResponse response2 = new OrderCheckoutsResponse(orderId, items, paymentType, value, paymentStatus);

        // Act & Assert
        assertEquals(response1.hashCode(), response2.hashCode(), "Hash codes should be equal for equal OrderCheckoutsResponses");
    }

    @Test
    void shouldGenerateCorrectToString() {
        // Arrange
        UUID orderId = UUID.randomUUID();
        CartItemResponse item1 = new CartItemResponse(1L, 2);
        CartItemResponse item2 = new CartItemResponse(2L, 3);
        List<CartItemResponse> items = List.of(item1, item2);
        String paymentType = "credit";
        int value = 100;
        String paymentStatus = "pending";

        OrderCheckoutsResponse response = new OrderCheckoutsResponse(orderId, items, paymentType, value, paymentStatus);
        String expectedToString = "OrderCheckoutsResponse[orderId=" + orderId + ", items=" + items + ", paymentType=credit, value=100, paymentStatus=pending]";

        // Act & Assert
        assertEquals(expectedToString, response.toString(), "toString() should return the correct string representation");
    }
}