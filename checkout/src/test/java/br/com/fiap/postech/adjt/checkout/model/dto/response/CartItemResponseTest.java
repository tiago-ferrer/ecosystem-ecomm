package br.com.fiap.postech.adjt.checkout.model.dto.response;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CartItemResponseTest {

    @Test
    void shouldCreateCartItemResponse_WithCorrectValues() {
        // Arrange
        Long expectedItemId = 1L;
        Integer expectedQnt = 2;

        // Act
        CartItemResponse cartItemResponse = new CartItemResponse(expectedItemId, expectedQnt);

        // Assert
        assertEquals(expectedItemId, cartItemResponse.itemId(), "ItemId should match the expected value");
        assertEquals(expectedQnt, cartItemResponse.qnt(), "Quantity should match the expected value");
    }

    @Test
    void shouldReturnTrue_WhenComparingEqualCartItemResponses() {
        // Arrange
        CartItemResponse cartItemResponse1 = new CartItemResponse(1L, 2);
        CartItemResponse cartItemResponse2 = new CartItemResponse(1L, 2);

        // Act & Assert
        assertEquals(cartItemResponse1, cartItemResponse2, "CartItemResponses with the same values should be equal");
    }

    @Test
    void shouldReturnFalse_WhenComparingDifferentCartItemResponses() {
        // Arrange
        CartItemResponse cartItemResponse1 = new CartItemResponse(1L, 2);
        CartItemResponse cartItemResponse2 = new CartItemResponse(2L, 3);

        // Act & Assert
        assertNotEquals(cartItemResponse1, cartItemResponse2, "CartItemResponses with different values should not be equal");
    }

    @Test
    void shouldGenerateCorrectHashCode() {
        // Arrange
        CartItemResponse cartItemResponse1 = new CartItemResponse(1L, 2);
        CartItemResponse cartItemResponse2 = new CartItemResponse(1L, 2);

        // Act & Assert
        assertEquals(cartItemResponse1.hashCode(), cartItemResponse2.hashCode(), "Hash codes should be equal for equal CartItemResponses");
    }

    @Test
    void shouldGenerateCorrectToString() {
        // Arrange
        CartItemResponse cartItemResponse = new CartItemResponse(1L, 2);
        String expectedToString = "CartItemResponse[itemId=1, qnt=2]";

        // Act & Assert
        assertEquals(expectedToString, cartItemResponse.toString(), "toString() should return the correct string representation");
    }
}