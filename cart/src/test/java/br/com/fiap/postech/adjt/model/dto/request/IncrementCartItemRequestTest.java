package br.com.fiap.postech.adjt.model.dto.request;

import br.com.fiap.postech.adjt.cart.model.dto.request.IncrementCartItemRequest;
import org.junit.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class IncrementCartItemRequestTest {

    @Test
    public void incrementCartItemRequestTest_validData_ShouldSetSuccessfully() {
        UUID uuid = UUID.randomUUID();
        IncrementCartItemRequest incrementCartItemRequest = new IncrementCartItemRequest(uuid.toString(),1L);

        assertEquals(uuid.toString(), incrementCartItemRequest.consumerId());
        assertEquals(1L, incrementCartItemRequest.itemId());
    }
}
