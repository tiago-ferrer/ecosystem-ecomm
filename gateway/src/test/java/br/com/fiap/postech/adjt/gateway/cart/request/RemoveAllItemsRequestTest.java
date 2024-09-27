package br.com.fiap.postech.adjt.gateway.cart.request;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class RemoveAllItemsRequestTest {

    @Test
    void shouldCreate() {
        var request = new RemoveAllItemsRequest("1234");
        assertThat(request).isNotNull().isInstanceOf(RemoveAllItemsRequest.class);
    }

}
