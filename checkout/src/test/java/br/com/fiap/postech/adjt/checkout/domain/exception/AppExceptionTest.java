package br.com.fiap.postech.adjt.checkout.domain.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AppExceptionTest {

    @Test
    void testCustomExceptionMessage() {
        String errorMessage = "Custom error message";

        // Testa a construção da exceção com uma mensagem
        AppException exception = assertThrows(AppException.class, () -> {
            throw new AppException(errorMessage);
        });

        assertEquals(errorMessage, exception.getMessage());
    }

}