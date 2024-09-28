package br.com.fiap.postech.adjt.checkout.model.dto.response;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CheckoutResponseTest {

    @Test
    void shouldCreateCheckoutResponse_WithCorrectValues() {
        // Arrange
        String expectedOrderId = "12345";
        String expectedStatus = "pending";

        // Act
        CheckoutResponse checkoutResponse = new CheckoutResponse(expectedOrderId, expectedStatus);

        // Assert
        assertEquals(expectedOrderId, checkoutResponse.getOrderId(), "OrderId should match the expected value");
        assertEquals(expectedStatus, checkoutResponse.getStatus(), "Status should match the expected value");
    }

    @Test
    void shouldReturnTrue_WhenComparingEqualCheckoutResponses() {
        // Arrange
        String orderId = "12345";
        String status = "pending";
        CheckoutResponse checkoutResponse1 = new CheckoutResponse(orderId, status);
        CheckoutResponse checkoutResponse2 = new CheckoutResponse(orderId, status);

        // Act & Assert
        assertEquals(checkoutResponse1, checkoutResponse2, "CheckoutResponses with the same values should be equal");
    }

    @Test
    void shouldReturnFalse_WhenComparingDifferentCheckoutResponses() {
        // Arrange
        CheckoutResponse checkoutResponse1 = new CheckoutResponse("12345", "pending");
        CheckoutResponse checkoutResponse2 = new CheckoutResponse("54321", "completed");

        // Act & Assert
        assertNotEquals(checkoutResponse1, checkoutResponse2, "CheckoutResponses with different values should not be equal");
    }

    @Test
    void shouldGenerateCorrectHashCode() {
        // Arrange
        String orderId = "12345";
        String status = "pending";
        CheckoutResponse checkoutResponse1 = new CheckoutResponse(orderId, status);
        CheckoutResponse checkoutResponse2 = new CheckoutResponse(orderId, status);

        // Act & Assert
        assertEquals(checkoutResponse1.hashCode(), checkoutResponse2.hashCode(), "Hash codes should be equal for equal CheckoutResponses");
    }

    @Test
    void shouldGenerateCorrectToString() {
        // Arrange
        String orderId = "12345";
        String status = "pending";
        CheckoutResponse checkoutResponse = new CheckoutResponse(orderId, status);
        String expectedToString = "CheckoutResponse(orderId=12345, status=pending)";

        // Act & Assert
        assertEquals(expectedToString, checkoutResponse.toString(), "toString() should return the correct string representation");
    }
}