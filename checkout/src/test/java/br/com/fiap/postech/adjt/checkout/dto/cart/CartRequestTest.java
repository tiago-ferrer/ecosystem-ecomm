package br.com.fiap.postech.adjt.checkout.dto.cart;

import br.com.fiap.postech.adjt.checkout.TestUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CartRequestTest {

    @Test
    void testInstance() {
        CartRequest cartRequest = new CartRequest();
        cartRequest.setConsumerId(TestUtils.genUUID().toString());
        cartRequest.setItemId(1L);
        cartRequest.setQuantity(10);
        assertEquals(TestUtils.genUUID().toString(), cartRequest.getConsumerId());
        assertEquals(1L, cartRequest.getItemId());
        assertEquals(10, cartRequest.getQuantity());
    }

}
