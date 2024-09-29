package br.com.fiap.postech.adjt.cart.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CartConsumerRequestTest {

    @Test
    void testGetConsumerId() {
        CartConsumerRequest request = new CartConsumerRequest();
        request.setConsumerId("153e23c8-322e-4fec-b9c4-72b8f74ad002");
        assertEquals("153e23c8-322e-4fec-b9c4-72b8f74ad002", request.getConsumerId());
    }

    @Test
    void testSetConsumerId() {
        CartConsumerRequest request = new CartConsumerRequest();
        request.setConsumerId("c5d9e62c-e29e-44b5-b8c6-92f5d8d2d77f");
        assertEquals("c5d9e62c-e29e-44b5-b8c6-92f5d8d2d77f", request.getConsumerId());
    }

    @Test
    void testConsumerIdIsNullByDefault() {
        CartConsumerRequest request = new CartConsumerRequest();
        assertNull(request.getConsumerId());
    }

    @Test
    void testConstructor() {
        CartConsumerRequest request = new CartConsumerRequest();
        assertNull(request.getConsumerId());
        request.setConsumerId("bcb69e23-f8e3-4d43-83b3-b845ff65b930");
        assertEquals("bcb69e23-f8e3-4d43-83b3-b845ff65b930", request.getConsumerId());
    }

    @Test
    void testEqualsAndHashCode() {
        CartConsumerRequest request1 = new CartConsumerRequest();
        request1.setConsumerId("456e23c8-222e-4b8f-b9c4-72b8f74ad002");

        CartConsumerRequest request2 = new CartConsumerRequest();
        request2.setConsumerId("456e23c8-222e-4b8f-b9c4-72b8f74ad002");

        assertEquals(request1, request2);
        assertEquals(request1.hashCode(), request2.hashCode());
    }

    @Test
    void testToString() {
        CartConsumerRequest request = new CartConsumerRequest();
        request.setConsumerId("789e23c8-123e-4b8f-b9c4-72b8f74ad999");
        String expected = "CartConsumerRequest(consumerId=789e23c8-123e-4b8f-b9c4-72b8f74ad999)";
        assertEquals(expected, request.toString());
    }
}
