package br.com.fiap.postech.adjt.cart.controller;

import br.com.fiap.postech.adjt.cart.dto.CartConsumerRequest;
import br.com.fiap.postech.adjt.cart.dto.CartItemRequest;
import br.com.fiap.postech.adjt.cart.dto.ResponseMessage;
import br.com.fiap.postech.adjt.cart.model.Cart;
import br.com.fiap.postech.adjt.cart.service.CartService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
class CartControllerTests {

    @InjectMocks
    private CartController cartController;

    @Mock
    private CartService cartService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddItem() {
        CartItemRequest request = new CartItemRequest();
        request.setConsumerId(UUID.randomUUID().toString());
        request.setItemId("1");
        request.setQuantity(2);

        when(cartService.addItem(anyString(), anyString(), anyInt())).thenReturn("Item added");

        ResponseEntity<?> response = cartController.addItem(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Item added", ((ResponseMessage) response.getBody()).getMessage());
        verify(cartService, times(1)).addItem(anyString(), anyString(), anyInt());
    }

    @Test
    void testRemoveItem() {
        CartItemRequest request = new CartItemRequest();
        request.setConsumerId(UUID.randomUUID().toString());
        request.setItemId("1");

        when(cartService.removeItem(anyString(), anyString())).thenReturn("Item removed");

        ResponseEntity<?> response = cartController.removeItem(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Item removed", ((ResponseMessage) response.getBody()).getMessage());
        verify(cartService, times(1)).removeItem(anyString(), anyString());
    }

    @Test
    void testIncrementItem() {
        CartItemRequest request = new CartItemRequest();
        request.setConsumerId(UUID.randomUUID().toString());
        request.setItemId("1");

        when(cartService.incrementItem(anyString(), anyString())).thenReturn("Item incremented");

        ResponseEntity<?> response = cartController.incrementItem(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Item incremented", ((ResponseMessage) response.getBody()).getMessage());
        verify(cartService, times(1)).incrementItem(anyString(), anyString());
    }

    @Test
    void testGetCart() {
        CartConsumerRequest request = new CartConsumerRequest();
        request.setConsumerId(UUID.randomUUID().toString());

        Cart cart = new Cart();
        when(cartService.getCart(any(UUID.class))).thenReturn(cart);

        ResponseEntity<?> response = cartController.getCart(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(cart, response.getBody());
        verify(cartService, times(1)).getCart(any(UUID.class));
    }

    @Test
    void testClearCart() {
        CartConsumerRequest request = new CartConsumerRequest();
        request.setConsumerId(UUID.randomUUID().toString());

        when(cartService.clearCart(anyString())).thenReturn("Cart cleared");

        ResponseEntity<?> response = cartController.clearCart(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Cart cleared", ((ResponseMessage) response.getBody()).getMessage());
        verify(cartService, times(1)).clearCart(anyString());
    }

}
