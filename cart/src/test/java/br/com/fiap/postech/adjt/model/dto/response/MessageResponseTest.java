package br.com.fiap.postech.adjt.model.dto.response;

import br.com.fiap.postech.adjt.cart.model.dto.response.MessageResponse;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MessageResponseTest {

    @Test
    public void messageResponseTest_validData_ShouldSetSuccessfully() {
        MessageResponse messageResponse = new MessageResponse("Message send");

        assertEquals("Message send",messageResponse.message());
    }
}