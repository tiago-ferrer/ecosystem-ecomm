package br.com.fiap.postech.adjt.checkout.controller.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

class StandardErrorTest {

    @Test
    void testCreate() {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        String message = "Invalid request";
        String path = "/api/test";

        StandardError error = StandardError.create(status, message, path);

        assertNotNull(error.timestamp());
        assertEquals(status.value(), error.status());
        assertEquals(status.name(), error.error());
        assertEquals(message, error.message());
        assertEquals(path, error.path());
    }
}
