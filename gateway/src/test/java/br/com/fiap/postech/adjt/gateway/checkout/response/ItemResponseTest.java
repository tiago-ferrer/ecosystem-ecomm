package br.com.fiap.postech.adjt.gateway.checkout.response;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

public class ItemResponseTest {

    @Test
    void shouldCreate() {
        assertThat(new ItemResponse(
                1L,
                1L
        ))
                .isNotNull()
                .isInstanceOf(ItemResponse.class);
    }

}
