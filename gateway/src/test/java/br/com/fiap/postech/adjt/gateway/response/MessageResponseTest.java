package br.com.fiap.postech.adjt.gateway.response;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class MessageResponseTest {

    @Test
    void shouldCreate() {
        var response = new MessageResponse("ok");
        assertThat(response).isNotNull().isInstanceOf(MessageResponse.class);
    }

}
