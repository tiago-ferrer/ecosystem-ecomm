package br.com.fiap.postech.adjt.gateway.checkout.response;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CreateOrderResponseTest {

    @Test
    void shouldCreate() {
        assertThat(new CreateOrderResponse(
                "123",
                "pending"
        ))
                .isNotNull()
                .isInstanceOf(CreateOrderResponse.class);
    }

}
