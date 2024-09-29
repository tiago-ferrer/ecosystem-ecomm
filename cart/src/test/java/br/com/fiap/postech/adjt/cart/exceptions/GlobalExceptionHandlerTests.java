package br.com.fiap.postech.adjt.cart.exceptions;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GlobalExceptionHandlerTests {

    @InjectMocks
    private GlobalExceptionHandler globalExceptionHandler;

    @BeforeEach
    void setUp() {
        globalExceptionHandler = new GlobalExceptionHandler();
    }

    @Test
    void testHandleResponseStatusException() {
        ResponseStatusException exception = new ResponseStatusException(HttpStatus.NOT_FOUND, "Resource not found");
        ResponseEntity<Object> response = globalExceptionHandler.handleResponseStatusException(exception);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        GlobalExceptionHandler.ErrorResponse errorResponse = (GlobalExceptionHandler.ErrorResponse) response.getBody();
        assertEquals("Resource not found", errorResponse.getError());
    }

    @Test
    void testHandleGenericException() {
        Exception exception = new Exception("Generic error");
        ResponseEntity<Object> response = globalExceptionHandler.handleGenericException(exception);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        GlobalExceptionHandler.ErrorResponse errorResponse = (GlobalExceptionHandler.ErrorResponse) response.getBody();
        assertEquals("An unexpected error occurred: Generic error", errorResponse.getError());
    }
}
