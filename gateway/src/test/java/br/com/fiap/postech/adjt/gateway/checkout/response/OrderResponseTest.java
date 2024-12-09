package br.com.fiap.postech.adjt.gateway.checkout.response;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

public class OrderResponseTest {

    @Test
    void shouldCreate() {
        assertThat(new OrderResponse(
                "1",
                new ArrayList<>(),
                "credit",
                1000L,
                "pending"
        ))
                .isNotNull()
                .isInstanceOf(OrderResponse.class);
    }

}
