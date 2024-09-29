package br.com.fiap.postech.adjt.checkout.model.dto.request;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ClearCartRequestTest {

    @Test
    void shouldCreateClearCartRequest_WhenConsumerIdIsProvided() {
        // Arrange
        String consumerId = "12345";

        // Act
        ClearCartRequest request = new ClearCartRequest(consumerId);

        // Assert
        assertNotNull(request);
        assertEquals(consumerId, request.consumerId());
    }

    @Test
    void shouldReturnNull_WhenConsumerIdIsNull() {
        // Act
        ClearCartRequest request = new ClearCartRequest(null);

        // Assert
        assertNull(request.consumerId());
    }
}