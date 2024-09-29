package br.com.fiap.postech.adjt.cart.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ErrorRecordTest {

    @Test
    void testInstance() {
        ErrorRecord error = new ErrorRecord("Car empty");
        assertEquals("Car empty", error.error());
    }

}
