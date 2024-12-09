package br.com.fiap.postech.adjt.gateway.cart.request;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class AddItemRequestTest {

    @Test
    void shouldCreate() {
        var request = new AddItemRequest("1234", "1", "1");
        assertThat(request).isNotNull().isInstanceOf(AddItemRequest.class);
    }

}
