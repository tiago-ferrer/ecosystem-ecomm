package br.com.fiap.postech.adjt.model.dto.request;

import br.com.fiap.postech.adjt.cart.model.dto.request.RemoveCartItemRequest;
import org.junit.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RemoveCartItemRequestTest {

    @Test
    public void removeCartItemRequestTest_validData_ShouldSetSuccessfully() {
        String uuid = UUID.randomUUID().toString();
        RemoveCartItemRequest removeCartItemRequest = new RemoveCartItemRequest(uuid, 1L);

        assertEquals(uuid,removeCartItemRequest.consumerId());
        assertEquals(1L,removeCartItemRequest.itemId());
    }
}
