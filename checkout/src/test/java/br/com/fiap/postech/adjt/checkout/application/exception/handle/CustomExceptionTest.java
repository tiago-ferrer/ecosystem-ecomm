package br.com.fiap.postech.adjt.checkout.application.exception.handle;

import br.com.fiap.postech.adjt.checkout.application.exception.CustomException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CustomExceptionTest {

    @Test
    void testCustomExceptionMessage() {
        String errorMessage = "Custom error message";

        // Testa a construção da exceção com uma mensagem
        CustomException exception = assertThrows(CustomException.class, () -> {
            throw new CustomException(errorMessage);
        });

        assertEquals(errorMessage, exception.getMessage());
    }

    @Test
    void testCustomExceptionMessageWithCause() {
        String errorMessage = "Custom error message";
        Throwable cause = new RuntimeException("Cause of the error");

        // Testa a construção da exceção com uma mensagem e uma causa
        CustomException exception = assertThrows(CustomException.class, () -> {
            throw new CustomException(errorMessage, cause);
        });

        assertEquals(errorMessage, exception.getMessage());
        assertEquals(cause, exception.getCause());
    }
}
