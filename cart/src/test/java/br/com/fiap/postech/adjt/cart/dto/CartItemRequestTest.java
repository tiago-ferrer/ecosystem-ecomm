package br.com.fiap.postech.adjt.cart.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CartItemRequestTest {

    @Test
    void testGetAndSetConsumerId() {
        CartItemRequest request = new CartItemRequest();
        request.setConsumerId("153e23c8-322e-4fec-b9c4-72b8f74ad002");
        assertEquals("153e23c8-322e-4fec-b9c4-72b8f74ad002", request.getConsumerId());
    }

    @Test
    void testGetAndSetItemId() {
        CartItemRequest request = new CartItemRequest();
        request.setItemId("1");
        assertEquals("1", request.getItemId());
    }

    @Test
    void testGetAndSetQuantity() {
        CartItemRequest request = new CartItemRequest();
        request.setQuantity(5);
        assertEquals(5, request.getQuantity());
    }

    @Test
    void testConsumerIdIsNullByDefault() {
        CartItemRequest request = new CartItemRequest();
        assertNull(request.getConsumerId());
    }

    @Test
    void testItemIdIsNullByDefault() {
        CartItemRequest request = new CartItemRequest();
        assertNull(request.getItemId());
    }

    @Test
    void testQuantityIsZeroByDefault() {
        CartItemRequest request = new CartItemRequest();
        assertEquals(0, request.getQuantity());
    }

    @Test
    void testEqualsAndHashCode() {
        CartItemRequest request1 = new CartItemRequest();
        request1.setConsumerId("153e23c8-322e-4fec-b9c4-72b8f74ad002");
        request1.setItemId("1");
        request1.setQuantity(2);

        CartItemRequest request2 = new CartItemRequest();
        request2.setConsumerId("153e23c8-322e-4fec-b9c4-72b8f74ad002");
        request2.setItemId("1");
        request2.setQuantity(2);

        assertEquals(request1, request2);
        assertEquals(request1.hashCode(), request2.hashCode());
    }

    @Test
    void testToString() {
        CartItemRequest request = new CartItemRequest();
        request.setConsumerId("153e23c8-322e-4fec-b9c4-72b8f74ad002");
        request.setItemId("1");
        request.setQuantity(3);
        String expected = "CartItemRequest(consumerId=153e23c8-322e-4fec-b9c4-72b8f74ad002, itemId=1, quantity=3)";
        assertEquals(expected, request.toString());
    }

    @Test
    void testEquals_DifferentObjectType() {
        CartItemRequest request = new CartItemRequest();
        assertFalse(request.equals("Some String")); // Testa a comparação com um tipo diferente
    }

    @Test
    void testEquals_DifferentAttributes() {
        CartItemRequest request1 = new CartItemRequest();
        request1.setConsumerId("153e23c8-322e-4fec-b9c4-72b8f74ad002");
        request1.setItemId("1");
        request1.setQuantity(2);

        CartItemRequest request2 = new CartItemRequest();
        request2.setConsumerId("different-id");
        request2.setItemId("1");
        request2.setQuantity(2);

        assertNotEquals(request1, request2); // Testa a desigualdade por `consumerId` diferente
    }

    @Test
    void testCanEqual() {
        CartItemRequest request = new CartItemRequest();
        assertTrue(request.canEqual(new CartItemRequest())); // Testa com uma instância do mesmo tipo
        assertFalse(request.canEqual(new Object())); // Testa com um tipo diferente
    }

}
