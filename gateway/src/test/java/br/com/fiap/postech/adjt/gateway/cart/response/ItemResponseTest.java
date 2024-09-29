package br.com.fiap.postech.adjt.gateway.cart.response;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ItemResponseTest {

    @Test
    void shouldCreate() {
        var response = new ItemResponse(1L, 1L);
        assertThat(response).isNotNull().isInstanceOf(ItemResponse.class);
    }

}
