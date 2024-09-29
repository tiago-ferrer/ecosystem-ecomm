package br.com.fiap.postech.adjt.checkout.dto;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class RemoveAllItemsDTOTest {

    @Test
    void shouldCreateDTO() {
        var dto = new RemoveAllItemsDTO("1234");
        assertThat(dto).isNotNull();
    }

}
