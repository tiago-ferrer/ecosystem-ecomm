package br.com.fiap.postech.adjt.checkout.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;

class CartNotFoundExceptionTest {

    @Test
    void testInstance() {
        CartNotFoundException ex = new CartNotFoundException("");
        assertInstanceOf(RuntimeException.class, ex);
    }

}
