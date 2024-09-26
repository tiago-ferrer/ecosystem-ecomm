package br.com.fiap.postech.adjt.cart.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ResponseMessageTests {

    @Test
    void testGetMessage() {
        ResponseMessage response = new ResponseMessage("Operation successful");
        assertEquals("Operation successful", response.getMessage());
    }

    @Test
    void testSetMessage() {
        ResponseMessage response = new ResponseMessage("Initial message");
        response.setMessage("Updated message");
        assertEquals("Updated message", response.getMessage());
    }

    @Test
    void testConstructor() {
        ResponseMessage response = new ResponseMessage("Message from constructor");
        assertEquals("Message from constructor", response.getMessage());
    }

    @Test
    void testEqualsAndHashCode() {
        ResponseMessage response1 = new ResponseMessage("Message for testing");
        ResponseMessage response2 = new ResponseMessage("Message for testing");

        assertEquals(response1, response2);
        assertEquals(response1.hashCode(), response2.hashCode());
    }

    @Test
    void testToString() {
        ResponseMessage response = new ResponseMessage("String test message");
        String expected = "ResponseMessage(message=String test message)";
        assertEquals(expected, response.toString());
    }
}
