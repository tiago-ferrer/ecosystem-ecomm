package br.com.fiap.postech.adjt.gateway.checkout.response;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

public class GetOrdersByConsumerIdResponseTest {

    @Test
    void shouldCreate() {
        assertThat(new GetOrdersByConsumerIdResponse(
                new ArrayList<>()
        ))
                .isNotNull()
                .isInstanceOf(GetOrdersByConsumerIdResponse.class);
    }

}
