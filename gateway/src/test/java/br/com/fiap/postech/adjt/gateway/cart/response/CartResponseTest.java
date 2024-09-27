package br.com.fiap.postech.adjt.gateway.cart.response;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

public class CartResponseTest {

    @Test
    void shouldCreate() {
        var items = new ArrayList<ItemResponse>();
        var response = new CartResponse(items);
        assertThat(response).isNotNull().isInstanceOf(CartResponse.class);
    }

}
