package br.com.fiap.postech.adjt.checkout.dto.checkout;

import br.com.fiap.postech.adjt.checkout.TestUtils;
import org.junit.jupiter.api.Test;

import java.util.List;

class CheckoutConsumerDtoTest {

    @Test
    void testInstance() {
        CheckoutConsumerDto dto = new CheckoutConsumerDto();
        dto.setOrders(List.of(TestUtils.buildOrderResponseDto()));
    }

}
