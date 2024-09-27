package br.com.fiap.postech.adjt.gateway.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatusCode;

import static org.assertj.core.api.Assertions.assertThat;

public class BadRequestExceptionTest {

    @Test
    void shouldCreateBadRequestException() {
        BadRequestException badRequestException = new BadRequestException(HttpStatusCode.valueOf(200), "ok");
        assertThat(badRequestException).isNotNull().isInstanceOf(BadRequestException.class);
    }

}
