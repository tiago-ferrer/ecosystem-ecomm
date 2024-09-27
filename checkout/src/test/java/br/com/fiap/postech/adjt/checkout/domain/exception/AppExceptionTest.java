package br.com.fiap.postech.adjt.checkout.domain.exception;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collection;

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

    @Test
    void testCollectionOfMessages() {
        // Testa a construção da exceção com uma coleção de mensagens
        Collection<String> messages = Arrays.asList("First error", "Second error", "Third error");

        AppException exception = assertThrows(AppException.class, () -> {
            throw new AppException(messages);
        });

        assertEquals("First error Second error Third error", exception.getMessage());
    }

    @Test
    void testCollectionWithNullMessages() {
        // Testa a construção da exceção com uma coleção contendo mensagens nulas
        Collection<String> messages = Arrays.asList("Error 1", null, "Error 2", null);

        AppException exception = assertThrows(AppException.class, () -> {
            throw new AppException(messages);
        });

        assertEquals("Error 1 Error 2", exception.getMessage());
    }

    @Test
    void testEmptyCollection() {
        // Testa a construção da exceção com uma coleção vazia
        Collection<String> messages = Arrays.asList();

        AppException exception = assertThrows(AppException.class, () -> {
            throw new AppException(messages);
        });

        assertEquals("", exception.getMessage());
    }
}
