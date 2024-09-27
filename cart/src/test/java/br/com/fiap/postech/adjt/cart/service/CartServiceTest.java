package br.com.fiap.postech.adjt.cart.service;

import br.com.fiap.postech.adjt.cart.dto.AddItemToCartDTO;
import br.com.fiap.postech.adjt.cart.model.Cart;
import br.com.fiap.postech.adjt.cart.repository.CartRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CartServiceTest {

    @InjectMocks
    private CartService cartService;

    @Mock
    private CartRepository cartRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addItemToCart_ShouldReturnBadRequest_WhenConsumerIdIsNull() {
        AddItemToCartDTO addItem = new AddItemToCartDTO(null, 2);
        ResponseEntity<?> response = cartService.addItemToCart(null, addItem);
        assertEquals(400, response.getStatusCodeValue());
        assertTrue(response.getBody().toString().contains("Invalid consumerId format"));
    }

    @Test
    void addItemToCart_ShouldReturnBadRequest_WhenItemIdIsNull() {
        AddItemToCartDTO addItem = new AddItemToCartDTO(null, 2);
        ResponseEntity<?> response = cartService.addItemToCart(UUID.randomUUID(), addItem);
        assertEquals(400, response.getStatusCodeValue());
        assertTrue(response.getBody().toString().contains("Invalid itemId does not exist"));
    }

    @Test
    void addItemToCart_ShouldReturnBadRequest_WhenQuantityIsZeroOrNegative() {
        AddItemToCartDTO addItem = new AddItemToCartDTO(1L, 0);
        ResponseEntity<?> response = cartService.addItemToCart(UUID.randomUUID(), addItem);
        assertEquals(400, response.getStatusCodeValue());
        assertTrue(response.getBody().toString().contains("Invalid itemId quantity"));
    }

    @Test
    void addItemToCart_ShouldAddItem_WhenItemIsNew() {
        UUID consumerId = UUID.randomUUID();
        AddItemToCartDTO addItem = new AddItemToCartDTO(1L, 2);
        when(cartRepository.findByConsumerId(consumerId)).thenReturn(Collections.emptyList());

        ResponseEntity<?> response = cartService.addItemToCart(consumerId, addItem);

        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody().toString().contains("Item added to cart successfully"));

        // Verifica se o novo item foi salvo
        ArgumentCaptor<Cart> cartCaptor = ArgumentCaptor.forClass(Cart.class);
        verify(cartRepository, times(1)).save(cartCaptor.capture());
        assertEquals(consumerId, cartCaptor.getValue().getConsumerId());
        assertEquals(1L, cartCaptor.getValue().getItemId());
        assertEquals(2, cartCaptor.getValue().getQuantity());
    }

    @Test
    void addItemToCart_ShouldUpdateItem_WhenItemAlreadyExists() {
        UUID consumerId = UUID.randomUUID();
        AddItemToCartDTO addItem = new AddItemToCartDTO(1L, 2);
        Cart existingCartItem = new Cart(consumerId, 1L, 1);
        when(cartRepository.findByConsumerId(consumerId)).thenReturn(Collections.singletonList(existingCartItem));

        ResponseEntity<?> response = cartService.addItemToCart(consumerId, addItem);

        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody().toString().contains("Item added to cart successfully"));

        // Verifica se o item existente foi atualizado
        ArgumentCaptor<Cart> cartCaptor = ArgumentCaptor.forClass(Cart.class);
        verify(cartRepository, times(1)).save(cartCaptor.capture());
        assertEquals(consumerId, cartCaptor.getValue().getConsumerId());
        assertEquals(1L, cartCaptor.getValue().getItemId());
<<<<<<< HEAD
        assertEquals(3, cartCaptor.getValue().getQuantity());
=======
        assertEquals(3, cartCaptor.getValue().getQuantity()); // 1 + 2
>>>>>>> 79b1335a998168ed3b4359d3ce24c4ea73b61784
    }

    @Test
    void removeItemFromCart_ShouldReturnBadRequest_WhenConsumerIdIsInvalid() {
        ResponseEntity<?> response = cartService.removeItemFromCart("invalid-uuid", 1L);
        assertEquals(400, response.getStatusCodeValue());
        assertTrue(response.getBody().toString().contains("Invalid consumerId format"));
    }

    @Test
    void removeItemFromCart_ShouldReturnBadRequest_WhenItemNotFound() {
        String consumerId = UUID.randomUUID().toString();
        when(cartRepository.findByConsumerIdAndItemId(any(), any())).thenReturn(Optional.empty());

        ResponseEntity<?> response = cartService.removeItemFromCart(consumerId, 1L);
        assertEquals(400, response.getStatusCodeValue());
        assertTrue(response.getBody().toString().contains("Item not found in cart"));
    }

    @Test
    void removeItemFromCart_ShouldRemoveItem_WhenItemExists() {
        String consumerId = UUID.randomUUID().toString();
        Cart existingItem = new Cart(UUID.fromString(consumerId), 1L, 1);
        when(cartRepository.findByConsumerIdAndItemId(UUID.fromString(consumerId), 1L)).thenReturn(Optional.of(existingItem));

        ResponseEntity<?> response = cartService.removeItemFromCart(consumerId, 1L);
        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody().toString().contains("Item removed from cart successfully"));

        // Verifica se o item foi excluído
        verify(cartRepository, times(1)).delete(existingItem);
    }
<<<<<<< HEAD
=======

    // Outros testes para removeAllItemsFromCart e getCartItems...
>>>>>>> 79b1335a998168ed3b4359d3ce24c4ea73b61784
}

