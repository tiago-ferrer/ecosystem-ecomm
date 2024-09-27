package br.com.fiap.postech.adjt.checkout.controller;

import br.com.fiap.postech.adjt.checkout.dto.cart.CartDto;
import br.com.fiap.postech.adjt.checkout.dto.cart.CartRequest;
import br.com.fiap.postech.adjt.checkout.service.CartService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class CartControllerTest {

    @InjectMocks
    private CartController cartController;

    @Mock
    private CartService cartService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindByConsumerId() {
        CartRequest cartRequest = new CartRequest();
        CartDto cartDto = new CartDto();
        when(cartService.findByConsumerId(any(CartRequest.class))).thenReturn(cartDto);

        ResponseEntity<CartDto> response = cartController.findByConsumerId(cartRequest);

        assertEquals(ResponseEntity.ok(cartDto), response);
    }

    @Test
    void testFindByConsumerId_NotFound() {
        CartRequest cartRequest = new CartRequest();
        when(cartService.findByConsumerId(any(CartRequest.class))).thenReturn(null);

        ResponseEntity<CartDto> response = cartController.findByConsumerId(cartRequest);
    }
}
