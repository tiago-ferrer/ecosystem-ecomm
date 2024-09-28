package br.com.fiap.postech.adjt.checkout.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;

class CartItemTest {

    @Test
    void shouldCreateCartItem_WithCorrectValues() {
        // Arrange
        Long expectedItemId = 1L;
        int expectedQuantity = 5;

        // Act
        CartItem cartItem = new CartItem(expectedItemId, expectedQuantity);

        // Assert
        assertEquals(expectedItemId, cartItem.getItemId(), "Item ID should match the expected value");
        assertEquals(expectedQuantity, cartItem.getQuantity(), "Quantity should match the expected value");
    }

    @Test
    void shouldReturnTrue_WhenComparingEqualCartItems() {
        // Arrange
        CartItem cartItem1 = new CartItem(1L, 5);
        CartItem cartItem2 = new CartItem(1L, 5);

        // Act & Assert
        assertEquals(cartItem1, cartItem2, "CartItems with the same values should be equal");
    }

    @Test
    void shouldReturnFalse_WhenComparingDifferentCartItems() {
        // Arrange
        CartItem cartItem1 = new CartItem(1L, 5);
        CartItem cartItem2 = new CartItem(2L, 10);

        // Act & Assert
        assertNotEquals(cartItem1, cartItem2, "CartItems with different values should not be equal");
    }

    @Test
    void shouldGenerateCorrectHashCode() {
        // Arrange
        CartItem cartItem1 = new CartItem(1L, 5);
        CartItem cartItem2 = new CartItem(1L, 5);

        // Act & Assert
        assertEquals(cartItem1.hashCode(), cartItem2.hashCode(), "Hash codes should be equal for equal CartItems");
    }

    @Test
    void shouldGenerateCorrectToString() {
        // Arrange
        CartItem cartItem = new CartItem(1L, 5);
        String expectedToString = "CartItem(id=null, itemId=1, quantity=5)";

        // Act & Assert
        assertEquals(expectedToString, cartItem.toString(), "toString() should return the correct string representation");
    }
}