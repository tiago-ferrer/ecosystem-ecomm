package br.com.fiap.postech.adjt.cart.service;

import br.com.fiap.postech.adjt.cart.exceptions.ErrorMessages;
import br.com.fiap.postech.adjt.cart.model.Cart;
import br.com.fiap.postech.adjt.cart.model.Item;
import br.com.fiap.postech.adjt.cart.repository.CartRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CartServiceTest {

    @InjectMocks
    private CartService cartService;

    @Mock
    private CartRepository cartRepository;

    private UUID consumerUUID;
    private String consumerId;
    private String itemId;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        consumerUUID = UUID.randomUUID();
        consumerId = consumerUUID.toString();
        itemId = "1";
    }

    @Test
    void testAddItem_NewCart() {
        when(cartRepository.findByConsumerId(consumerUUID)).thenReturn(Optional.empty());

        String result = cartService.addItem(consumerId, itemId, 2);

        assertEquals(ErrorMessages.ITEM_ADDED_SUCCESSFULLY, result);
        verify(cartRepository, times(1)).save(any(Cart.class));
    }

    @Test
    void testRemoveItem() {
        Cart cart = new Cart();
        Item item = new Item(itemId, 1);
        cart.setItems(new ArrayList<>());
        cart.getItems().add(item);

        when(cartRepository.findByConsumerId(consumerUUID)).thenReturn(Optional.of(cart));

        String result = cartService.removeItem(consumerId, itemId);

        assertEquals(ErrorMessages.ITEM_REMOVED_SUCCESSFULLY, result);
        verify(cartRepository, times(1)).save(cart);
        assertTrue(cart.getItems().isEmpty());
    }

    @Test
    void testIncrementItem() {
        Cart cart = new Cart();
        Item item = new Item(itemId, 1);
        cart.setItems(new ArrayList<>());
        cart.getItems().add(item);

        when(cartRepository.findByConsumerId(consumerUUID)).thenReturn(Optional.of(cart));

        String result = cartService.incrementItem(consumerId, itemId);

        assertEquals(ErrorMessages.ITEM_INCREMENTED_SUCCESSFULLY, result);
        verify(cartRepository, times(1)).save(cart);
        assertEquals(2, item.getQuantity());
    }

    @Test
    void testClearCart() {
        Cart cart = new Cart();
        cart.setItems(new ArrayList<>());
        cart.getItems().add(new Item(itemId, 2));

        when(cartRepository.findByConsumerId(consumerUUID)).thenReturn(Optional.of(cart));

        String result = cartService.clearCart(consumerId);

        assertEquals(ErrorMessages.CART_CLEARED_SUCCESSFULLY, result);
        verify(cartRepository, times(1)).save(cart);
        assertTrue(cart.getItems().isEmpty());
    }

    @Test
    void testGetCart_ThrowsExceptionIfEmpty() {
        Cart cart = new Cart();
        cart.setItems(new ArrayList<>());

        when(cartRepository.findByConsumerId(consumerUUID)).thenReturn(Optional.of(cart));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            cartService.getCart(consumerUUID);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals(ErrorMessages.EMPTY_CART, exception.getReason());
    }

    @Test
    void testGetCart_ReturnsCartIfNotEmpty() {
        Cart cart = new Cart();
        cart.setItems(new ArrayList<>());
        cart.getItems().add(new Item(itemId, 2));

        when(cartRepository.findByConsumerId(consumerUUID)).thenReturn(Optional.of(cart));

        Cart result = cartService.getCart(consumerUUID);

        assertNotNull(result);
        assertFalse(result.getItems().isEmpty());
    }

    @Test
    void testAddItem_InvalidConsumerIdFormat() {
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            cartService.addItem("invalid-id", itemId, 2);
        });
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals(ErrorMessages.INVALID_CONSUMER_ID_FORMAT, exception.getReason());
    }

    @Test
    void testAddItem_InvalidItemId() {
        when(cartRepository.findByConsumerId(consumerUUID)).thenReturn(Optional.empty());
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            cartService.addItem(consumerId, "", 2);
        });
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals(ErrorMessages.INVALID_ITEM_ID, exception.getReason());
    }

    @Test
    void testAddItem_NegativeQuantity() {
        when(cartRepository.findByConsumerId(consumerUUID)).thenReturn(Optional.empty());
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            cartService.addItem(consumerId, itemId, -1);
        });
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals(ErrorMessages.INVALID_ITEM_QUANTITY, exception.getReason());
    }

    @Test
    void testRemoveItem_CartNotFound() {
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            cartService.removeItem(consumerId, itemId);
        });
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals(ErrorMessages.EMPTY_CART, exception.getReason());
    }

    @Test
    void testAddItem_ExistingItem_QuantityIncreased() {
        Cart cart = new Cart();
        Item item = new Item(itemId, 1);
        cart.setItems(new ArrayList<>());
        cart.getItems().add(item);

        when(cartRepository.findByConsumerId(consumerUUID)).thenReturn(Optional.of(cart));

        String result = cartService.addItem(consumerId, itemId, 2);

        assertEquals(ErrorMessages.ITEM_ADDED_SUCCESSFULLY, result);
        verify(cartRepository, times(1)).save(cart);
        assertEquals(3, item.getQuantity()); // Quantidade deve ser atualizada
    }

}
