package br.com.fiap.postech.adjt.cart.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

class MessagesTest {

    @Test
    void testInstance() {
        assertThrows(IllegalStateException.class, Messages::new);
    }

}
