package br.com.fiap.postech.adjt.model.dto.request;

import br.com.fiap.postech.adjt.cart.model.dto.request.ClearCartRequest;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ClearCartRequestTest {

    @Test
    void clearCartRequestTest_validData_ShouldSetSuccessfully() {
        UUID uuid = UUID.randomUUID();
        ClearCartRequest clearCartRequest = new ClearCartRequest(uuid.toString());

        assertEquals(uuid.toString(), clearCartRequest.consumerId());
    }
}
