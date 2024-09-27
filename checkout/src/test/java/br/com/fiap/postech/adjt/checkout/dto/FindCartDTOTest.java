package br.com.fiap.postech.adjt.checkout.dto;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class FindCartDTOTest {

    @Test
    void shouldCreateDTO() {
        var dto = new FindCartDTO("1234");
        assertThat(dto).isNotNull();
    }

}
