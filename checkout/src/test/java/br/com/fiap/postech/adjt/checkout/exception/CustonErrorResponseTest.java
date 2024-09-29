package br.com.fiap.postech.adjt.checkout.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

class CustonErrorResponseTest {

    @Test
    void test() {
        CustonErrorResponse ex = new CustonErrorResponse("error");
        assertEquals("error", ex.getError());
        ex.setError("otherError");
        assertEquals("otherError", ex.getError());
    }

}
