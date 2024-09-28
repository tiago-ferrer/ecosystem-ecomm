package br.com.fiap.postech.adjt.checkout.model.dto.response;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MessageResponseTest {

    @Test
    void shouldCreateMessageResponse_WithCorrectValue() {
        // Arrange
        String expectedMessage = "Success";

        // Act
        MessageResponse messageResponse = new MessageResponse(expectedMessage);

        // Assert
        assertEquals(expectedMessage, messageResponse.message(), "Message should match the expected value");
    }

    @Test
    void shouldReturnTrue_WhenComparingEqualMessageResponses() {
        // Arrange
        String message = "Success";
        MessageResponse messageResponse1 = new MessageResponse(message);
        MessageResponse messageResponse2 = new MessageResponse(message);

        // Act & Assert
        assertEquals(messageResponse1, messageResponse2, "MessageResponses with the same message should be equal");
    }

    @Test
    void shouldReturnFalse_WhenComparingDifferentMessageResponses() {
        // Arrange
        MessageResponse messageResponse1 = new MessageResponse("Success");
        MessageResponse messageResponse2 = new MessageResponse("Error");

        // Act & Assert
        assertNotEquals(messageResponse1, messageResponse2, "MessageResponses with different messages should not be equal");
    }

    @Test
    void shouldGenerateCorrectHashCode() {
        // Arrange
        String message = "Success";
        MessageResponse messageResponse1 = new MessageResponse(message);
        MessageResponse messageResponse2 = new MessageResponse(message);

        // Act & Assert
        assertEquals(messageResponse1.hashCode(), messageResponse2.hashCode(), "Hash codes should be equal for equal MessageResponses");
    }

    @Test
    void shouldGenerateCorrectToString() {
        // Arrange
        String message = "Success";
        MessageResponse messageResponse = new MessageResponse(message);
        String expectedToString = "MessageResponse[message=Success]";

        // Act & Assert
        assertEquals(expectedToString, messageResponse.toString(), "toString() should return the correct string representation");
    }
}