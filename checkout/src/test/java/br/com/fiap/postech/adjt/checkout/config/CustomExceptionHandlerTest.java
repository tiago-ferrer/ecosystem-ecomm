package br.com.fiap.postech.adjt.checkout.config;

import br.com.fiap.postech.adjt.checkout.exception.CartConsumerException;
import br.com.fiap.postech.adjt.checkout.exception.CustonErrorResponse;
import br.com.fiap.postech.adjt.checkout.exception.InvalidOrderIdException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class CustomExceptionHandlerTest {

    @InjectMocks
    private CustomExceptionHandler customExceptionHandler;

    @Test
    void testInvalidOrderId() {
        ResponseEntity<CustonErrorResponse> resp = customExceptionHandler
                .InvalidOrderId(new InvalidOrderIdException("error"));
        assertNotNull(resp.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, resp.getStatusCode());
        assertEquals("Invalid orderId format", resp.getBody().getError());
    }

    @Test
    void testCartConsumer() {
        ResponseEntity<CustonErrorResponse> resp = customExceptionHandler
                .CartConsumer(new CartConsumerException("error"));
        assertNotNull(resp.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, resp.getStatusCode());
        assertEquals("Invalid consumerId format", resp.getBody().getError());
    }


}
