package br.com.fiap.postech.adjt.checkout.model.dto.request;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class FindCartByCustomerIdRequestTest {

    @Test
    void shouldCreateFindCartByCustomerIdRequest_WhenConsumerIdIsProvided() {
        // Arrange
        String consumerId = "12345";

        // Act
        FindCartByCustomerIdRequest request = new FindCartByCustomerIdRequest(consumerId);

        // Assert
        assertNotNull(request);
        assertEquals(consumerId, request.consumerId());
    }

    @Test
    void shouldReturnNull_WhenConsumerIdIsNull() {
        // Act
        FindCartByCustomerIdRequest request = new FindCartByCustomerIdRequest(null);

        // Assert
        assertNull(request.consumerId());
    }
}