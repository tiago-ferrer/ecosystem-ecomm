package br.com.fiap.postech.adjt.model.dto.request;

import br.com.fiap.postech.adjt.cart.model.dto.request.FindCartByCustomerIdRequest;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FindCartByCustomerIdRequestTest {

    @Test
    void findCartByCustomerIdRequestTest_validData_ShouldSetSuccessfully() {
        UUID uuid = UUID.randomUUID();
        FindCartByCustomerIdRequest findCartByCustomerIdRequestTest = new FindCartByCustomerIdRequest(uuid);

        assertEquals(uuid, findCartByCustomerIdRequestTest.consumerId());
    }

}
