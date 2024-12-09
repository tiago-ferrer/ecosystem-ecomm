package br.com.fiap.postech.adjt.gateway.checkout.request;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CreateOrderRequestTest {

    @Test
    void shouldCreate() {
        var createOrderRequest = new CreateOrderRequest(
                "123",
                1000.50,
                "BRL",
                new PaymentMethodRequest(
                    "test",
                    new PaymentMethodFieldsRequest(
                            "1",
                            "1",
                            "1",
                            "11",
                            "Lucas"
                    )
                )
        );
        assertThat(createOrderRequest).isNotNull().isInstanceOf(CreateOrderRequest.class);
    }

}
