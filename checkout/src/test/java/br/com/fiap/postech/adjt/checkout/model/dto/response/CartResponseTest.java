package br.com.fiap.postech.adjt.checkout.model.dto.response;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class CartResponseTest {

    @Test
    void shouldCreateCartResponse_WithCorrectValues() {
        // Arrange
        CartItemResponse item1 = new CartItemResponse(1L, 2);
        CartItemResponse item2 = new CartItemResponse(2L, 3);
        List<CartItemResponse> expectedItems = List.of(item1, item2);

        // Act
        CartResponse cartResponse = new CartResponse(expectedItems);

        // Assert
        assertEquals(expectedItems, cartResponse.items(), "Items should match the expected list");
    }

    @Test
    void shouldReturnTrue_WhenComparingEqualCartResponses() {
        // Arrange
        CartItemResponse item1 = new CartItemResponse(1L, 2);
        CartItemResponse item2 = new CartItemResponse(2L, 3);
        List<CartItemResponse> items = List.of(item1, item2);

        CartResponse cartResponse1 = new CartResponse(items);
        CartResponse cartResponse2 = new CartResponse(items);

        // Act & Assert
        assertEquals(cartResponse1, cartResponse2, "CartResponses with the same items should be equal");
    }

    @Test
    void shouldReturnFalse_WhenComparingDifferentCartResponses() {
        // Arrange
        CartItemResponse item1 = new CartItemResponse(1L, 2);
        CartItemResponse item2 = new CartItemResponse(2L, 3);
        CartItemResponse item3 = new CartItemResponse(3L, 4);

        List<CartItemResponse> items1 = List.of(item1, item2);
        List<CartItemResponse> items2 = List.of(item1, item3);

        CartResponse cartResponse1 = new CartResponse(items1);
        CartResponse cartResponse2 = new CartResponse(items2);

        // Act & Assert
        assertNotEquals(cartResponse1, cartResponse2, "CartResponses with different items should not be equal");
    }

    @Test
    void shouldGenerateCorrectHashCode() {
        // Arrange
        CartItemResponse item1 = new CartItemResponse(1L, 2);
        CartItemResponse item2 = new CartItemResponse(2L, 3);
        List<CartItemResponse> items = List.of(item1, item2);

        CartResponse cartResponse1 = new CartResponse(items);
        CartResponse cartResponse2 = new CartResponse(items);

        // Act & Assert
        assertEquals(cartResponse1.hashCode(), cartResponse2.hashCode(), "Hash codes should be equal for equal CartResponses");
    }

    @Test
    void shouldGenerateCorrectToString() {
        // Arrange
        CartItemResponse item1 = new CartItemResponse(1L, 2);
        CartItemResponse item2 = new CartItemResponse(2L, 3);
        List<CartItemResponse> items = List.of(item1, item2);

        CartResponse cartResponse = new CartResponse(items);
        String expectedToString = "CartResponse[items=[CartItemResponse[itemId=1, qnt=2], CartItemResponse[itemId=2, qnt=3]]]";

        // Act & Assert
        assertEquals(expectedToString, cartResponse.toString(), "toString() should return the correct string representation");
    }
}