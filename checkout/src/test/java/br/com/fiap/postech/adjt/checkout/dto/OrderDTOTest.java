package br.com.fiap.postech.adjt.checkout.dto;

import br.com.fiap.postech.adjt.checkout.entity.PaymentStatus;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class OrderDTOTest {

    @Test
    void shouldCreateDTO() {
        List<ItemDTO> items = new ArrayList<ItemDTO>();
        var dto = new OrderDTO(
                "123",
                items,
                "credit",
                1000L,
                PaymentStatus.approved
        );
        assertThat(dto).isNotNull();
    }

}
