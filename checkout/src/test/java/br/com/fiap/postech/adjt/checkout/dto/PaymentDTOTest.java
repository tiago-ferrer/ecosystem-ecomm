package br.com.fiap.postech.adjt.checkout.dto;

import br.com.fiap.postech.adjt.checkout.entity.PaymentStatus;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class PaymentDTOTest {

    @Test
    void shouldCreateDTO() {
        var dto = new PaymentDTO("1234", PaymentStatus.pending);
        assertThat(dto).isNotNull();
    }

}
