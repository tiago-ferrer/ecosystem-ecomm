package br.com.fiap.postech.adjt.checkout.controller.response;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ErrorResponseTest {

    @Test
    void shouldCreate() {
        var response = new ErrorResponse("error");
        assertThat(response).isNotNull().isInstanceOf(ErrorResponse.class);
    }

}
