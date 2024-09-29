package br.com.fiap.postech.adjt.checkout.dto;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

public class CartDTOTest {

    @Test
    void shouldCreateDTO() {
        var item = new ItemDTO(1L, 1L);

        var items = new ArrayList<ItemDTO>();
        items.add(item);

        var dto = new CartDTO(items);
        assertThat(dto).isNotNull();
    }

}
