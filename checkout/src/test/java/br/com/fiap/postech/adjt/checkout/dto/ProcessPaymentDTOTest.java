package br.com.fiap.postech.adjt.checkout.dto;

import br.com.fiap.postech.adjt.checkout.controller.request.PaymentMethodFieldsRequest;
import br.com.fiap.postech.adjt.checkout.controller.request.PaymentMethodRequest;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ProcessPaymentDTOTest {

    @Test
    void shouldCreateDTO() {

        var paymentMethodFieldsRequest = new PaymentMethodFieldsRequest(
            "12345",
                "1",
                "2",
                "123",
                "Lucas"
        );

        var paymentMethod = new PaymentMethodRequest(
                "test",
                paymentMethodFieldsRequest
        );

        var dto = new ProcessPaymentDTO(
                "1234",
                100L,
                "BRL",
                paymentMethod
        );
        assertThat(dto).isNotNull();
    }

}
