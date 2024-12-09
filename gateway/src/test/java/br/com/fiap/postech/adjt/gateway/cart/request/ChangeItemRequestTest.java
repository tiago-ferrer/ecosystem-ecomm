package br.com.fiap.postech.adjt.gateway.cart.request;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ChangeItemRequestTest {

    @Test
    void shouldCreate() {
        var request = new ChangeItemRequest("1234", 1L);
        assertThat(request).isNotNull().isInstanceOf(ChangeItemRequest.class);
    }

}
