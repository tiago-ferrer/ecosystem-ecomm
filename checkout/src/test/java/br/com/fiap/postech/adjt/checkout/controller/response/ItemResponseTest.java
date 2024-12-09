package br.com.fiap.postech.adjt.checkout.controller.response;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ItemResponseTest {

    @Test
    void shouldCreate() {
        var response = new ItemResponse(1L, 2L);
        assertThat(response).isNotNull().isInstanceOf(ItemResponse.class);
    }

}
