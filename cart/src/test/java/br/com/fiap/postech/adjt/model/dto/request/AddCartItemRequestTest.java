package br.com.fiap.postech.adjt.model.dto.request;

import br.com.fiap.postech.adjt.cart.model.dto.request.AddCartItemRequest;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AddCartItemRequestTest {

    @Test
    void addCartItemRequest_validData_ShouldSetSuccessfully() {
        String uuid = UUID.randomUUID().toString();
        AddCartItemRequest addCartItemRequest = new AddCartItemRequest(uuid,1L,"10");

        assertEquals(uuid, addCartItemRequest.consumerId());
        assertEquals(1L, addCartItemRequest.itemId());
        assertEquals("10", addCartItemRequest.quantity());
    }

}
