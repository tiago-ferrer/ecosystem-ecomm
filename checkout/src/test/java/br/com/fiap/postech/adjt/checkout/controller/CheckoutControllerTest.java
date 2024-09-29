package br.com.fiap.postech.adjt.checkout.controller;

import br.com.fiap.postech.adjt.checkout.exception.CartConsumerException;
import br.com.fiap.postech.adjt.checkout.integracao.PaymentServiceAPI;
import br.com.fiap.postech.adjt.checkout.service.CheckoutService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class CheckoutControllerTest {

    @InjectMocks
    private CheckoutController checkoutController;

    @Mock
    private CheckoutService checkoutService;

    @Mock
    private PaymentServiceAPI paymentServiceAPI;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetOrdersByConsumer_NotFound() {
        String consumerId = "123";
        when(checkoutService.getOrdersByConsumer(consumerId)).thenThrow(new CartConsumerException("Consumer not found"));

        ResponseEntity<?> response = checkoutController.getOrdersByConsumer(consumerId);

        assertEquals(ResponseEntity.badRequest().body("Consumer not found"), response);
    }
}
